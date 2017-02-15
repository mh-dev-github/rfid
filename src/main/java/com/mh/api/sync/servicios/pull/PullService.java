package com.mh.api.sync.servicios.pull;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import com.mh.model.esb.domain.esb.Integracion;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.repo.esb.IntegracionRepository;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PullService<T> {
	@Value("${sync.InsertIntoStageBatchSize}")
	protected int INSERT_INTO_STAGE_BATCH_SIZE;

	@Autowired
	private IntegracionRepository integracionRepository;

	@Qualifier("erpJdbcTemplate")
	@Autowired
	private NamedParameterJdbcTemplate erpJdbcTemplate;

	@Qualifier("stageJdbcTemplate")
	@Autowired
	private NamedParameterJdbcTemplate stageJdbcTemplate;

	protected PullService() {
		super();
	}

	// -------------------------------------------------------------------------------------
	// Pull
	// -------------------------------------------------------------------------------------
	@Transactional(value = "stageTransactionManager")
	public void pull() {
		log.info("{}:Inicio Pull", getClass().getName());

		pushRowsToStage(pullRowsFromErp());
		merge();

		log.info("{}:Fin Pull", getClass().getName());
	}

	// -------------------------------------------------------------------------------------
	// Integracion
	// -------------------------------------------------------------------------------------
	abstract protected IntegracionType getIntegracionType();

	protected Integracion getIntegracion(IntegracionType codigo) {
		return integracionRepository.findOneByCodigo(codigo.toString());
	}

	protected LocalDateTime getFechaUltimoPull() {
		return getIntegracion(getIntegracionType()).getFechaUltimoPull();
	}

	// -------------------------------------------------------------------------------------
	// PULL ROWS FROM ERP
	// -------------------------------------------------------------------------------------
	@Transactional(value = "stageTransactionManager")
	protected List<T> pullRowsFromErp() {
		String sql = getSQLSelectFromErp();
		val parametros = buildSqlParameterSource();

		return erpJdbcTemplate.query(sql, parametros, getRowMapper());
	}

	abstract protected String getSQLSelectFromErp();

	protected MapSqlParameterSource buildSqlParameterSource() {
		return new MapSqlParameterSource();
	}

	abstract protected RowMapper<T> getRowMapper();

	// -------------------------------------------------------------------------------------
	// PUSH ROWS TO STAGE
	// -------------------------------------------------------------------------------------
	@Transactional(value = "stageTransactionManager")
	protected void pushRowsToStage(List<T> rows) {
		tuncateStageTable();
		batchUpdate(rows);
	}

	protected void tuncateStageTable() {
		stageJdbcTemplate.getJdbcOperations().execute(getSQLTruncateStageTable());
	}

	abstract protected String getSQLTruncateStageTable();

	private void batchUpdate(final List<T> list) {
		final int batchSize = INSERT_INTO_STAGE_BATCH_SIZE;
		String sql = getSQLInsertIntoStage();

		for (int i = 0; i < list.size(); i += batchSize) {
			final List<T> batchList = list.subList(i, i + batchSize > list.size() ? list.size() : i + batchSize);
			SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(batchList.toArray());
			stageJdbcTemplate.batchUpdate(sql, params);
		}
	}

	abstract protected String getSQLInsertIntoStage();

	// -------------------------------------------------------------------------------------
	// MERGE
	// -------------------------------------------------------------------------------------
	@Transactional(value = "stageTransactionManager")
	private void merge() {
		stageJdbcTemplate.getJdbcOperations().execute(getSQLProcedureMerge());
	}

	abstract protected String getSQLProcedureMerge();
}
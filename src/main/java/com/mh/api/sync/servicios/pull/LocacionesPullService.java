package com.mh.api.sync.servicios.pull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.pull.dto.LocacionPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

@Service
public class LocacionesPullService extends PullService<LocacionPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.LOCACIONES;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+ " WITH" 
		+ " cte_00 AS(" 
		+ "    SELECT " 
		+ "        COALESCE(RTRIM(LTRIM(a.FILIAL)),'') AS name,"
		+ "        COALESCE(RTRIM(LTRIM(a.COD_FILIAL)),'') AS externalId,"
		+ "        COALESCE(RTRIM(LTRIM(a.DIRECCION)),'') AS [address],"
		+ "        REPLACE(COALESCE(RTRIM(LTRIM(a.TIPO_FILIAL)),''),' ','_') AS [type],"
		+ "        a.DATA_PARA_TRANSFERENCIA" 
		+ "    FROM dbo.MH_MAESTRO_TIENDAS_RFID a" 
		+ "    WHERE"
		+ "        a.DATA_PARA_TRANSFERENCIA >= :fechaUltimoPull" 
		+ " )" 
		+ " SELECT" 
		+ "    a.name,"
		+ "    a.externalId," 
		+ "    a.[address]," 
		+ "    a.[type]," 
		+ "    GETDATE() AS fecha_ultimo_pull"
		+ " FROM cte_00 a" 
		+ " ORDER BY" 
		+ "    a.DATA_PARA_TRANSFERENCIA";
		// @formatter:on
	}
	
	@Override
	protected MapSqlParameterSource buildSqlParameterSource() {
		MapSqlParameterSource paramSource = super.buildSqlParameterSource();
		paramSource.addValue("fechaUltimoPull", getFechaUltimoPull());
		return paramSource;
	}

	@Override
	protected String getSQLTruncateStageTable() {
		return "TRUNCATE TABLE stage.Locaciones";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
		+ " INSERT INTO stage.Locaciones" 
		+ "    (externalId,name,[address],[type])"
		+ "	VALUES" 
		+ "    (:externalId,:name,:address,:type)";		
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.LocacionesMerge";
	}

	@Override
	protected RowMapper<LocacionPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return LocacionPullDTO
				.builder()
				.externalId(rs.getString("externalId"))

				.name(rs.getString("name"))
				.address(rs.getString("address"))
				.type(rs.getString("type"))
				
				.build();
		};
		// @formatter:on
	}
}

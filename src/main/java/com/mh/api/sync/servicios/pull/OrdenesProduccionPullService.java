package com.mh.api.sync.servicios.pull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.pull.dto.OrdenProduccionPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

@Service
public class OrdenesProduccionPullService extends PullService<OrdenProduccionPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.ORDENES_DE_PRODUCCION;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+" WITH "
		+" cte_00 AS "
		+" ( "
		+"     SELECT  "
		+"          COALESCE(LTRIM(RTRIM(a.OP)),'') AS OP "
		+"         ,COALESCE(LTRIM(RTRIM(a.CODIGO_BARRA)),'') AS CODIGO_BARRA "
		+"         ,COALESCE(a.CANTIDAD,0) as CANTIDAD "
		+"         ,COALESCE(LTRIM(RTRIM(a.RECURSO_PRODUTIVO)),'') AS RECURSO_PRODUTIVO "
		+"         ,COALESCE(CONVERT(VARCHAR(10),FECHA,120),'') AS FECHA "
		+"         ,COALESCE(a.SECUENCIA,'') AS SECUENCIA "		
		+"     FROM dbo.MH_ORDENES_PRODUCCION2_RFID a "
		+" ), "
		+" cte_01 AS "
		+" ( "
		+"     SELECT "
		+"          a.OP AS externalId "
		+"         ,a.CODIGO_BARRA AS sku "
		+"         ,a.CANTIDAD AS amount "
		+"         ,a.RECURSO_PRODUTIVO AS supplier "
		+"         ,a.FECHA AS arrivalDate "
		+"         ,DENSE_RANK() OVER(PARTITION BY a.OP ORDER BY secuencia) AS orden "
		+"         ,GETDATE() AS fecha_ultimo_pull "
		+"     FROM cte_00 a "
		+" ) "
		+" SELECT "
		+"     * "
		+" FROM cte_01 a "
		+" WHERE 0 = 0 "
		+" AND a.orden = 1 "		
		+"  ";
		// @formatter:on
	}

	@Override
	protected String getSQLTruncateStageTable() {
		return "TRUNCATE TABLE stage.OrdenesProduccion";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
		+ " INSERT INTO stage.OrdenesProduccion" 
		+ "    (externalId,supplier,arrivalDate,sku,amount)"
		+ "	VALUES" 
		+ "    (:externalId,:supplier,:arrivalDate,:sku,:amount)";		
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.OrdenesProduccionMerge";
	}

	@Override
	protected RowMapper<OrdenProduccionPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return OrdenProduccionPullDTO
				.builder()
				.externalId(rs.getString("externalId"))

				.supplier(rs.getString("supplier"))
				.arrivalDate(rs.getString("arrivalDate"))
				.sku(rs.getString("sku"))
				.amount(rs.getInt("amount"))
				
				.build();
		};
		// @formatter:on
	}
}

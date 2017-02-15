package com.mh.api.sync.servicios.pull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.pull.dto.EntradaProductoPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

@Service
public class EntradasProductoPullService extends PullService<EntradaProductoPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.ENTRADAS_PT;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+" WITH "
		+" cte_00 AS "
		+" ( "
		+"     SELECT  "
		+"          COALESCE(LTRIM(RTRIM(a.NO_ENTRADA)),'') AS NO_ENTRADA "
		+"         ,COALESCE(LTRIM(RTRIM(a.CODIGO_BARRA)),'') AS CODIGO_BARRA "
		+"         ,COALESCE(a.CANT,0) as CANT "
		+"         ,COALESCE(LTRIM(RTRIM(a.FABRICANTE)),'') AS FABRICANTE "
		+"         ,COALESCE(CONVERT(VARCHAR(10),EMISSAO,120),'') AS EMISSAO "
		+"         ,COALESCE(LTRIM(RTRIM(a.DESCRIPCION_ENT)),'') AS DESCRIPCION_ENT "
		+"     FROM dbo.MH_ENTRADAS_DE_PT_CON_COD_BARRAS_WMS a "
		+" ) "
		+" SELECT "
		+"      a.NO_ENTRADA AS externalId "
		+"     ,a.CODIGO_BARRA AS sku "
		+"     ,a.CANT AS amount "
		+"     ,'NO FABRICA' AS supplier "
		+"     ,a.EMISSAO AS arrivalDate "
		+"     ,a.DESCRIPCION_ENT AS concept "
		+"     ,GETDATE() AS fecha_ultimo_pull "
		+" FROM cte_00 a "
		+"  ";
		// @formatter:on
	}

	@Override
	protected String getSQLTruncateStageTable() {
		return "TRUNCATE TABLE stage.EntradasProductoTerminado";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
		+ " INSERT INTO stage.EntradasProductoTerminado" 
		+ "    (externalId,supplier,arrivalDate,concept,sku,amount)"
		+ "	VALUES" 
		+ "    (:externalId,:supplier,:arrivalDate,:concept,:sku,:amount)";		
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.EntradasProductoTerminadoMerge";
	}

	@Override
	protected RowMapper<EntradaProductoPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return EntradaProductoPullDTO
				.builder()
				.externalId(rs.getString("externalId"))

				.supplier(rs.getString("supplier"))
				.concept(rs.getString("concept"))
				.arrivalDate(rs.getString("arrivalDate"))
				.sku(rs.getString("sku"))
				.amount(rs.getInt("amount"))
				
				.build();
		};
		// @formatter:on
	}
}

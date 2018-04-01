package com.mh.servicios.salidasTienda;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.mh.dto.servicios.sync.pull.SalidaTiendaPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.services.PullService;

@Service
public class SalidasTiendaPullService extends PullService<SalidaTiendaPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.SALIDAS_TIENDA;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+" WITH "
		+" cte_00 AS "
		+" ( "
		+"     SELECT "
		+"          COALESCE(LTRIM(RTRIM(a.FILIAL)),'') AS BODEGA_ORIGEN "
		+"         ,COALESCE(LTRIM(RTRIM(a.FILIAL_DESTINO)),'') AS BODEGA_DESTINO "
		+"         ,COALESCE(LTRIM(RTRIM(a.NUMERO_SALIDA)),'') AS NUMERO_SALIDA "
		+"         ,COALESCE(CONVERT(VARCHAR(10),a.EMISSAO,120),'') AS EMISSAO "
		+"  "
		+"         ,COALESCE(LTRIM(RTRIM(a.CODIGO_BARRA)),'') AS CODIGO_BARRA "
		+"         ,COALESCE(a.CANTIDAD,0) AS CANTIDAD "
		+"     FROM dbo.MH_SALIDAS_TIENDAS_RFID2 a "
		+"     WHERE "
		+"         a.DATA_PARA_TRANSFERENCIA >= :fechaUltimoPull "
		+" ), "
		+" cte_01 AS "
		+" ( "
		+" SELECT "
		+"      a.NUMERO_SALIDA as externalId "
		+"     ,a.BODEGA_ORIGEN AS sourceId "
		+"     ,a.BODEGA_DESTINO AS destinationId "
		+"     ,a.EMISSAO as expectedShipmentDate "
		+"  "
		+"     ,a.CODIGO_BARRA AS sku "
		+"     ,a.CANTIDAD AS amount "
		+"  "
		+"     ,a.BODEGA_ORIGEN "
		+"     ,a.BODEGA_DESTINO "
		+"  "
		+"     ,GETDATE() AS fecha_ultimo_pull "
		+" FROM cte_00 a "
		+" ) "
		+" SELECT "
		+"     * "
		+" FROM cte_01 a "
		+"  ";
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
		return "TRUNCATE TABLE stage.SalidasTiendas";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
		+ " INSERT INTO stage.SalidasTiendas" 
		+ "    (externalId,sourceId,destinationId,expectedShipmentDate,sku,amount,BODEGA_ORIGEN,BODEGA_DESTINO)"
		+ "	VALUES" 
		+ "    (:externalId,:sourceId,:destinationId,:expectedShipmentDate,:sku,:amount,:bodegaOrigen,:bodegaDestino)";		
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.SalidasTiendasMerge";
	}

	@Override
	protected RowMapper<SalidaTiendaPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return SalidaTiendaPullDTO
				.builder()
				.externalId(rs.getString("externalId"))

				.sourceId(rs.getString("sourceId"))
				.destinationId(rs.getString("destinationId"))
				.expectedShipmentDate(rs.getString("expectedShipmentDate"))
				.sku(rs.getString("sku"))
				.amount(rs.getInt("amount"))
				.bodegaOrigen(rs.getString("BODEGA_ORIGEN"))
				.bodegaDestino(rs.getString("BODEGA_DESTINO"))
				
				.build();
		};
		// @formatter:on
	}
}

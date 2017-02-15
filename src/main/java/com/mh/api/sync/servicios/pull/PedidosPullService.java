package com.mh.api.sync.servicios.pull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.pull.dto.PedidoPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

@Service
public class PedidosPullService extends PullService<PedidoPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.PEDIDOS;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+" WITH "
		+" cte_00 AS "
		+" ( "
		+"     SELECT "
		+"          COALESCE(LTRIM(RTRIM(a.BODEGA)),'') AS BODEGA_ORIGEN "
		+"         ,'ALISTAMIENTO' AS BODEGA_DESTINO "
		+"         ,COALESCE(LTRIM(RTRIM(a.PEDIDO)),'') AS PEDIDO "
		+"  "
		+"         ,COALESCE(LTRIM(RTRIM(a.CODIGO_BARRA)),'') AS CODIGO_BARRA "
		+"         ,COALESCE(a.CANT_X_ENTREGAR,0) AS CANT_X_ENTREGAR "
		+"         ,COALESCE(CONVERT(VARCHAR(10),a.LIMITE_ENTREGA,120),'') AS LIMITE_ENTREGA "
		+"          "
		+"         ,COALESCE(LTRIM(RTRIM(a.CLIENTE)),'') AS CLIENTE "
		+"         ,COALESCE(LTRIM(RTRIM(a.AGENCIA)),'') AS AGENCIA "
		+"     FROM dbo.MH_PEDIDOS_PEND_CON_TALLAS1_RFID a "
		+" ), "
		+" cte_01 AS "
		+" ( "
		+"     SELECT "
		+" 			 a.PEDIDO as externalId "
		+" 			,a.BODEGA_ORIGEN AS sourceId "
		+"     		,a.BODEGA_DESTINO AS destinationId "
		+"  "
		+"     		,a.CODIGO_BARRA AS sku "
		+"     		,a.CANT_X_ENTREGAR AS amount "
		+"     		,a.LIMITE_ENTREGA as expectedShipmentDate "
		+"  "
		+"     		,a.CLIENTE AS Cliente "
		+"     		,a.AGENCIA AS Agencia "
		+"     		,a.BODEGA_ORIGEN "
		+"     		,a.BODEGA_DESTINO "
		+"  "
		+" 		FROM cte_00 a "
		+" ) "
		+" SELECT "
		+"     * "
		+" FROM cte_01 a "
		+"  ";
		// @formatter:on
	}
	
	@Override
	protected String getSQLTruncateStageTable() {
		return "TRUNCATE TABLE stage.Despachos";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
		+ " INSERT INTO stage.Despachos" 
		+ "    (externalId,sourceId,destinationId,cliente,agencia,sku,amount,expectedShipmentDate,BODEGA_ORIGEN,BODEGA_DESTINO)"
		+ "	VALUES" 
		+ "    (:externalId,:sourceId,:destinationId,:cliente,:agencia,:sku,:amount,:expectedShipmentDate,:bodegaOrigen,:bodegaDestino)";		
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.DespachosMerge";
	}

	@Override
	protected RowMapper<PedidoPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return PedidoPullDTO
				.builder()
				.externalId(rs.getString("externalId"))

				.sourceId(rs.getString("sourceId"))
				.destinationId(rs.getString("destinationId"))
				.cliente(rs.getString("cliente"))
				.agencia(rs.getString("agencia"))

				.sku(rs.getString("sku"))
				.amount(rs.getInt("amount"))
				.expectedShipmentDate(rs.getString("expectedShipmentDate"))				
				.bodegaOrigen(rs.getString("BODEGA_ORIGEN"))
				.bodegaDestino(rs.getString("BODEGA_DESTINO"))
				
				.build();
		};
		// @formatter:on
	}
}

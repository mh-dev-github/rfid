package com.mh.servicios.pedidos;

import org.springframework.stereotype.Service;

import com.mh.dto.amqp.RequestDTO;

@Service("update")
public class PedidosUpdatePushService extends PedidosPushService {

	@Override
	protected String getSQLSelectFromPendingChanges(RequestDTO request) {
		// @formatter:off
		String sql = "" 
		+ "\n WITH "
		+ "\n cte_00 AS "
		+ "\n ( "
		+ "\n     SELECT "
		+ "\n         a.externalId "
		+ "\n     FROM "
		+ "\n     ( "
		+ "\n         VALUES "
		+ "\n             @externalId "
		+ "\n             (NULL) "
		+ "\n     ) a(externalId) "
		+ "\n     WHERE "
		+ "\n         a.externalId IS NOT NULL "
		+ "\n ), "
		+ "\n cte_01 AS "
		+ "\n ( "
		+ "\n     SELECT "
		+ "\n         a.mid "
		+ "\n     FROM msg.Despachos a "
		+ "\n     LEFT OUTER JOIN cte_00 b ON "
		+ "\n         b.externalId = a.externalId "
		+ "\n     WHERE 0 = 0 "
		+ "\n     AND a.tipo_cambio IN ('U')   "
		+ "\n     AND a.estado_cambio IN ('PENDIENTE','REINTENTO')  "
		+ "\n     AND a.fecha_ultimo_pull < :fechaUltimoPull   "
		+ "\n     AND (b.externalId IS NOT NULL OR NOT EXISTS(SELECT a.externalId FROM cte_00 a)) "		
		+ "\n     ORDER BY  "
		+ "\n          a.intentos "
		+ "\n         ,a.mid "
		+ "\n     OFFSET 0 ROWS  "
		+ "\n     FETCH NEXT :fetch + 1 ROWS ONLY "
		+ "\n ) "		
		+ "\n SELECT "
		+ "\n      a.mid "
		+ "\n FROM cte_01 a "
		+"  ";
		// @formatter:on

		StringBuffer sb = new StringBuffer();
		for (String e : request.getExternalId()) {
			sb.append("('").append(e).append("'),");
		}
		String externalId = sb.toString();
		sql = sql.replaceAll("@externalId", externalId);
		return sql;
	}
}

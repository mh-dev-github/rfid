package com.mh.api.sync.servicios.pull;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.mh.api.sync.servicios.pull.dto.ProductoPullDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

@Service
public class ProductosPullService extends PullService<ProductoPullDTO> {

	@Override
	protected IntegracionType getIntegracionType() {
		return IntegracionType.PRODUCTOS;
	}

	@Override
	protected String getSQLSelectFromErp() {
		// @formatter:off
		return "" 
		+" WITH "
		+" cte_00 AS( "
		+"     SELECT "
		+"          COALESCE(RTRIM(LTRIM(a.CODIGO_BARRA)),'') AS externalId "
		+"         ,LEFT(COALESCE(RTRIM(LTRIM(a.CODIGO_BARRA)),''),7) AS companyPrefix "
		+"         ,COALESCE(RTRIM(LTRIM(a.DESCRIPCION)),'') AS [name] "
		+"         ,RIGHT(COALESCE(RTRIM(LTRIM(a.CODIGO_BARRA)),''),6) AS reference "
		+"         ,COALESCE(RTRIM(LTRIM(a.CODIGO_BARRA)),'') AS ean "
		+"  "
		+"         ,COALESCE(RTRIM(LTRIM(a.COLOR)),'') AS color "
		+"         ,COALESCE(RTRIM(LTRIM(a.COD_COLOR)),'') AS codigoColor "
		+"         ,COALESCE(RTRIM(LTRIM(a.TALLA)),'') AS talla "
		+"         ,COALESCE(RTRIM(LTRIM(a.TIPO_PRODUTO)),'') AS tipoProducto "
		+"         ,COALESCE(RTRIM(LTRIM(a.COLECCION)),'') AS coleccion "
		+"         ,COALESCE(RTRIM(LTRIM(a.GRUPO_PRODUTO)),'') AS grupoProducto "
		+"         ,COALESCE(RTRIM(LTRIM(a.SUBGRUPO_PRODUTO)),'') AS subGrupoProducto "
		+"         ,COALESCE(RTRIM(LTRIM(a.FABRICANTE)),'') AS fabricante "
		+"         ,COALESCE(RTRIM(LTRIM(a.TEMPORADA)),'') AS temporada "
		+"         ,COALESCE(RTRIM(LTRIM(a.PRODUCTO)),'') AS referencia "
		+"         ,COALESCE(RTRIM(LTRIM(a.MODELO)),'') AS modelo "
		+"         ,COALESCE(RTRIM(LTRIM(a.GENERO)),'') AS Genero "
		+"         ,a.DATA_PARA_TRANSFERENCIA "
		+"     FROM dbo.MH_MAESTRO_PRODUCTOS_ARIADNA1_RFID a "
		+"     WHERE "
		+"         a.DATA_PARA_TRANSFERENCIA >= :fechaUltimoPull "
		+" ) "
		+" SELECT "
		+"      a.externalId "
		+"     ,a.companyPrefix "
		+"     ,a.[name] "
		+"     ,a.reference "
		+"     ,a.ean "
		+"  "
		+"     ,a.color "
		+"     ,a.codigoColor "
		+"     ,a.talla "
		+"     ,a.tipoProducto "
		+"     ,a.coleccion "
		+"     ,a.grupoProducto "
		+"     ,a.subGrupoProducto "
		+"     ,a.fabricante "
		+"     ,a.temporada "
		+"     ,a.referencia "
		+"     ,a.modelo "
		+"     ,a.genero"		
		+"  "
		+" FROM cte_00 a "

		+" ";

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
		return "TRUNCATE TABLE stage.Productos";
	}

	@Override
	protected String getSQLInsertIntoStage() {
		// @formatter:off
		return "" 
			+ " INSERT INTO [stage].[Productos] " 
			+ "     ([externalId],[companyPrefix],[name],[reference],[ean],[color],[codigoColor],[talla],[tipoProducto],[coleccion],[grupoProducto],[subGrupoProducto],[fabricante],[temporada],[referencia],[modelo],[genero]) " 
			+ " VALUES " 
			+ "     (:externalId,:companyPrefix,:name,:reference,:ean,:color,:codigoColor,:talla,:tipoProducto,:coleccion,:grupoProducto,:subGrupoProducto,:fabricante,:temporada,:referencia,:modelo,:genero)  ";
		// @formatter:on
	}

	@Override
	protected String getSQLProcedureMerge() {
		return "esb.ProductosMerge";
	}

	@Override
	protected RowMapper<ProductoPullDTO> getRowMapper() {
		// @formatter:off
		return (rs, rowNum) -> {
			return ProductoPullDTO
				.builder()
				.externalId(rs.getString("externalId"))
				   
				.companyPrefix(rs.getString("companyPrefix"))
				.name(rs.getString("name"))
				.reference(rs.getString("reference"))
				.ean(rs.getString("ean"))
				.color(rs.getString("color"))
				.codigoColor(rs.getString("codigoColor"))
				.talla(rs.getString("talla"))
				.tipoProducto(rs.getString("tipoProducto"))
				.coleccion(rs.getString("coleccion"))
				.grupoProducto(rs.getString("grupoProducto"))
				.subGrupoProducto(rs.getString("subGrupoProducto"))
				.fabricante(rs.getString("fabricante"))
				.temporada(rs.getString("temporada"))
				.referencia(rs.getString("referencia"))
				.modelo(rs.getString("modelo"))
				.genero(rs.getString("genero"))
				
				.build();
		};
		// @formatter:on
	}
}

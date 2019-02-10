package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.RekeningListBOView;

import oracle.jdbc.internal.OracleTypes;

@Component
public class RiwayatTransaksiBODao extends ParentDao<RekeningListBOView>{
	public List<RekeningListBOView> getRekeningList(int start, int length, String cifKey, String nomorRekening,
			String namaNasabah, String currency) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
			.withSchemaName(CommonCons.DB_SCHEMA)
			.withProcedureName("SP_BO_REKENING_LIST")
			.declareParameters(
					new SqlOutParameter("C1", OracleTypes.CURSOR),
					new SqlParameter("P_START", OracleTypes.INTEGER),
					new SqlParameter("P_LENGTH", OracleTypes.INTEGER),
					new SqlParameter("P_CIF_KEY", OracleTypes.VARCHAR),
					new SqlParameter("P_NAMA_NASABAH", OracleTypes.VARCHAR),
					new SqlParameter("P_ACCOUNT_NO", OracleTypes.VARCHAR),
					new SqlParameter("P_CURRENCY", OracleTypes.VARCHAR)
					);
		
		Map<String, Object> mValues = caller.execute(start, length, cifKey, namaNasabah, nomorRekening, currency);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>)mValues.get("C1");
		List<RekeningListBOView> res = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			res.add(serialize(RekeningListBOView.class, row));
		}
		return res;
	}
	
	public int getRekeningListCount(int start, int length, String cifKey, String nomorRekening,
			String namaNasabah, String currency) {
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_BO_COUNT_REKENING_LIST")
				.declareParameters(
						new SqlOutParameter("RES", OracleTypes.INTEGER),
						new SqlParameter("P_CIF_KEY", OracleTypes.VARCHAR),
						new SqlParameter("P_NAMA_NASABAH", OracleTypes.VARCHAR),
						new SqlParameter("P_ACCOUNT_NO", OracleTypes.VARCHAR),
						new SqlParameter("P_CURRENCY", OracleTypes.VARCHAR)
						);

		Map<String, Object> mValues = caller.execute(cifKey,  namaNasabah, nomorRekening,currency);

		return ((Integer) mValues.get("RES")).intValue();
	}
}

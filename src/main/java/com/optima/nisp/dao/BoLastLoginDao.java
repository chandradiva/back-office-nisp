package com.optima.nisp.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import oracle.jdbc.internal.OracleTypes;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.BoLastLogin;

@Component
public class BoLastLoginDao extends ParentDao<BoLastLogin>{

	public void save(String username){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
			.withSchemaName(CommonCons.DB_SCHEMA)
			.withProcedureName("SP_SAVE_BO_LAST_LOGIN")
			.declareParameters(new SqlParameter("P_USERNAME", OracleTypes.VARCHAR));
		
		caller.execute(username);
	}
	
	public Date getLastLogin(String username){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
			.withSchemaName(CommonCons.DB_SCHEMA)
			.withProcedureName("SP_GET_BO_LAST_LOGIN")
			.declareParameters(
					new SqlOutParameter("PO_LAST_LOGIN", OracleTypes.TIMESTAMP),
					new SqlParameter("P_USERNAME", OracleTypes.VARCHAR)
					);
		
		Map<String, Object> mValues = caller.execute(username);
		Timestamp ts = (Timestamp)mValues.get("PO_LAST_LOGIN");		
		return new Date(ts.getTime());
	}
}

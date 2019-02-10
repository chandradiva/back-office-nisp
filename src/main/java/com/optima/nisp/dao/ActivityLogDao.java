package com.optima.nisp.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;


public class ActivityLogDao extends ParentDao<String>{

//	public void insertUrlMapping(Map<String, String> mapUrl) {
//		ARRAY aUrlPath = null;
//		ARRAY aActionName = null;
//		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(apiTemplate).withProcedureName("SP_URL_MAPPING_I");
//		try {
//			ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("WEBSTATEMENT.ARRAY_LIST", apiTemplate.getDataSource().getConnection());
//			aUrlPath = new ARRAY(arrayDescriptor, apiTemplate.getDataSource().getConnection(), mapUrl.keySet().toArray());
//
//			aActionName = new ARRAY(arrayDescriptor, apiTemplate.getDataSource().getConnection(), mapUrl.values().toArray());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		jdbcCall.addDeclaredParameter(new SqlParameter("V_URL_PATH", java.sql.Types.ARRAY));
//		jdbcCall.addDeclaredParameter(new SqlParameter("V_ACTION_NM", java.sql.Types.ARRAY));
//		Map<String, Object> inParamsValue = new HashMap<String, Object>();
//
//		inParamsValue.put("V_URL_PATH", aUrlPath);
//		inParamsValue.put("V_ACTION_NM", aActionName);
//		jdbcCall.execute(inParamsValue);
//
//	}
//
//	public void insertUrlMapping(String[] urlMapping, String[] actionName) {
//		ARRAY aUrlPath = null;
//		ARRAY aActionName = null;
//		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(apiTemplate).withProcedureName("SP_URL_MAPPING_I");
//		try {
//			ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("WEBSTATEMENT.ARRAY_LIST", apiTemplate.getDataSource().getConnection());
//			aUrlPath = new ARRAY(arrayDescriptor, apiTemplate.getDataSource().getConnection(), urlMapping);
//
//			aActionName = new ARRAY(arrayDescriptor, apiTemplate.getDataSource().getConnection(), actionName);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		jdbcCall.addDeclaredParameter(new SqlParameter("V_URL_PATH", java.sql.Types.ARRAY));
//		jdbcCall.addDeclaredParameter(new SqlParameter("V_ACTION_NM", java.sql.Types.ARRAY));
//		Map<String, Object> inParamsValue = new HashMap<String, Object>();
//
//		inParamsValue.put("V_URL_PATH", aUrlPath);
//		inParamsValue.put("V_ACTION_NM", aActionName);
//		jdbcCall.execute(inParamsValue);
//
//	}

	public void insertUserActivity(String urlPath, String reqMethod, String userID, String groupName, Date startTime, Date finishTime, String sessionID, String requestMap, String source, String ipAddress, String browser, String information) {
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(template).withProcedureName("SP_USR_LOG_I");
		jdbcCall.addDeclaredParameter(new SqlParameter("V_URL_PATH", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_REQ_METHOD", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_USER_ID", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_GROUP_NAME", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_TIME_STARTED", java.sql.Types.TIMESTAMP));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_TIME_FINISHED", java.sql.Types.TIMESTAMP));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_TIME_TAKEN", java.sql.Types.NUMERIC));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_SESSION_ID", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_REQ_PARAM", java.sql.Types.CLOB));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_SRC", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_IP_ADDRESS", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_BROWSER", java.sql.Types.NVARCHAR));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_INFORMATION", java.sql.Types.CLOB));
		jdbcCall.addDeclaredParameter(new SqlParameter("V_CIF_KEY", java.sql.Types.NVARCHAR));
		Map<String, Object> inParamsValue = new HashMap<String, Object>();

		inParamsValue.put("V_URL_PATH", urlPath);
		inParamsValue.put("V_REQ_METHOD", reqMethod);
		inParamsValue.put("V_USER_ID", userID);
		inParamsValue.put("V_GROUP_NAME", groupName);
		inParamsValue.put("V_TIME_STARTED", startTime);
		inParamsValue.put("V_TIME_FINISHED", finishTime);
		inParamsValue.put("V_TIME_TAKEN", finishTime.getTime() - startTime.getTime());
		inParamsValue.put("V_SESSION_ID", sessionID);
		inParamsValue.put("V_REQ_PARAM", requestMap);
		inParamsValue.put("V_SRC", source);
		inParamsValue.put("V_IP_ADDRESS", ipAddress);
		inParamsValue.put("V_BROWSER", browser);
		inParamsValue.put("V_INFORMATION", information);
		inParamsValue.put("V_CIF_KEY", null);
		jdbcCall.execute(inParamsValue);
	}

}

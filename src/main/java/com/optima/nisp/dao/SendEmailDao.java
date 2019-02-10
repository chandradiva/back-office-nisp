package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jdbc.support.oracle.SqlArrayValue;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.EmailContentType;
import com.optima.nisp.constanta.SendEmailStatus;
import com.optima.nisp.model.SendEmail;
import com.optima.nisp.response.SendEmailResponse;

import oracle.jdbc.internal.OracleTypes;

@Component
public class SendEmailDao extends ParentDao<SendEmail> {

	public int getTotalRow(String cifKey, String accountNumber, String periode, int status) {
		String sql;
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = "AND STATUS = " + status;
		} else {
			sqlStatus = "AND STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' AND PERIODE = "+periode+" "
					+ sqlStatus+")";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ sqlStatus+")";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public int getSendableTotalRow(String cifKey, String accountNumber, String periode, int status) {
		String sql;
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = "AND STATUS = " + status;
		} else {
			sqlStatus = "AND STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' AND PERIODE = "+periode+" "
					+ sqlStatus+" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+")";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ sqlStatus+" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+")";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public int getWhitelistTotalRow(String cifKey, String accountNumber, String periode, int status) {
		String sql;
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = "AND B.STATUS = " + status;
		} else {
			sqlStatus = "AND B.STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") AND B.ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' AND B.PERIODE = "+periode+" "
					+ sqlStatus + " AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE
					+ " AND B.CIF_KEY IS NOT NULL)";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") AND B.ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ sqlStatus+" AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL)";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}

	public SendEmail getById(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL WHERE ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(SendEmail.class, rows.get(0));
	}

	public List<SendEmailResponse> getEmails(String cifKey, String accountNumber, String periode, int status, int page, int totalPerPage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		int batasBawah = 1 + totalPerPage * (page - 1);
		int batasAtas = totalPerPage * page;
		
		String sql;
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND STATUS = " + status;
		} else {
			sqlStatus = " AND STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ "AND PERIODE = " + periode + sqlStatus + " "
					+ "ORDER BY ID DESC) Q) WHERE R >= "+batasBawah+" AND R <= " + batasAtas;
		} else {
			sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'"
					+ sqlStatus +" "
					+ "ORDER BY ID DESC) Q) WHERE R >= "+batasBawah+" AND R <= " + batasAtas;
		}
		
		ArrayList<SendEmailResponse> res = new ArrayList<SendEmailResponse>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			SendEmailResponse se = serialize(SendEmailResponse.class, row);
			res.add(se);
		}
				
		return res;
	}
	
	public List<SendEmail> getEmails(String cifKey, String accountNumber, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql;
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND STATUS = " + status;
		} else {
			sqlStatus = " AND STATUS != " + SendEmailStatus.NOT_SENT;
		}	
				
		if(!periode.isEmpty()){
			sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ "AND PERIODE = "+periode + sqlStatus +" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+" "
					+ "ORDER BY ID DESC";
		} else {
			sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'"
					+ sqlStatus + " AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+" "
					+ "ORDER BY ID DESC";
		}
		
		ArrayList<SendEmail> res = new ArrayList<SendEmail>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			SendEmail sysParam = serialize(SendEmail.class, row);
			res.add(sysParam);
		}
				
		return res;
	}
	
	public List<SendEmail> getWhitelistEmails(String cifKey, String accountNumber, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql;
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND B.STATUS = " + status;
		} else {
			sqlStatus = " AND B.STATUS != " + SendEmailStatus.NOT_SENT;
		}	
				
		if(!periode.isEmpty()){
			sql = "SELECT * FROM (SELECT B.* FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") AND B.ACCOUNT_NUMBER LIKE '%"+accountNumber+"%' "
					+ "AND B.PERIODE = "+periode + sqlStatus +" AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL) "
					+ "ORDER BY ID DESC";
		} else {
			sql = "SELECT * FROM (SELECT B.* FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_SEND_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") AND B.ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'"
					+ sqlStatus + " AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL) "
					+ "ORDER BY ID DESC";
		}
		
		ArrayList<SendEmail> res = new ArrayList<SendEmail>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			SendEmail sysParam = serialize(SendEmail.class, row);
			res.add(sysParam);
		}
				
		return res;
	}
	
	private List<String> getEmail(String accountNumber){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_GET_EMAIL_ACC_NO")
				.declareParameters(
						new SqlOutParameter("PO_CURSOR", OracleTypes.CURSOR),
						new SqlParameter("P_ACC_NO", OracleTypes.NVARCHAR));
		
		Map<String, Object> mValues = caller.execute(accountNumber);
		ArrayList<String> res = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) mValues.get("PO_CURSOR");
		for (Map<String, Object> row : rows) {
			String email = (String)row.get("EMAIL");
//			if( email == null || email.length() == 0 )
//				email = "marthin.nainggolan@devteam.net";
			res.add(email);
		}
		return res;
	}
	
	private String getCCEmail(String accountNumber){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_GET_CC_EMAIL")
				.declareParameters(
						new SqlOutParameter("PO_EMAIL", OracleTypes.NVARCHAR),
						new SqlParameter("P_CC_NO", OracleTypes.NVARCHAR));
		
		Map<String, Object> mValues = caller.execute(accountNumber);
		if( mValues.size() > 0 )
			return (String)mValues.get("PO_EMAIL");
		return null;
	}
	
	public List<String> getEmail(String accountNumber, int emailType){
		List<String> email = getEmail(accountNumber);
		if( email.size() == 0 && emailType == EmailContentType.CC ){
			email.add(getCCEmail(accountNumber));
		}
		return email;		
	}
	
	public String getStatementPassword(String cifKey){
		String sql = "SELECT STATEMENT_PASSWORD FROM "+CommonCons.DB_SCHEMA+".TBL_CUSTOMER_INFO WHERE CIF_KEY = ?";
		
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(cifKey);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("STATEMENT_PASSWORD");
		}
		return null;
	}
	
	public void addToQueue(Long[] ids){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_SEND_EMAIL_QUEUE")
				.declareParameters(
						new SqlParameter("PA_IDS", OracleTypes.ARRAY, CommonCons.DB_SCHEMA + ".T_NUMBER"));
		
		SqlArrayValue<Long> data = new SqlArrayValue<Long>(ids);
		caller.execute(data);
	}
	
	public void addToProcessing(Long[] ids){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_SEND_EMAIL_PROCESSING")
				.declareParameters(
						new SqlParameter("PA_IDS", OracleTypes.ARRAY, CommonCons.DB_SCHEMA + ".T_NUMBER"));
		
		SqlArrayValue<Long> data = new SqlArrayValue<Long>(ids);
		caller.execute(data);
	}
	
	public List<SendEmail> getNextQueueEmail() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_GET_SEND_EMAIL_QUEUE")
				.declareParameters(
						new SqlOutParameter("PO_CURSOR", OracleTypes.CURSOR));
		
		Map<String, Object> mValues = caller.execute();
		
		ArrayList<SendEmail> res = new ArrayList<SendEmail>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) mValues.get("PO_CURSOR");
		for (Map<String, Object> row : rows) {
			SendEmail sendEmail = serialize(SendEmail.class, row); 
			res.add(sendEmail);
		}
		return res;
	}
	
	public String getWhitelistCifKey(String cifKey){
		String sql = "SELECT CIF_KEY FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST WHERE TO_NUMBER(CIF_KEY) = TO_NUMBER(" + cifKey + ")" ;
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("CIF_KEY");
		}
		return null;
	}
	
	public String getCCAttachment(String accountNumber){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.FE_DB_SCHEMA)
				.withProcedureName("SP_GET_CC_ATTACHMENT")
				.declareParameters(
						new SqlParameter("P_ACCOUNT_NUMBER", OracleTypes.NVARCHAR),
						new SqlOutParameter("PO_ATTACHMENT", OracleTypes.NVARCHAR));
		
		Map<String, Object> mValues = caller.execute(accountNumber);
		
		String attch = (String)mValues.get("PO_ATTACHMENT");
		if (attch!=null && !attch.isEmpty()) {
			return attch;
		}
		
		return null;
	}
}

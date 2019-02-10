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
import com.optima.nisp.constanta.SendEmailStatus;
import com.optima.nisp.model.KonsolidasiEmail;
import com.optima.nisp.response.KonsolidasiEmailResponse;

import oracle.jdbc.internal.OracleTypes;

@Component
public class KonsolidasiEmailDao extends ParentDao<KonsolidasiEmail> {

	public int getTotalRow(String cifKey, String periode, int status) {
		String sql;
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = "AND STATUS = " + status;
		} else {
			sqlStatus = "AND STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND PERIODE = "+periode+" "
					+ sqlStatus+")";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ sqlStatus+")";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public int getSendableTotalRow(String cifKey, String periode, int status) {
		String sql;
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = "AND STATUS = " + status;
		} else {
			sqlStatus = "AND STATUS != " + SendEmailStatus.NOT_SENT;
		}
				
		if(!periode.isEmpty()){
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") AND PERIODE = "+periode+" "
					+ sqlStatus+" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+")";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL WHERE "
					+ "(CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ sqlStatus+" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+")";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public int getWhitelistTotalRow(String cifKey, String periode, int status) {
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
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") AND B.PERIODE = "+periode+" "
					+ sqlStatus + " AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE
					+ " AND B.CIF_KEY IS NOT NULL)";
		} else {
			sql = "SELECT COUNT(*) TOTAL FROM "
					+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") "
					+ sqlStatus+" AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL)";
		}
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public KonsolidasiEmail getById(Long id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL WHERE ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(KonsolidasiEmail.class, rows.get(0));
	}
	
	public List<KonsolidasiEmailResponse> getEmails(String cifKey, String periode, int status, int page, int totalPerPage) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
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
			sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ "AND PERIODE = " + periode + sqlStatus + " "
					+ "ORDER BY ID DESC) Q) WHERE R >= "+batasBawah+" AND R <= " + batasAtas;
		} else {
			sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ sqlStatus +" "
					+ "ORDER BY ID DESC) Q) WHERE R >= "+batasBawah+" AND R <= " + batasAtas;
		}
		
		ArrayList<KonsolidasiEmailResponse> res = new ArrayList<KonsolidasiEmailResponse>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiEmailResponse se = serialize(KonsolidasiEmailResponse.class, row);
			res.add(se);
		}
				
		return res;
	}
	
	public List<KonsolidasiEmail> getEmails(String cifKey, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql;
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND STATUS = " + status;
		} else {
			sqlStatus = " AND STATUS != " + SendEmailStatus.NOT_SENT;
		}	
				
		if(!periode.isEmpty()){
			sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ "AND PERIODE = "+periode + sqlStatus +" AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+" "
					+ "ORDER BY ID DESC";
		} else {
			sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL "
					+ "WHERE (CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR CIF_KEY IS NULL")+") "
					+ sqlStatus + " AND STATUS!="+SendEmailStatus.PROCESSING+" AND STATUS!="+SendEmailStatus.QUEUE+" "
					+ "ORDER BY ID DESC";
		}
		
		ArrayList<KonsolidasiEmail> res = new ArrayList<KonsolidasiEmail>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiEmail sysParam = serialize(KonsolidasiEmail.class, row);
			res.add(sysParam);
		}
				
		return res;
	}
	
	public List<KonsolidasiEmail> getWhitelistEmails(String cifKey, String periode, int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql;
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND B.STATUS = " + status;
		} else {
			sqlStatus = " AND B.STATUS != " + SendEmailStatus.NOT_SENT;
		}	
				
		if(!periode.isEmpty()){
			sql = "SELECT * FROM (SELECT B.* FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") "
					+ "AND B.PERIODE = "+periode + sqlStatus +" AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL) "
					+ "ORDER BY ID DESC";
		} else {
			sql = "SELECT * FROM (SELECT B.* FROM "+CommonCons.DB_SCHEMA+".TBL_STATEMENT_WHITELIST A "
					+ "LEFT JOIN "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL B ON TO_NUMBER(A.CIF_KEY) = TO_NUMBER(B.CIF_KEY) "
					+ "WHERE (B.CIF_KEY LIKE '%"+cifKey+"%' "+(cifKey.length()>0?"":"OR B.CIF_KEY IS NULL")+") "
					+ sqlStatus + " AND B.STATUS!="+SendEmailStatus.PROCESSING+" AND B.STATUS!="+SendEmailStatus.QUEUE+" AND B.CIF_KEY IS NOT NULL) "
					+ "ORDER BY ID DESC";
		}
		
		ArrayList<KonsolidasiEmail> res = new ArrayList<KonsolidasiEmail>();
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiEmail sysParam = serialize(KonsolidasiEmail.class, row);
			res.add(sysParam);
		}
				
		return res;
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
				.withProcedureName("SP_KONDOLIDASI_EMAIL_QUEUE")
				.declareParameters(
						new SqlParameter("PA_IDS", OracleTypes.ARRAY, CommonCons.DB_SCHEMA + ".T_NUMBER"));
		
		SqlArrayValue<Long> data = new SqlArrayValue<Long>(ids);
		caller.execute(data);
	}
	
	// NO SP AVAILABLE
	public void addToProcessing(Long[] ids){
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_SEND_EMAIL_PROCESSING")
				.declareParameters(
						new SqlParameter("PA_IDS", OracleTypes.ARRAY, CommonCons.DB_SCHEMA + ".T_NUMBER"));
		
		SqlArrayValue<Long> data = new SqlArrayValue<Long>(ids);
		caller.execute(data);
	}
	
	public List<KonsolidasiEmail> getNextQueueEmail() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		SimpleJdbcCall caller = new SimpleJdbcCall(template)
				.withSchemaName(CommonCons.DB_SCHEMA)
				.withProcedureName("SP_GET_KONSOLIDASI_EMAIL_QUEUE")
				.declareParameters(
						new SqlOutParameter("PO_CURSOR", OracleTypes.CURSOR));
		
		Map<String, Object> mValues = caller.execute();
		
		ArrayList<KonsolidasiEmail> res = new ArrayList<KonsolidasiEmail>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) mValues.get("PO_CURSOR");
		for (Map<String, Object> row : rows) {
			KonsolidasiEmail sendEmail = serialize(KonsolidasiEmail.class, row); 
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
	
}

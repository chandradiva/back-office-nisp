package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.SendEmailStatus;
import com.optima.nisp.model.KonsolidasiEmailLog;

@Component
public class KonsolidasiEmailLogDao extends ParentDao<KonsolidasiEmailLog> {

	public List<KonsolidasiEmailLog> getList(String subject, String mailTo, String tglKirimFrom, String tglKirimTo, int status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{	
		subject = subject.toLowerCase().trim();
		mailTo = mailTo.toLowerCase().trim();
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND STATUS = " + status + " ";
		}
		
		String sqlSubject = "";
		if(subject!=null && !subject.isEmpty()){
			sqlSubject = " AND LOWER(SUBJECT) LIKE '%"+subject.toLowerCase().trim()+"%' ";
		}
		
		String sqlTglFrom = "";
		if(!tglKirimFrom.isEmpty()){
			sqlTglFrom = " AND TRUNC(TIME) >= TO_DATE('"+tglKirimFrom+"','DD/MM/YYYY') ";
		}
		
		String sqlTglTo = "";
		if(!tglKirimTo.isEmpty()){
			sqlTglFrom = " AND TRUNC(TIME) <= TO_DATE('"+tglKirimTo+"','DD/MM/YYYY') ";
		}
		
		String sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL_LOG "
				+ "WHERE LOWER(MAIL_TO) LIKE '%"+mailTo+"%' " + sqlStatus + sqlSubject + sqlTglFrom + sqlTglTo
				+ "ORDER BY TIME DESC) Q) WHERE R > "+start+" AND R <= "+(start+length);
		
		List<KonsolidasiEmailLog> res = new ArrayList<KonsolidasiEmailLog>();

		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			KonsolidasiEmailLog log = serialize(KonsolidasiEmailLog.class, row);
			res.add(log);
		}
		
		return res;
	}
	
	public Integer getTotalRow(String subject, String mailTo, String tglKirimFrom, String tglKirimTo, int status) {
		subject = subject.toLowerCase().trim();
		mailTo = mailTo.toLowerCase().trim();
		
		String sqlStatus = "";
		if(status != SendEmailStatus.ALL_STATUS){
			sqlStatus = " AND STATUS = " + status + " ";
		}
		
		String sqlSubject = "";
		if(subject!=null && !subject.isEmpty()){
			sqlSubject = " AND LOWER(SUBJECT) LIKE '%"+subject.toLowerCase().trim()+"%' ";
		}
		
		String sqlTglFrom = "";
		if(!tglKirimFrom.isEmpty()){
			sqlTglFrom = " AND TRUNC(TIME) >= TO_DATE('"+tglKirimFrom+"','DD/MM/YYYY') ";
		}
		
		String sqlTglTo = "";
		if(!tglKirimTo.isEmpty()){
			sqlTglFrom = " AND TRUNC(TIME) <= TO_DATE('"+tglKirimTo+"','DD/MM/YYYY') ";
		}

		String sql = "SELECT COUNT(*) TOTAL FROM ( SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_KONSOLIDASI_EMAIL_LOG "
				+ "WHERE LOWER(MAIL_TO) LIKE '%"+mailTo+"%' " + sqlStatus + sqlSubject + sqlTglFrom + sqlTglTo
				+ "ORDER BY TIME DESC)";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
}

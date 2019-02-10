package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.CCEmailAttachment;

@Component
public class CCEmailAttachmentDao extends ParentDao<CCEmailAttachment>{

	public CCEmailAttachment getByBin(String bin) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<CCEmailAttachment> res = new ArrayList<CCEmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT WHERE BIN = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(bin);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			CCEmailAttachment emailAttachment = serialize(CCEmailAttachment.class, row);
			res.add(emailAttachment);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public List<CCEmailAttachment> getAll() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<CCEmailAttachment> res = new ArrayList<CCEmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			CCEmailAttachment ccAttachment = serialize(CCEmailAttachment.class, row);
			res.add(ccAttachment);
		}
		return res;
	}
	
	public int getTotalCode(){
		String sql = "SELECT COUNT(*) TOTAL FROM (SELECT DISTINCT ATTCH_CODE FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT)";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<CCEmailAttachment> getAllByCode() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<CCEmailAttachment> res = new ArrayList<CCEmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT "
					+ "WHERE ROWID IN(SELECT MAX(ROWID) FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT GROUP BY ATTCH_CODE) "
					+ "ORDER BY PRODUCT_NAME";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			CCEmailAttachment ccAttachment = serialize(CCEmailAttachment.class, row);
			res.add(ccAttachment);
		}
		return res;
	}
	
	public List<String> getAnotherBins(String bin, String attchCode){
		List<String> bins = new ArrayList<String>();
		String sql = "SELECT BIN FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT WHERE BIN!=? AND ATTCH_CODE=?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(bin);
		args.add(attchCode);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		
		for (Map<String, Object> row : rows) {
			String oBin = (String) row.get("BIN");
			bins.add(oBin);
		}
		
		return bins;
	}
	
	public String getBinByCode(String attchCode){
		String sql = "SELECT BIN FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT WHERE ATTCH_CODE=?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(attchCode);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		
		if(rows.size()>0){
			return (String) rows.get(0).get("BIN");
		} else {
			return null;
		}
	}
	
	public List<CCEmailAttachment> getAttchsSameCode(String attchCode) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<CCEmailAttachment> res = new ArrayList<CCEmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT WHERE ATTCH_CODE=?";
		List<Object> args = new ArrayList<Object>();
		args.add(attchCode);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			CCEmailAttachment ccAttachment = serialize(CCEmailAttachment.class, row);
			res.add(ccAttachment);
		}
		return res;
	}
}

package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.AuditTrailWSView;

@Component
public class AuditTrailWSViewDao extends ParentDao<AuditTrailWSView> {

	public Integer getTotalRow(String username, String atDateFrom, String atDateTo, String urlPath, String reqParam, String information, String cifKey, String activity) {
		String atDate = "";
		if(!atDateFrom.isEmpty())
			atDate += "TRUNC(TIME) >= TO_DATE('"+atDateFrom+"','dd/MM/yyyy')";
		if(!atDateFrom.isEmpty() && !atDateTo.isEmpty())
			atDate += " AND ";
		if(!atDateTo.isEmpty())
			atDate+= "TRUNC(TIME) <= TO_DATE('"+atDateTo+"','dd/MM/yyyy')";
		
		username = username.toLowerCase().trim();
		urlPath = urlPath.toLowerCase().trim();
		reqParam = reqParam.toLowerCase().trim();
		information = information.toLowerCase().trim();
		activity = activity.toLowerCase().trim();
		
		String requestParam = "";
		if(!reqParam.isEmpty()){
			requestParam =  " AND LOWER(REQ_PARAM) LIKE '%"+reqParam+"%' ";
		}
		
		String sql = "SELECT COUNT(*) TOTAL FROM "
				+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".VIEW_WS_AUDIT_TRAIL "
				+ "WHERE LOWER(INFORMATION) LIKE '%"+information+"%' AND LOWER(USERNAME) LIKE '%"+username+"%' "
				+ "AND LOWER(URL_PATH) LIKE '%"+urlPath+"%' AND CIF_KEY LIKE '%"+cifKey+"%' AND LOWER(ACTIVITY) LIKE '%"+activity+"%'";
		
				if(!atDate.isEmpty()){
					sql += " AND " + atDate;
				}
					
				sql += requestParam + ")";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<AuditTrailWSView> getList(int start, int length, String username, String atDateFrom, String atDateTo, String urlPath, String reqParam, String information, String cifKey, String activity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String atDate = "";
		if(!atDateFrom.isEmpty())
			atDate += "TRUNC(TIME) >= TO_DATE('"+atDateFrom+"','dd/MM/yyyy')";
		if(!atDateFrom.isEmpty() && !atDateTo.isEmpty())
			atDate += " AND ";
		if(!atDateTo.isEmpty())
			atDate+= "TRUNC(TIME) <= TO_DATE('"+atDateTo+"','dd/MM/yyyy')";
		
		username = username.toLowerCase().trim();
		urlPath = urlPath.toLowerCase().trim();
		reqParam = reqParam.toLowerCase().trim();
		information = information.toLowerCase().trim();
		activity = activity.toLowerCase().trim();
		
		String requestParam = "";
		if(!reqParam.isEmpty()){
			requestParam =  " AND LOWER(REQ_PARAM) LIKE '%"+reqParam+"%' ";
		}
		
		String sql = "SELECT * FROM (SELECT AT.*, ROWNUM R FROM "+CommonCons.DB_SCHEMA+".VIEW_WS_AUDIT_TRAIL AT "
				+ "WHERE LOWER(INFORMATION) LIKE '%"+information+"%' AND LOWER(USERNAME) LIKE '%"+username+"%' "
				+ "AND LOWER(URL_PATH) LIKE '%"+urlPath+"%' AND CIF_KEY LIKE '%"+cifKey+"%' AND LOWER(ACTIVITY) LIKE '%"+activity+"%'";
				
				if(!atDate.isEmpty()){
					sql += " AND " + atDate;
				}
				sql += requestParam + ") WHERE R > ? AND R <= ?";
		
		List<AuditTrailWSView> res = new ArrayList<AuditTrailWSView>();
		List<Object> args = new ArrayList<Object>();
		args.add(start);
		args.add(start+length);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			AuditTrailWSView atView = serialize(AuditTrailWSView.class, row);
			res.add(atView);
		}
		return res;
	}
}

package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.AuditTrailBOView;

@Component
public class AuditTrailBOViewDao extends ParentDao<AuditTrailBOView> {
	
	public Integer getTotalRow(String username, String groups, String atDateFrom, String atDateTo, String action, String information) {
		String atDate = "";
		if(!atDateFrom.isEmpty())
			atDate += "TRUNC(TIME) >= TO_DATE('"+atDateFrom+"','DD/MM/YYYY')";
		if(!atDateFrom.isEmpty() && !atDateTo.isEmpty())
			atDate += " AND ";
		if(!atDateTo.isEmpty())
			atDate+= "TRUNC(TIME) <= TO_DATE('"+atDateTo+"','DD/MM/YYYY')";
		
		username = username.toLowerCase().trim();
		action = action.toLowerCase().trim();
		information = information.toLowerCase().trim();
		groups = groups.toLowerCase().trim();
		
		String act = "";
		if(!action.isEmpty()){
			act =  " AND (LOWER(BUTTON_ACTIVITY) LIKE '%"+action+"%' OR LOWER(BUTTON_ACTIVITY) LIKE '%"+action
					+"%' OR (LOWER(URL_PATH) LIKE '%"+action+"%' AND URL_PATH IN ('"+CommonCons.LOGIN_URL+"','"
					+CommonCons.LOGOUT_URL+"','"+CommonCons.RIWAYAT_TRANSAKSI_TAMPIL_URL+"','"
					+CommonCons.RIWAYAT_TRANSAKSI_DOWNLOAD_URL+"'))) ";
		}
		
		String sql = "SELECT COUNT(*) TOTAL FROM "
				+ "(SELECT * FROM "+CommonCons.DB_SCHEMA+".VIEW_BO_AUDIT_TRAIL "
				+ "WHERE LOWER(USERNAME) LIKE '%"+username+"%' "
				+ "AND LOWER(GROUPS) LIKE '%"+groups+"%' "
				+ "AND LOWER(INFORMATION) LIKE '%"+information+"%'";
		
				if(!atDate.isEmpty()){
					sql += " AND " + atDate;
				}		
				sql += act + ")";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<AuditTrailBOView> getList(int start, int length, String username, String groups, String atDateFrom, String atDateTo, String action, String information) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String atDate = "";
		if(!atDateFrom.isEmpty())
			atDate += "TRUNC(TIME) >= TO_DATE('"+atDateFrom+"','DD/MM/YYYY')";
		if(!atDateFrom.isEmpty() && !atDateTo.isEmpty())
			atDate += " AND ";
		if(!atDateTo.isEmpty())
			atDate+= "TRUNC(TIME) <= TO_DATE('"+atDateTo+"','DD/MM/YYYY')";
		
		username = username.toLowerCase().trim();
		action = action.toLowerCase().trim();
		information = information.toLowerCase().trim();
		groups = groups.toLowerCase().trim();
		
		String act = "";
		if(!action.isEmpty()){
			act =  " AND (LOWER(BUTTON_ACTIVITY) LIKE '%"+action+"%' OR LOWER(BUTTON_ACTIVITY) LIKE '%"+action
					+"%' OR (LOWER(URL_PATH) LIKE '%"+action+"%' AND URL_PATH IN ('"+CommonCons.LOGIN_URL+"','"
					+CommonCons.LOGOUT_URL+"','"+CommonCons.RIWAYAT_TRANSAKSI_TAMPIL_URL+"','"
					+CommonCons.RIWAYAT_TRANSAKSI_DOWNLOAD_URL+"'))) ";
		}
		
		String sql = "SELECT * FROM (SELECT AT.*, ROWNUM R FROM "+CommonCons.DB_SCHEMA+".VIEW_BO_AUDIT_TRAIL AT "
				+ "WHERE LOWER(USERNAME) LIKE '%"+username+"%' "
				+ "AND LOWER(GROUPS) LIKE '%"+groups+"%' "
				+ "AND LOWER(INFORMATION) LIKE '%"+information+"%'";
		
				if(!atDate.isEmpty()){
					sql += " AND " + atDate;
				}		
				sql += act + ") WHERE R > ? AND R <= ?";
		
		List<AuditTrailBOView> res = new ArrayList<AuditTrailBOView>();
		List<Object> args = new ArrayList<Object>();
		args.add(start);
		args.add(start+length);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			AuditTrailBOView atView = serialize(AuditTrailBOView.class, row);
			res.add(atView);
		}
		return res;
	}
}

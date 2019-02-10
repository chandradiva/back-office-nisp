package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.model.RiwayatTransaksiParameterTemp;

@Component
public class RiwayatTransaksiParameterTempDao extends ParentDao<RiwayatTransaksiParameterTemp> {

	public void delete(Integer reviewedStatus){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS = ?";
		template.update(sql, reviewedStatus);
	}
	
	public void deleteReviewed(){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS != ? AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, NotifStatus.HIDE);
	}
	
	public List<RiwayatTransaksiParameterTemp> getNotReviewed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<RiwayatTransaksiParameterTemp> res = new ArrayList<RiwayatTransaksiParameterTemp>();
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS = 0";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			RiwayatTransaksiParameterTemp paramTemp = serialize(RiwayatTransaksiParameterTemp.class, row);
			res.add(paramTemp);
		}
		
		return res;
	}
	
	public List<RiwayatTransaksiParameterTemp> getByRequester(String requesterId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<RiwayatTransaksiParameterTemp> res = new ArrayList<RiwayatTransaksiParameterTemp>();
		requesterId = requesterId.toLowerCase().trim();
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(notifStatus);
		
		String sql = "SELECT * FROM " + CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ? AND SHOW_NOTIF_STATUS = ?";
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			RiwayatTransaksiParameterTemp paramTemp = serialize(RiwayatTransaksiParameterTemp.class, row);
			res.add(paramTemp);
		}
		return res;
	}
	
	public RiwayatTransaksiParameterTemp getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE KEY = ? AND REVIEWED_STATUS = 0";
		List<Object> args = new ArrayList<Object>();
		args.add(key);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(RiwayatTransaksiParameterTemp.class, rows.get(0));		
	}
	
	public Integer getTotalNotReviewedRow(){
		String sql = "SELECT COUNT(*) TOTAL_ROW FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS = 0";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL_ROW");
			return n.intValue();
		}
		
		return 0;
	}
	
	public String getUpdatedBy(){
		String sql = "SELECT UPDATED_BY FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_RIWAYAT_TRANS_PARAM_TMP WHERE REVIEWED_STATUS = 0";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("UPDATED_BY");
		}
		return null;
	}
}

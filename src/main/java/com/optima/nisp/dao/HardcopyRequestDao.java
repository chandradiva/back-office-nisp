package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.HardcopyRequestStatus;
import com.optima.nisp.model.HardcopyRequest;

@Component
public class HardcopyRequestDao extends ParentDao<HardcopyRequest> {

	public Integer getRequestTotalRow(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber) {		
		String reqDate = "";
		if(!reqDateFrom.isEmpty())
			reqDate += "TRUNC(REQUEST_DATE) >= TO_DATE('"+reqDateFrom+"','dd/MM/yyyy')";
		if(!reqDateFrom.isEmpty() && !reqDateTo.isEmpty())
			reqDate += " AND ";
		if(!reqDateTo.isEmpty())
			reqDate+= "TRUNC(REQUEST_DATE) <= TO_DATE('"+reqDateTo+"','dd/MM/yyyy')";
		
		String periode = "";
		if(!reqDate.isEmpty() && (!periodeFrom.isEmpty() || !periodeTo.isEmpty()))
			periode += " AND ";
		if(!periodeFrom.isEmpty())
			periode += " PERIODE_NUMBER >= " + periodeFrom;
		if(!periodeFrom.isEmpty() && !periodeTo.isEmpty())
			periode += " AND ";
		if(!periodeTo.isEmpty())
			periode += " PERIODE_NUMBER <= " + periodeTo;
		
		String sql = "SELECT COUNT(*) TOTAL FROM (SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY "
				+ "WHERE STATUS = "+HardcopyRequestStatus.PENDING+" AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'";
		if(!reqDate.isEmpty() || !periode.isEmpty()){
			sql += " AND " + reqDate + periode;
		}
		sql += " ORDER BY REQUEST_DATE DESC)";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<HardcopyRequest> getList(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String reqDate = "";
		if(!reqDateFrom.isEmpty())
			reqDate += "TRUNC(REQUEST_DATE) >= TO_DATE('"+reqDateFrom+"','dd/MM/yyyy')";
		if(!reqDateFrom.isEmpty() && !reqDateTo.isEmpty())
			reqDate += " AND ";
		if(!reqDateTo.isEmpty())
			reqDate+= "TRUNC(REQUEST_DATE) <= TO_DATE('"+reqDateTo+"','dd/MM/yyyy')";
		
		String periode = "";
		if(!reqDate.isEmpty() && (!periodeFrom.isEmpty() || !periodeTo.isEmpty()))
			periode += " AND ";
		if(!periodeFrom.isEmpty())
			periode += " PERIODE_NUMBER >= " + periodeFrom;
		if(!periodeFrom.isEmpty() && !periodeTo.isEmpty())
			periode += " AND ";
		if(!periodeTo.isEmpty())
			periode += " PERIODE_NUMBER <= " + periodeTo;
		
		String sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM (SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY "
				+ "WHERE STATUS = "+HardcopyRequestStatus.PENDING+" AND ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'";
		if(!reqDate.isEmpty() || !periode.isEmpty()){
			sql += " AND " + reqDate + periode;
		}
		sql += " ORDER BY REQUEST_DATE DESC)";
		sql += " Q) WHERE R > "+start+" AND R <= "+(start+length);
		
		List<HardcopyRequest> res = new ArrayList<HardcopyRequest>();

		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			HardcopyRequest hcReq = serialize(HardcopyRequest.class, row);
			res.add(hcReq);
		}
		return res;
	}
	
	public Integer getHistoryTotalRow(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, String batchId, Integer status) {		
		String reqDate = "";
		if(!reqDateFrom.isEmpty())
			reqDate += "TRUNC(REQUEST_DATE) >= TO_DATE('"+reqDateFrom+"','dd/MM/yyyy')";
		if(!reqDateFrom.isEmpty() && !reqDateTo.isEmpty())
			reqDate += " AND ";
		if(!reqDateTo.isEmpty())
			reqDate+= "TRUNC(REQUEST_DATE) <= TO_DATE('"+reqDateTo+"','dd/MM/yyyy')";
		
		String periode = "";
		if(!reqDate.isEmpty() && (!periodeFrom.isEmpty() || !periodeTo.isEmpty()))
			periode += " AND ";
		if(!periodeFrom.isEmpty())
			periode += " PERIODE_NUMBER >= " + periodeFrom;
		if(!periodeFrom.isEmpty() && !periodeTo.isEmpty())
			periode += " AND ";
		if(!periodeTo.isEmpty())
			periode += " PERIODE_NUMBER <= " + periodeTo;
		
		String stat = "";
		if(status != HardcopyRequestStatus.ALL_STATUS){			
			stat += " AND STATUS = " + status;
		}
		
		String batch = "";
		if(batchId!=null && !batchId.isEmpty()){
			batch += " AND BATCH_ID = TO_NUMBER('" + batchId +"')";
		}
		
		String sql = "SELECT COUNT(*) TOTAL FROM (SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY "
				+ "WHERE ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'";
		if(!reqDate.isEmpty() || !periode.isEmpty()){
			sql += " AND " + reqDate + periode;
		}
		sql += stat + batch + " ORDER BY REQUEST_DATE DESC)";
		
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			BigDecimal n = (BigDecimal) rows.get(0).get("TOTAL");
			return n.intValue();
		}
		
		return 0;
	}
	
	public List<HardcopyRequest> getHistoryList(String reqDateFrom, String reqDateTo, String periodeFrom, String periodeTo, String accountNumber, String batchId, Integer status, int start, int length) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String reqDate = "";
		if(!reqDateFrom.isEmpty())
			reqDate += "TRUNC(REQUEST_DATE) >= TO_DATE('"+reqDateFrom+"','dd/MM/yyyy')";
		if(!reqDateFrom.isEmpty() && !reqDateTo.isEmpty())
			reqDate += " AND ";
		if(!reqDateTo.isEmpty())
			reqDate+= "TRUNC(REQUEST_DATE) <= TO_DATE('"+reqDateTo+"','dd/MM/yyyy')";
		
		String periode = "";
		if(!reqDate.isEmpty() && (!periodeFrom.isEmpty() || !periodeTo.isEmpty()))
			periode += " AND ";
		if(!periodeFrom.isEmpty())
			periode += " PERIODE_NUMBER >= " + periodeFrom;
		if(!periodeFrom.isEmpty() && !periodeTo.isEmpty())
			periode += " AND ";
		if(!periodeTo.isEmpty())
			periode += " PERIODE_NUMBER <= " + periodeTo;
		
		String stat = "";
		if(status != HardcopyRequestStatus.ALL_STATUS){			
			stat += " AND STATUS = " + status;
		} else {
			stat += " AND STATUS != " + HardcopyRequestStatus.PENDING;
		}
		
		String batch = "";
		if(batchId!=null && !batchId.isEmpty()){
			batch += " AND BATCH_ID = TO_NUMBER('" + batchId + "')";
		}
		
		String sql = "SELECT * FROM (SELECT Q.*, ROWNUM R FROM (SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY "
				+ "WHERE ACCOUNT_NUMBER LIKE '%"+accountNumber+"%'";
		if(!reqDate.isEmpty() || !periode.isEmpty()){
			sql += " AND " + reqDate + periode;
		}
		sql += stat + batch + " ORDER BY REQUEST_DATE DESC)";
		sql += " Q) WHERE R > "+start+" AND R <= "+(start+length);
		
		List<HardcopyRequest> res = new ArrayList<HardcopyRequest>();

		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			HardcopyRequest hcReq = serialize(HardcopyRequest.class, row);
			res.add(hcReq);
		}
		return res;
	}
	
	public HardcopyRequest getById(Long hcReqId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY WHERE ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(hcReqId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(HardcopyRequest.class, rows.get(0));	
	}
	
	public Long getBatchId(Long hcReqId) {		
		String sql = "SELECT BATCH_ID FROM "+CommonCons.DB_SCHEMA+".TBL_REQ_HARDCOPY "
				+ "WHERE ID = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(hcReqId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		
		if( rows.size() > 0 ){
			BigDecimal batchId = (BigDecimal) rows.get(0).get("BATCH_ID");
			return batchId.longValue();
		}
		
		return null;
	}
}

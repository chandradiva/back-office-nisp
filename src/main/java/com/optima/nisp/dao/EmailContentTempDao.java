package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.model.EmailContentTemp;

@Component
public class EmailContentTempDao extends ParentDao<EmailContentTemp> {

	public void delete(Integer reviewedStatus, Integer ecType){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT_TEMP WHERE REVIEWED_STATUS = ? AND TYPE = ?";
		template.update(sql, reviewedStatus, ecType);
	}
	
	public void deleteReviewed(Integer ecType){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT_TEMP WHERE REVIEWED_STATUS != ? AND TYPE = ? AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, ecType, NotifStatus.HIDE);
	}
	
	public EmailContentTemp getEmailContentTemp(Integer ecType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<EmailContentTemp> res = new ArrayList<EmailContentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT_TEMP WHERE REVIEWED_STATUS = 0 AND TYPE = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(ecType);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailContentTemp emailContent = serialize(EmailContentTemp.class, row);
			res.add(emailContent);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public EmailContentTemp getByRequester(String requesterId, Integer notifStatus, Integer ecType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<EmailContentTemp> res = new ArrayList<EmailContentTemp>();
		requesterId = requesterId.toLowerCase().trim();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT_TEMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ? AND SHOW_NOTIF_STATUS = ? AND TYPE = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(notifStatus);
		args.add(ecType);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailContentTemp emailContent = serialize(EmailContentTemp.class, row);
			res.add(emailContent);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public String getUpdatedBy(Integer ecType){
		String sql = "SELECT UPDATED_BY FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT_TEMP WHERE REVIEWED_STATUS = 0 AND TYPE = ?";
		
		List<Object> args = new ArrayList<Object>();
		args.add(ecType);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("UPDATED_BY");
		}
		return null;
	}
}

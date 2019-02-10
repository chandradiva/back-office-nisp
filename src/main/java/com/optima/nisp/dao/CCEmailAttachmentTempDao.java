package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.model.CCEmailAttachmentTemp;

@Component
public class CCEmailAttachmentTempDao extends ParentDao<CCEmailAttachmentTemp> {

	public CCEmailAttachmentTemp getByBin(String bin, Integer reviewedStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<CCEmailAttachmentTemp> res = new ArrayList<CCEmailAttachmentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE BIN = ? AND REVIEWED_STATUS = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(bin);
		args.add(reviewedStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			CCEmailAttachmentTemp emailAttachment = serialize(CCEmailAttachmentTemp.class, row);
			res.add(emailAttachment);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public List<CCEmailAttachmentTemp> getAll(Integer reviewedStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<CCEmailAttachmentTemp> res = new ArrayList<CCEmailAttachmentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(reviewedStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			CCEmailAttachmentTemp ccAttachment = serialize(CCEmailAttachmentTemp.class, row);
			res.add(ccAttachment);
		}
		return res;
	}
	
	public List<CCEmailAttachmentTemp> getAllByRequester(String requesterId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<CCEmailAttachmentTemp> res = new ArrayList<CCEmailAttachmentTemp>();
		requesterId = requesterId.toLowerCase().trim();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ?  AND SHOW_NOTIF_STATUS = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(notifStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			CCEmailAttachmentTemp temp = serialize(CCEmailAttachmentTemp.class, row);
			res.add(temp);
		}
		return res;
	}
	
	public String getUpdatedBy(){
		String sql = "SELECT UPDATED_BY FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = 0";
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("UPDATED_BY");
		}
		return null;
	}
	
	public void deleteByBin(Integer reviewedStatus, String bin){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND BIN = ?";
		template.update(sql, reviewedStatus, bin);
	}
	
	public void deleteAllReviewed(){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".TBL_CC_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, NotifStatus.HIDE);
	}
}

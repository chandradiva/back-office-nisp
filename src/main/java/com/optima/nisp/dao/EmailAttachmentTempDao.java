package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.constanta.NotifStatus;
import com.optima.nisp.constanta.ReviewStatus;
import com.optima.nisp.model.EmailAttachmentTemp;

@Component
public class EmailAttachmentTempDao extends ParentDao<EmailAttachmentTemp> {

	public EmailAttachmentTemp getByCategory(Integer categoryId, Integer reviewedStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE CATEGORY = ? AND REVIEWED_STATUS = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(categoryId);
		args.add(reviewedStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp emailAttachment = serialize(EmailAttachmentTemp.class, row);
			res.add(emailAttachment);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public void deleteAllCASA(Integer reviewedStatus){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+")";
		template.update(sql, reviewedStatus, CategoryCons.CREDIT_CARD);
	}
	
	public void deleteAllCASACompany(Integer reviewedStatus){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY_COMPANY+", "
				+CategoryCons.GIRO_NON_KRK_COMPANY+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
				+")";
		template.update(sql, reviewedStatus, CategoryCons.CREDIT_CARD);
	}
	
	public void deleteAllReviewedCASA(){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+") AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, NotifStatus.HIDE);
	}
	
	public void deleteAllReviewedCASACompany(){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY_COMPANY+", "
				+CategoryCons.GIRO_NON_KRK_COMPANY+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
				+") AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, NotifStatus.HIDE);
	}
	
	public void deleteByCategory(Integer reviewedStatus, Integer category){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND CATEGORY = ?";
		template.update(sql, reviewedStatus, category);
	}
	
	public void deleteAllReviewedCC(){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND CATEGORY = ? AND SHOW_NOTIF_STATUS = ?";
		template.update(sql, ReviewStatus.NOT_REVIEWED, CategoryCons.CREDIT_CARD, NotifStatus.HIDE);
	}
	
	public List<EmailAttachmentTemp> getAllCASA(Integer reviewStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+") ORDER BY CATEGORY";
		List<Object> args = new ArrayList<Object>();
		args.add(reviewStatus);		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp temp = serialize(EmailAttachmentTemp.class, row);
			res.add(temp);
		}
		return res;
	}
	
	public List<EmailAttachmentTemp> getAllCASACompany(Integer reviewStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY_COMPANY+", "
				+CategoryCons.GIRO_NON_KRK_COMPANY+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
				+") ORDER BY CATEGORY";
		List<Object> args = new ArrayList<Object>();
		args.add(reviewStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp temp = serialize(EmailAttachmentTemp.class, row);
			res.add(temp);
		}
		return res;
	}
	
	public String getUpdatedBy(){
		String sql = "SELECT UPDATED_BY FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = 0"
				+ " AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+")";
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("UPDATED_BY");
		}
		return null;
	}
	
	public String getCasaCompanyUpdatedBy(){
		String sql = "SELECT UPDATED_BY FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS = 0"
				+ " AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY_COMPANY+", "
				+CategoryCons.GIRO_NON_KRK_COMPANY+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
				+")";
		List<Map<String, Object>> rows = template.queryForList(sql);
		
		if( rows.size() > 0 ){
			return (String) rows.get(0).get("UPDATED_BY");
		}
		return null;
	}
	
	public List<EmailAttachmentTemp> getAllCASAByRequester(String requesterId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		requesterId = requesterId.toLowerCase().trim();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+") AND SHOW_NOTIF_STATUS = ? ORDER BY CATEGORY";
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(notifStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp temp = serialize(EmailAttachmentTemp.class, row);
			res.add(temp);
		}
		return res;
	}
	
	public List<EmailAttachmentTemp> getAllCASACompanyByRequester(String requesterId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		requesterId = requesterId.toLowerCase().trim();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ? AND CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY_COMPANY+", "
				+CategoryCons.GIRO_NON_KRK_COMPANY+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
				+") AND SHOW_NOTIF_STATUS = ? ORDER BY CATEGORY";
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(notifStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp temp = serialize(EmailAttachmentTemp.class, row);
			res.add(temp);
		}
		return res;
	}
	
	public EmailAttachmentTemp getCCByRequester(String requesterId, Integer notifStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachmentTemp> res = new ArrayList<EmailAttachmentTemp>();
		requesterId = requesterId.toLowerCase().trim();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT_TEMP WHERE REVIEWED_STATUS != ? AND LOWER(UPDATED_BY) = ? AND CATEGORY = ? AND SHOW_NOTIF_STATUS = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(ReviewStatus.NOT_REVIEWED);
		args.add(requesterId);
		args.add(CategoryCons.CREDIT_CARD);
		args.add(notifStatus);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachmentTemp emailAttachment = serialize(EmailAttachmentTemp.class, row);
			res.add(emailAttachment);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
}

package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CategoryCons;
import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.EmailAttachment;

@Component
public class EmailAttachmentDao extends ParentDao<EmailAttachment> {

	public EmailAttachment getByCategory(Integer categoryId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		ArrayList<EmailAttachment> res = new ArrayList<EmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT WHERE CATEGORY = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(categoryId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailAttachment emailAttachment = serialize(EmailAttachment.class, row);
			res.add(emailAttachment);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
	
	public List<EmailAttachment> getAllCASA() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<EmailAttachment> res = new ArrayList<EmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT WHERE CATEGORY IN ("
				+CategoryCons.MULTI_CURRENCY+", "
				+CategoryCons.GIRO_NON_KRK+", "
				+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN+", "
				+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN+", "
				+CategoryCons.GIRO_TABUNGAN_HARIAN+", "
				+CategoryCons.MULTI_CURRENCY_BILLINGUAL
				+") ORDER BY CATEGORY";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			EmailAttachment button = serialize(EmailAttachment.class, row);
			res.add(button);
		}
		return res;
	}
	
	public List<EmailAttachment> getAllCASACompany() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<EmailAttachment> res = new ArrayList<EmailAttachment>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_ATTACHMENT WHERE CATEGORY IN ("
		+CategoryCons.MULTI_CURRENCY_COMPANY+", "
		+CategoryCons.GIRO_NON_KRK_COMPANY+", "
		+CategoryCons.GIRO_KRK_TANPA_TUNGGAKAN_COMPANY+", "
		+CategoryCons.GIRO_KRK_DENGAN_TUNGGAKAN_COMPANY+", "
		+CategoryCons.GIRO_TABUNGAN_HARIAN_COMPANY+", "
		+CategoryCons.MULTI_CURRENCY_BILLINGUAL_COMPANY
		+") ORDER BY CATEGORY";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			EmailAttachment button = serialize(EmailAttachment.class, row);
			res.add(button);
		}
		return res;
	}
}

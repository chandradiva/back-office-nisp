package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.EmailContent;

@Component
public class EmailContentDao extends ParentDao<EmailContent> {

	public EmailContent getEmailContent(Integer ecType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<EmailContent> res = new ArrayList<EmailContent>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_EMAIL_CONTENT WHERE TYPE=?";
		List<Object> args = new ArrayList<Object>();
		args.add(ecType);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			EmailContent emailContent = serialize(EmailContent.class, row);
			res.add(emailContent);
		}
		if(res.size() == 0 )
			return null;
		
		return res.get(0);
	}
}

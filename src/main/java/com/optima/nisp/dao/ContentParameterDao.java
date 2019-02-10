package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.ContentParameter;

@Component
public class ContentParameterDao extends ParentDao<ContentParameter>{
	public List<ContentParameter> getAllContentParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<ContentParameter> res = new ArrayList<ContentParameter>();
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_CONTENT_PARAM ORDER BY KEY";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			ContentParameter contentParam = serialize(ContentParameter.class, row);
			res.add(contentParam);
		}
		return res;
	}

	public ContentParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_CONTENT_PARAM WHERE KEY = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(key);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(ContentParameter.class, rows.get(0));		
	}
}

package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.SystemParameter;

@Component
public class SystemParameterDao extends ParentDao<SystemParameter> {

	public List<SystemParameter> getAllSysParams() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<SystemParameter> res = new ArrayList<SystemParameter>();
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SYS_PARAM";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			SystemParameter sysParam = serialize(SystemParameter.class, row);
			res.add(sysParam);
		}
		return res;
	}

	public SystemParameter getByKey(String key) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.DB_SCHEMA+".BO_TBL_SYS_PARAM WHERE KEY = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(key);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(SystemParameter.class, rows.get(0));		
	}
}

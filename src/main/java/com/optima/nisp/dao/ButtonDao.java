package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.Button;

@Component
public class ButtonDao extends ParentDao<Button> {

	public Button getButton(Long buttonId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_BUTTON WHERE BUTTON_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(buttonId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Button.class, rows.get(0));	
	}

	public List<Button> getAllButtons() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<Button> res = new ArrayList<Button>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_BUTTON ORDER BY BUTTON_TITLE";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Button button = serialize(Button.class, row);
			res.add(button);
		}
		return res;
	}

	public void delete(Long buttonId){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_BUTTON WHERE BUTTON_ID = ?";
		template.update(sql, buttonId);
	}
	
	public Button getByButtonUrl(String buttonUrl) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_BUTTON WHERE BUTTON_URL = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(buttonUrl);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Button.class, rows.get(0));	
	}
}

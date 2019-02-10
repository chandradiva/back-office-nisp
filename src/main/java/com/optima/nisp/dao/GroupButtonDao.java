package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.GroupButton;

@Component
public class GroupButtonDao extends ParentDao<GroupButton> {

	public GroupButton getGroupButton(Long groupBtnId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {	
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON WHERE GROUP_BUTTON_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupBtnId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(GroupButton.class, rows.get(0));
	}

	public List<GroupButton> getAllGroupButtons() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		ArrayList<GroupButton> res = new ArrayList<GroupButton>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			GroupButton groupBtn = serialize(GroupButton.class, row);
			res.add(groupBtn);
		}
		return res;
	}

	public List<GroupButton> getByGroupId(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<GroupButton> res = new ArrayList<GroupButton>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON WHERE GROUP_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			GroupButton groupBtn = serialize(GroupButton.class, row);
			res.add(groupBtn);
		}
		return res;
	}

	public void deleteByButtonId(Long buttonId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON WHERE BUTTON_ID=?";
		template.update(sql, buttonId);
	}

	public void deleteByGroupId(Long groupId){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON WHERE GROUP_ID=?";
		template.update(sql, groupId);
	}
}

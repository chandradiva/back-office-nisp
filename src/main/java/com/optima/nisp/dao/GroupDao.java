package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.Group;

@Component
public class GroupDao extends ParentDao<Group> {

	public List<Group> getAllGroups() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<Group> res = new ArrayList<Group>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP ORDER BY GROUP_NAME";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Group group = serialize(Group.class, row);
			res.add(group);
		}
		return res;
	}

	public Group getById(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP WHERE GROUP_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Group.class, rows.get(0));	
	}
	
	public void deleteById(Long groupId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP WHERE GROUP_ID = ?";
		template.update(sql, groupId);
	}
	
	public List<Group> getGroupsByFGroup(String fGroupName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException{
		String sql = "SELECT A.GROUP_ID, A.GROUP_NAME FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP A "
				+ "LEFT JOIN "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_FGROUP B ON A.GROUP_ID=B.GROUP_ID "
				+ "LEFT JOIN "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP C ON B.FGROUP_ID=C.FGROUP_ID "
				+ "WHERE C.FGROUP_NAME=?";
		
		ArrayList<Group> res = new ArrayList<Group>();
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(fGroupName);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			Group group = serialize(Group.class, row);
			res.add(group);
		}
		return res;
	}
	
	public List<Group> getByButtonId(Long buttonId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT G.GROUP_ID, G.GROUP_NAME "
					+ "FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_BUTTON GB, "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP G "
					+ "WHERE GB.BUTTON_ID = ? AND GB.GROUP_ID = G.GROUP_ID";
		
		ArrayList<Group> res = new ArrayList<Group>();
		List<Object> args = new ArrayList<Object>();
		args.add(buttonId);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			Group group = serialize(Group.class, row);
			res.add(group);
		}
		return res;	
	}
	
	public List<Group> getByMenuId(Long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT GM.GROUP_ID, G.GROUP_NAME "
				+ "FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU GM, "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP G "
				+ "WHERE GM.GROUP_ID = G.GROUP_ID AND GM.MENU_ID = ?"
				+ "ORDER BY GM.GROUP_ID";
	
		ArrayList<Group> res = new ArrayList<Group>();
		List<Object> args = new ArrayList<Object>();
		args.add(menuId);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			Group group = serialize(Group.class, row);
			res.add(group);
		}
		return res;	
	}
}

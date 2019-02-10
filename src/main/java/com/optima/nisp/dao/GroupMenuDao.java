package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.GroupMenu;

@Component
public class GroupMenuDao extends ParentDao<GroupMenu> {

	public GroupMenu getGroupMenu(Long groupMenuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_MENU_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupMenuId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(GroupMenu.class, rows.get(0));
	}

	public List<GroupMenu> getAllGroupMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<GroupMenu> res = new ArrayList<GroupMenu>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			GroupMenu groupMenu = serialize(GroupMenu.class, row);
			res.add(groupMenu);
		}
		return res;
	}

	public List<GroupMenu> getByGroupId(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<GroupMenu> res = new ArrayList<GroupMenu>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			GroupMenu groupMenu = serialize(GroupMenu.class, row);
			res.add(groupMenu);
		}
		return res;
	}

	public void deleteByMenuId(Long menuId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE MENU_ID = ?";
		
		template.update(sql, menuId);
	}

	public List<GroupMenu> getRootGroupMenus(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<GroupMenu> res = new ArrayList<GroupMenu>();
		String sql = "SELECT GROUP_MENU_ID, MENU_ID, GROUP_ID FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_ID = ? " 
				 + "MINUS SELECT GM.GROUP_MENU_ID, GM.MENU_ID, GM.GROUP_ID "
				 + "FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU GM, "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION MR WHERE GM.MENU_ID = MR.CHILD_MENU_ID";
		
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			GroupMenu groupMenu = serialize(GroupMenu.class, row);
			res.add(groupMenu);
		}
		return res;
	}

	public void deleteByGroupId(Long groupId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_ID = ?";
		template.update(sql, groupId);
	}
	
	public void delete(Long groupId, Long menuId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_ID = ? AND MENU_ID = ?";
		
		template.update(sql, groupId, menuId);
	}

	public GroupMenu getGroupMenu(Long groupId, Long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_MENU WHERE GROUP_ID = ? AND MENU_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		args.add(menuId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(GroupMenu.class, rows.get(0));
		
	}
}

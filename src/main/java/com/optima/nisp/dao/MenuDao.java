package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.Menu;

@Component
public class MenuDao extends ParentDao<Menu> {

	public Menu getMenu(Long menuId, Integer activeStatus) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE MENU_ID = ? AND ACTIVE_STATUS=?";
		List<Object> args = new ArrayList<Object>();
		args.add(menuId);
		args.add(activeStatus);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Menu.class, rows.get(0));
	}
	
	public Menu getMenu(Long menuId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE MENU_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(menuId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Menu.class, rows.get(0));
	}

	public List<Menu> getAllMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		ArrayList<Menu> res = new ArrayList<Menu>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE ACTIVE_STATUS=1 AND TITLE NOT IN (?, ?, ?, ?) ORDER BY TITLE";
		List<Object> args = new ArrayList<Object>();
		args.add(CommonCons.MANAGE_GROUP);
		args.add(CommonCons.MANAGE_FGROUP);
		args.add(CommonCons.ADD_GROUP);
		args.add(CommonCons.EDIT_GROUP);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			Menu menu = serialize(Menu.class, row);
			res.add(menu);
		}
		return res;
	}

	public List<Menu> getRootMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		ArrayList<Menu> res = new ArrayList<Menu>();
		String sql = "SELECT MENU_ID, TITLE, SUBTITLE, LINK, FLAG, ACTIVE_STATUS FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE FLAG = 1 AND ACTIVE_STATUS=1"
				+ "MINUS SELECT M.MENU_ID, M.TITLE, M.SUBTITLE, M.LINK, M.FLAG, M.ACTIVE_STATUS FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION MR, "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU M "
				+ "WHERE MR.CHILD_MENU_ID = M.MENU_ID";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Menu menu = serialize(Menu.class, row);
			res.add(menu);
		}
		return res;
	}

	public List<Menu> getAllRootMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<Menu> res = new ArrayList<Menu>();
		String sql = "SELECT MENU_ID, TITLE, SUBTITLE, LINK, FLAG, ACTIVE_STATUS FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE ACTIVE_STATUS=1 "
				+ "MINUS SELECT M.MENU_ID, M.TITLE, M.SUBTITLE, M.LINK, M.FLAG, M.ACTIVE_STATUS FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION MR, "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU M "
				+ "WHERE MR.CHILD_MENU_ID = M.MENU_ID";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Menu menu = serialize(Menu.class, row);
			res.add(menu);
		}
		return res;
	}

	public List<Menu> getAllChildMenus() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<Menu> res = new ArrayList<Menu>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE FLAG=0 AND ACTIVE_STATUS=1";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			Menu menu = serialize(Menu.class, row);
			res.add(menu);
		}
		return res;
	}

	public Menu getByLink(String link) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU WHERE LINK = ? AND ACTIVE_STATUS=1";
		List<Object> args = new ArrayList<Object>();
		args.add(link);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(Menu.class, rows.get(0));
	}
}

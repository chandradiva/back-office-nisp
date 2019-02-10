package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.MenuRelation;

@Component
public class MenuRelationDao extends ParentDao<MenuRelation>{

	public List<MenuRelation> getMenuRelations(Long parentId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		ArrayList<MenuRelation> res = new ArrayList<MenuRelation>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION WHERE PARENT_MENU_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(parentId);
		
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			MenuRelation menuRelation = serialize(MenuRelation.class, row);
			res.add(menuRelation);
		}
		return res;
	}

	public void deleteByParent(Long parentId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION WHERE PARENT_MENU_ID = ?";
		
		template.update(sql, parentId);
	}

	public void deleteByChild(Long childId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION WHERE CHILD_MENU_ID = ?";
		
		template.update(sql, childId);
	}

	public MenuRelation getByChild(Long childId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_MENU_RELATION where CHILD_MENU_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(childId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(MenuRelation.class, rows.get(0));
	}
}

package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.FGroup;

@Component
public class FGroupDao extends ParentDao<FGroup> {

	public FGroup getFGroup(Long fgroupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP WHERE FGROUP_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(fgroupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(FGroup.class, rows.get(0));
	}
	
	public FGroup getByName(String fgroupName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP WHERE FGROUP_NAME = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(fgroupName);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(FGroup.class, rows.get(0));
	}

	public List<FGroup> getAllFGroups() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<FGroup> res = new ArrayList<FGroup>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP ORDER BY FGROUP_NAME";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			FGroup fgroup = serialize(FGroup.class, row);
			res.add(fgroup);
		}
		return res;
	}
	
	public List<FGroup> getAllFGroupsNoSecurity() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<FGroup> res = new ArrayList<FGroup>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP WHERE FGROUP_NAME != ? ORDER BY FGROUP_NAME";
		List<Object> args = new ArrayList<Object>();
		args.add(CommonCons.SECURITY_FGROUP);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			FGroup fgroup = serialize(FGroup.class, row);
			res.add(fgroup);
		}
		return res;
	}
	
	public List<FGroup> getByGroupId(Long groupId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<FGroup> res = new ArrayList<FGroup>();
		String sql = "SELECT F.FGROUP_ID, F.FGROUP_NAME FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP F, "
				+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_FGROUP GF "
					+ "WHERE GF.GROUP_ID = ? AND F.FGROUP_ID = GF.FGROUP_ID ORDER BY F.FGROUP_NAME";
		List<Object> args = new ArrayList<Object>();
		args.add(groupId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		for (Map<String, Object> row : rows) {
			FGroup fgroup = serialize(FGroup.class, row);
			res.add(fgroup);
		}
		return res;
	}

	public void delete(Long fgroupId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_FGROUP WHERE FGROUP_ID = ?";
		template.update(sql, fgroupId);		
	}

}

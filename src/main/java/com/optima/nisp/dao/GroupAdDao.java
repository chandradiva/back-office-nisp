package com.optima.nisp.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.GroupAd;

@Component
public class GroupAdDao extends ParentDao<GroupAd> {

	public GroupAd getGroupAd(long groupAdId) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {		
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_AD WHERE GROUP_AD_ID = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(groupAdId);
		List<Map<String, Object>> rows = template.queryForList(sql, args.toArray());
		if( rows.size() == 0 )
			return null;
		return serialize(GroupAd.class, rows.get(0));
	}

	public List<GroupAd> getAllGroupAds() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		ArrayList<GroupAd> res = new ArrayList<GroupAd>();
		String sql = "SELECT * FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_AD";
		List<Map<String, Object>> rows = template.queryForList(sql);
		for (Map<String, Object> row : rows) {
			GroupAd groupAd = serialize(GroupAd.class, row);
			res.add(groupAd);
		}
		return res;
	}
	
	public void deleteByGroupId(Long groupId){
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_AD WHERE GROUP_ID=?";
		template.update(sql, groupId);
	}
}

package com.optima.nisp.dao;

import org.springframework.stereotype.Component;

import com.optima.nisp.constanta.CommonCons;
import com.optima.nisp.model.GroupFGroup;

@Component
public class GroupFGroupDao extends ParentDao<GroupFGroup> {

	public void deleteByGroupId(Long groupId) {
		String sql = "DELETE FROM "+CommonCons.FE_DB_SCHEMA+".BO_TBL_GROUP_FGROUP WHERE GROUP_ID = ?";
		
		template.update(sql, groupId);
	}
}

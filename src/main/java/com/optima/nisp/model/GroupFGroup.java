package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_GROUP_FGROUP", dbSchema=CommonCons.FE_DB_SCHEMA)
public class GroupFGroup implements Serializable {

	private static final long serialVersionUID = 2570735904327758913L;
	
	@NISPEntitySerialize(field="GROUP_FGROUP_ID")
	@NISPEntity(columnName="GROUP_FGROUP_ID")
	@NISPEntityId
	private Long groupFgroupId;
	
	@NISPEntitySerialize(field="GROUP_ID")
	@NISPEntity(columnName="GROUP_ID")
	private Long groupId;
	
	@NISPEntitySerialize(field="FGROUP_ID")
	@NISPEntity(columnName="FGROUP_ID")
	private Long fgroupId;
	
	public Long getGroupFgroupId() {
		return groupFgroupId;
	}
	public void setGroupFgroupId(Long groupFgroupId) {
		this.groupFgroupId = groupFgroupId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getFgroupId() {
		return fgroupId;
	}
	public void setFgroupId(Long fgroupId) {
		this.fgroupId = fgroupId;
	}
}

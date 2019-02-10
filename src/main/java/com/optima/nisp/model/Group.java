package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_GROUP", dbSchema=CommonCons.FE_DB_SCHEMA)
public class Group implements Serializable {

	private static final long serialVersionUID = 9129144329481459163L;

	@NISPEntitySerialize(field="GROUP_ID")
	@NISPEntity(columnName="GROUP_ID")
	@NISPEntityId
	private Long groupId;
	
	@NISPEntitySerialize(field="GROUP_NAME")
	@NISPEntity(columnName="GROUP_NAME")
	private String groupName;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}

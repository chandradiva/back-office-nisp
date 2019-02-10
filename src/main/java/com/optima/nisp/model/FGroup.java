package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_FGROUP", dbSchema=CommonCons.FE_DB_SCHEMA)
public class FGroup implements Serializable {

	private static final long serialVersionUID = 5976234044974658482L;

	@NISPEntitySerialize(field="FGROUP_ID")
	@NISPEntity(columnName="FGROUP_ID")
	@NISPEntityId
	private Long fgroupId;
	
	@NISPEntitySerialize(field="FGROUP_NAME")
	@NISPEntity(columnName="FGROUP_NAME")
	private String fgroupName;
	
	public Long getFgroupId() {
		return fgroupId;
	}
	public void setFgroupId(Long fgroupId) {
		this.fgroupId = fgroupId;
	}
	public String getFgroupName() {
		return fgroupName;
	}
	public void setFgroupName(String fgroupName) {
		this.fgroupName = fgroupName;
	}
}

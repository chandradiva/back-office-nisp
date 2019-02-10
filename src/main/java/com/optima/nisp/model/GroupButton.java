package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_GROUP_BUTTON", dbSchema=CommonCons.FE_DB_SCHEMA)
public class GroupButton implements Serializable {

	private static final long serialVersionUID = -7504354949712736937L;
	
	@NISPEntitySerialize(field="GROUP_BUTTON_ID")
	@NISPEntity(columnName="GROUP_BUTTON_ID")
	@NISPEntityId
	private Long groupButtonId;
	
	@NISPEntitySerialize(field="BUTTON_ID")
	@NISPEntity(columnName="BUTTON_ID")
	private Long buttonId;
	
	@NISPEntitySerialize(field="GROUP_ID")
	@NISPEntity(columnName="GROUP_ID")
	private Long groupId;
	
	public Long getGroupButtonId() {
		return groupButtonId;
	}

	public void setGroupButtonId(Long groupBtnId) {
		this.groupButtonId = groupBtnId;
	}

	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long btnId) {
		this.buttonId = btnId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
}

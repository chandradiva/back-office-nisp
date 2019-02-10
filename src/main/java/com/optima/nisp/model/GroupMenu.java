package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_GROUP_MENU", dbSchema=CommonCons.FE_DB_SCHEMA)
public class GroupMenu implements Serializable {

	private static final long serialVersionUID = -1205648839224044787L;

	@NISPEntitySerialize(field="GROUP_MENU_ID")
	@NISPEntity(columnName="GROUP_MENU_ID")
	@NISPEntityId
	private Long groupMenuId;
	
	@NISPEntitySerialize(field="GROUP_ID")
	@NISPEntity(columnName="GROUP_ID")
	private Long groupId;
	
	@NISPEntitySerialize(field="MENU_ID")
	@NISPEntity(columnName="MENU_ID")
	private Long menuId;

	public Long getGroupMenuId() {
		return groupMenuId;
	}

	public void setGroupMenuId(Long groupMenuId) {
		this.groupMenuId = groupMenuId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
}

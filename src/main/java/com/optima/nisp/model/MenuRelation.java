package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_MENU_RELATION", dbSchema=CommonCons.FE_DB_SCHEMA)
public class MenuRelation implements Serializable {

	private static final long serialVersionUID = -4036245691680353104L;
	
	@NISPEntitySerialize(field="MENU_RELATION_ID")
	@NISPEntity(columnName="MENU_RELATION_ID")
	@NISPEntityId
	private Long menuRelationId;
	
	@NISPEntitySerialize(field="PARENT_MENU_ID")
	@NISPEntity(columnName="PARENT_MENU_ID")
	private Long parentMenuId;
	
	@NISPEntitySerialize(field="CHILD_MENU_ID")
	@NISPEntity(columnName="CHILD_MENU_ID")
	private Long childMenuId;
	
	public Long getMenuRelationId() {
		return menuRelationId;
	}
	
	public void setMenuRelationId(Long menuRelationId) {
		this.menuRelationId = menuRelationId;
	}
	
	public Long getParentMenuId() {
		return parentMenuId;
	}
	
	public void setParentMenuId(Long parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	
	public Long getChildMenuId() {
		return childMenuId;
	}
	
	public void setChildMenuId(Long childMenuId) {
		this.childMenuId = childMenuId;
	}
}

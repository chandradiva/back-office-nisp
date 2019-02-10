package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_MENU", dbSchema=CommonCons.FE_DB_SCHEMA)
public class Menu implements Serializable {
	
	private static final long serialVersionUID = -5267935084313451883L;
	
	@NISPEntitySerialize(field="MENU_ID")
	@NISPEntity(columnName="MENU_ID")
	@NISPEntityId
	private Long menuId;
	
	@NISPEntitySerialize(field="TITLE")
	@NISPEntity(columnName="TITLE")
	private String title;
	
	@NISPEntitySerialize(field="SUBTITLE")
	@NISPEntity(columnName="SUBTITLE")
	private String subtitle;
	
	@NISPEntitySerialize(field="LINK")
	@NISPEntity(columnName="LINK")
	private String link;
	
	@NISPEntitySerialize(field="ACTION")
	@NISPEntity(columnName="ACTION")
	private String action;
	
	@NISPEntitySerialize(field="FLAG")
	@NISPEntity(columnName="FLAG")
	private Integer flag;
	
	@NISPEntitySerialize(field="ACTIVE_STATUS")
	@NISPEntity(columnName="ACTIVE_STATUS")
	private Integer activeStatus = 1;
	
	public Long getMenuId() {
		return menuId;
	}
	
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSubtitle() {
		return subtitle;
	}
	
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(Integer activeStatus) {
		this.activeStatus = activeStatus;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}

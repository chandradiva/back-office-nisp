package com.optima.nisp.response;

import java.util.List;

public class MenuResponse {
	private Long menuId;
	private String title;
	private String subtitle;
	private String link;
	private List<MenuResponse> subMenus;
	private Integer flag;
	private Integer checkedStatus = 0;
	
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

	public List<MenuResponse> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<MenuResponse> subMenus) {
		this.subMenus = subMenus;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Integer getCheckedStatus() {
		return checkedStatus;
	}

	public void setCheckedStatus(Integer checkedStatus) {
		this.checkedStatus = checkedStatus;
	}
}

package com.optima.nisp.model.api;

import java.io.Serializable;
import java.util.List;

import com.optima.nisp.model.Menu;

public class GroupMenuApi implements Serializable {

	private static final long serialVersionUID = -3527285106690646431L;

	private Long groupId;
	private List<Menu> menus;
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
}

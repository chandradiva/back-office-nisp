package com.optima.nisp.model.api;

import com.optima.nisp.model.Group;

public class GroupApi {
	
	private Group group;
	private Long[] menuIds;
	private Long[] fgroupIds;
	
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	public Long[] getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(Long[] menuIds) {
		this.menuIds = menuIds;
	}
	public Long[] getFgroupIds() {
		return fgroupIds;
	}
	public void setFgroupIds(Long[] fgroupIds) {
		this.fgroupIds = fgroupIds;
	}
}

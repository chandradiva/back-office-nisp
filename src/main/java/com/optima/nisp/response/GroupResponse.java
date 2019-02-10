package com.optima.nisp.response;

public class GroupResponse {

	private Long groupId;
	private String groupName;
	private Integer checkedStatus = 0;

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

	public Integer getCheckedStatus() {
		return checkedStatus;
	}

	public void setCheckedStatus(Integer checkedStatus) {
		this.checkedStatus = checkedStatus;
	}
}

package com.optima.nisp.constanta;

public class UserSession {

	private Long[] groupIds;
	private String username;
	private String lastLogin;
	private String groupName;
	private String fGroupName;
	private Integer idleTime;
	
	public Long[] getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(Long[] groupIds) {
		this.groupIds = groupIds;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getfGroupName() {
		return fGroupName;
	}

	public void setfGroupName(String fGroupName) {
		this.fGroupName = fGroupName;
	}

	public Integer getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(Integer idleTime) {
		this.idleTime = idleTime;
	}
}

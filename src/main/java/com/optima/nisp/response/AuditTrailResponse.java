package com.optima.nisp.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditTrailResponse {

	private Integer number;
	
	private String username;
	
	private String groups;
	
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	private String action;
	
	private String info;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getGroups() {
		return groups;
	}
	public void setGroups(String groups) {
		this.groups = groups;
	}
}

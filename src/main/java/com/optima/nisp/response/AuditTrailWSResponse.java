package com.optima.nisp.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditTrailWSResponse {

	private Integer number;
	
	private String username;
	
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	private String urlPath;
	
	private String reqParam;
	
	private String ipAddress;
	
	private String browser;
	
	private String information;
	
	private String cifKey;
	
	private String activity;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

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

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getCifKey() {
		return cifKey;
	}

	public void setCifKey(String cifKey) {
		this.cifKey = cifKey;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}	
}

package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntitySerialize;

public class AuditTrailWSView implements Serializable {

	private static final long serialVersionUID = -7146152393890408496L;

	@NISPEntitySerialize(field="USERNAME")
	private String username;
	
	@NISPEntitySerialize(field="TIME")
	@JsonFormat(pattern="dd/MM/yyyy - kk:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	@NISPEntitySerialize(field="REQ_PARAM")
	private String reqParam;
	
	@NISPEntitySerialize(field="URL_PATH")
	private String urlPath;
	
	@NISPEntitySerialize(field="IP_ADDRESS")
	private String ipAddress;
	
	@NISPEntitySerialize(field="BROWSER")
	private String browser;
	
	@NISPEntitySerialize(field="INFORMATION")
	private String information;
	
	@NISPEntitySerialize(field="CIF_KEY")
	private String cifKey;
	
	@NISPEntitySerialize(field="ACTIVITY")
	private String activity;

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

	public String getReqParam() {
		return reqParam;
	}

	public void setReqParam(String reqParam) {
		this.reqParam = reqParam;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
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

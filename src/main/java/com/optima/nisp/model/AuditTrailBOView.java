package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntitySerialize;

public class AuditTrailBOView implements Serializable {

	private static final long serialVersionUID = 3017572369523852884L;

	@NISPEntitySerialize(field="USERNAME")
	private String username;
	
	@NISPEntitySerialize(field="GROUPS")
	private String groups;
	
	@NISPEntitySerialize(field="TIME")
	@JsonFormat(pattern="dd/MM/yyyy hh:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	@NISPEntitySerialize(field="MENU_ACTIVITY")
	private String menuActivity;
	
	@NISPEntitySerialize(field="BUTTON_ACTIVITY")
	private String buttonActivity;
	
	@NISPEntitySerialize(field="INFORMATION")
	private String information;
	
	@NISPEntitySerialize(field="URL_PATH")
	private String urlPath;

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

	public String getMenuActivity() {
		return menuActivity;
	}

	public void setMenuActivity(String menuActivity) {
		this.menuActivity = menuActivity;
	}

	public String getButtonActivity() {
		return buttonActivity;
	}

	public void setButtonActivity(String buttonActivity) {
		this.buttonActivity = buttonActivity;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
}

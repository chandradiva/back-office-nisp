package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_LAST_LOGIN", dbSchema=CommonCons.DB_SCHEMA)
public class BoLastLogin implements Serializable{

	private static final long serialVersionUID = 959129399959976333L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="USERNAME")
	@NISPEntity(columnName="USERNAME")
	private String username;
	
	@NISPEntitySerialize(field="LAST_LOGIN")
	@NISPEntity(columnName="LAST_LOGIN")
	private Date lastLogin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	
}

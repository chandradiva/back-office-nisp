package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_BLOCK_HC", dbSchema=CommonCons.DB_SCHEMA)
public class BlockHardcopy implements Serializable {

	private static final long serialVersionUID = -2668455933780298494L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="NAME")
	@NISPEntity(columnName="NAME")
	private String name;
	
	@NISPEntitySerialize(field="ACCOUNT_NUMBER")
	@NISPEntity(columnName="ACCOUNT_NUMBER")
	private String accountNumber;
	
	@NISPEntitySerialize(field="STATUS")
	@NISPEntity(columnName="STATUS")
	private Integer status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

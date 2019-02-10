package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="TBL_REQ_HARDCOPY", dbSchema=CommonCons.DB_SCHEMA)
public class HardcopyRequest implements Serializable {

	private static final long serialVersionUID = -2801053593124661581L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@JsonFormat(pattern="dd MMM yyyy HH:mm:ss", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="REQUEST_DATE")
	@NISPEntity(columnName="REQUEST_DATE")
	private Date requestDate;
	
	@NISPEntitySerialize(field="PERIODE")
	@NISPEntity(columnName="PERIODE")
	private String periode;
	
	@NISPEntitySerialize(field="ACCOUNT_NUMBER")
	@NISPEntity(columnName="ACCOUNT_NUMBER")
	private String accountNumber;
	
	@NISPEntitySerialize(field="CURRENCY")
	@NISPEntity(columnName="CURRENCY")
	private String currency;
	
	@NISPEntitySerialize(field="MAILING_ADDRESS")
	@NISPEntity(columnName="MAILING_ADDRESS")
	private String mailingAddress;
	
	@NISPEntitySerialize(field="STATUS")
	@NISPEntity(columnName="STATUS")
	private Integer status;
	
	@NISPEntitySerialize(field="BATCH_ID")
	@NISPEntity(columnName="BATCH_ID")
	private Long batchId; 
	
	@JsonFormat(pattern="dd MMM yyyy HH:mm:ss", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="PROCESS_DATE")
	@NISPEntity(columnName="PROCESS_DATE")
	private Date processDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public String getPeriode() {
		return periode;
	}

	public void setPeriode(String periode) {
		this.periode = periode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public Date getProcessDate() {
		return processDate;
	}

	public void setProcessDate(Date processDate) {
		this.processDate = processDate;
	}
}

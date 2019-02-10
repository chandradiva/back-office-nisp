package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_CONTENT_PARAM", dbSchema=CommonCons.DB_SCHEMA)
public class ContentParameter implements Serializable {

	private static final long serialVersionUID = -1692943531899254814L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="KEY")
	@NISPEntity(columnName="KEY")
	private String key;
	
	@NISPEntitySerialize(field="KEY_DESC")
	@NISPEntity(columnName="KEY_DESC")
	private String keyDesc;
	
	@NISPEntitySerialize(field="VALUE")
	@NISPEntity(columnName="VALUE")
	private String value;
	
	@JsonFormat(pattern="dd/MM/yyyy", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="CREATED_DATE")
	@NISPEntity(columnName="CREATED_DATE")
	private Date createdDate;
	
	@JsonFormat(pattern="dd/MM/yyyy", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="UPDATED_DATE")
	@NISPEntity(columnName="UPDATED_DATE")
	private Date updatedDate;
	
	@NISPEntitySerialize(field="CREATED_BY")
	@NISPEntity(columnName="CREATED_BY")
	private String createdBy;
	
	@NISPEntitySerialize(field="UPDATED_BY")
	@NISPEntity(columnName="UPDATED_BY")
	private String updatedBy;
	
	@NISPEntitySerialize(field="APPROVED_BY")
	@NISPEntity(columnName="APPROVED_BY")
	private String approvedBy;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKeyDesc() {
		return keyDesc;
	}
	public void setKeyDesc(String keyDesc) {
		this.keyDesc = keyDesc;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
}

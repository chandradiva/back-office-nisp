package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_EMAIL_CONTENT", dbSchema=CommonCons.FE_DB_SCHEMA)
public class EmailContent implements Serializable {

	private static final long serialVersionUID = -3446109559546157106L;
	
	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="HEADER")
	@NISPEntity(columnName="HEADER")
	private String header;
	
	@NISPEntitySerialize(field="PEMBUKA_PASSWORD")
	@NISPEntity(columnName="PEMBUKA_PASSWORD")
	private String pembukaPassword;
	
	@NISPEntitySerialize(field="PEMBUKA_NO_PASSWORD")
	@NISPEntity(columnName="PEMBUKA_NO_PASSWORD")
	private String pembukaNoPassword;
	
	@NISPEntitySerialize(field="BODY")
	@NISPEntity(columnName="BODY")
	private String body;
	
	@NISPEntitySerialize(field="PENUTUP")
	@NISPEntity(columnName="PENUTUP")
	private String penutup;
	
	@NISPEntitySerialize(field="FOOTER")
	@NISPEntity(columnName="FOOTER")
	private String footer;
	
	@NISPEntitySerialize(field="CREATED_DATE")
	@NISPEntity(columnName="CREATED_DATE")
	private Date createdDate;
	
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
	
	@NISPEntitySerialize(field="TYPE")
	@NISPEntity(columnName="TYPE")
	private Integer type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getPembukaPassword() {
		return pembukaPassword;
	}
	public void setPembukaPassword(String pembukaPassword) {
		this.pembukaPassword = pembukaPassword;
	}
	public String getPembukaNoPassword() {
		return pembukaNoPassword;
	}
	public void setPembukaNoPassword(String pembukaNoPassword) {
		this.pembukaNoPassword = pembukaNoPassword;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPenutup() {
		return penutup;
	}
	public void setPenutup(String penutup) {
		this.penutup = penutup;
	}
	public String getFooter() {
		return footer;
	}
	public void setFooter(String footer) {
		this.footer = footer;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}

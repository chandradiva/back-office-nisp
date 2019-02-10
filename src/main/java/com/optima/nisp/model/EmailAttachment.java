package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_EMAIL_ATTACHMENT", dbSchema=CommonCons.FE_DB_SCHEMA)
public class EmailAttachment implements Serializable{

	private static final long serialVersionUID = -276628072158051873L;
	
	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="CATEGORY")
	@NISPEntity(columnName="CATEGORY")
	private Integer category;
	
	@NISPEntitySerialize(field="ATTACHMENT")
	@NISPEntity(columnName="ATTACHMENT")
	private String attachment;
	
	@JsonFormat(pattern="dd/MM/yyyy", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="UPDATED_DATE")
	@NISPEntity(columnName="UPDATED_DATE")
	private Date updatedDate;
	
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

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="TBL_CC_EMAIL_ATTACHMENT", dbSchema=CommonCons.FE_DB_SCHEMA)
public class CCEmailAttachment implements Serializable {

	private static final long serialVersionUID = 3583020766944184072L;

	@NISPEntityId
	@NISPEntitySerialize(field="BIN")
	@NISPEntity(columnName="BIN")
	private String bin;
	
	@NISPEntitySerialize(field="PRODUCT_NAME")
	@NISPEntity(columnName="PRODUCT_NAME")
	private String productName;
	
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
	
	@NISPEntitySerialize(field="ATTCH_CODE")
	@NISPEntity(columnName="ATTCH_CODE")
	private String attchCode;

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getAttchCode() {
		return attchCode;
	}

	public void setAttchCode(String attchCode) {
		this.attchCode = attchCode;
	}
}

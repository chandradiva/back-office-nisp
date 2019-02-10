package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="TBL_CC_EMAIL_ATTACHMENT_TEMP", dbSchema=CommonCons.FE_DB_SCHEMA)
public class CCEmailAttachmentTemp implements Serializable {

	private static final long serialVersionUID = -4651599046565341809L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="BIN")
	@NISPEntity(columnName="BIN")
	private String bin;
	
	@NISPEntitySerialize(field="OLD_ATTACHMENT")
	@NISPEntity(columnName="OLD_ATTACHMENT")
	private String oldAttachment;
	
	@NISPEntitySerialize(field="NEW_ATTACHMENT")
	@NISPEntity(columnName="NEW_ATTACHMENT")
	private String newAttachment;
	
	@NISPEntitySerialize(field="REVIEWED_STATUS")
	@NISPEntity(columnName="REVIEWED_STATUS")
	private Integer reviewedStatus;
	
	@NISPEntitySerialize(field="UPDATED_BY")
	@NISPEntity(columnName="UPDATED_BY")
	private String updatedBy;
	
	@NISPEntitySerialize(field="REVIEWED_BY")
	@NISPEntity(columnName="REVIEWED_BY")
	private String reviewedBy;
	
	@NISPEntitySerialize(field="SHOW_NOTIF_STATUS")
	@NISPEntity(columnName="SHOW_NOTIF_STATUS")
	private Integer showNotifStatus;
	
	public CCEmailAttachmentTemp(){}
	
	public CCEmailAttachmentTemp(String bin, String oldAttachment, String newAttachment, Integer reviewedStatus){
		this.bin = bin;
		this.oldAttachment = oldAttachment;
		this.newAttachment = newAttachment;
		this.reviewedStatus = reviewedStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBin() {
		return bin;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public String getOldAttachment() {
		return oldAttachment;
	}

	public void setOldAttachment(String oldAttachment) {
		this.oldAttachment = oldAttachment;
	}

	public String getNewAttachment() {
		return newAttachment;
	}

	public void setNewAttachment(String newAttachment) {
		this.newAttachment = newAttachment;
	}

	public Integer getReviewedStatus() {
		return reviewedStatus;
	}

	public void setReviewedStatus(Integer reviewedStatus) {
		this.reviewedStatus = reviewedStatus;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getReviewedBy() {
		return reviewedBy;
	}

	public void setReviewedBy(String reviewedBy) {
		this.reviewedBy = reviewedBy;
	}

	public Integer getShowNotifStatus() {
		return showNotifStatus;
	}

	public void setShowNotifStatus(Integer showNotifStatus) {
		this.showNotifStatus = showNotifStatus;
	}
}

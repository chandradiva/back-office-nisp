package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_EMAIL_CONTENT_TEMP", dbSchema=CommonCons.FE_DB_SCHEMA)
public class EmailContentTemp implements Serializable {

	private static final long serialVersionUID = -8302945883694486182L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="EMAIL_CONTENT_ID")
	@NISPEntity(columnName="EMAIL_CONTENT_ID")
	private Long emailContentId;
	
	@NISPEntitySerialize(field="NEW_HEADER")
	@NISPEntity(columnName="NEW_HEADER")
	private String newHeader;
	
	@NISPEntitySerialize(field="NEW_PEMBUKA_PASSWORD")
	@NISPEntity(columnName="NEW_PEMBUKA_PASSWORD")
	private String newPembukaPassword;
	
	@NISPEntitySerialize(field="NEW_PEMBUKA_NO_PASSWORD")
	@NISPEntity(columnName="NEW_PEMBUKA_NO_PASSWORD")
	private String newPembukaNoPassword;
	
	@NISPEntitySerialize(field="NEW_BODY")
	@NISPEntity(columnName="NEW_BODY")
	private String newBody;
	
	@NISPEntitySerialize(field="NEW_PENUTUP")
	@NISPEntity(columnName="NEW_PENUTUP")
	private String newPenutup;
	
	@NISPEntitySerialize(field="NEW_FOOTER")
	@NISPEntity(columnName="NEW_FOOTER")
	private String newFooter;
	
	@NISPEntitySerialize(field="OLD_HEADER")
	@NISPEntity(columnName="OLD_HEADER")
	private String oldHeader;
	
	@NISPEntitySerialize(field="OLD_PEMBUKA_PASSWORD")
	@NISPEntity(columnName="OLD_PEMBUKA_PASSWORD")
	private String oldPembukaPassword;
	
	@NISPEntitySerialize(field="OLD_PEMBUKA_NO_PASSWORD")
	@NISPEntity(columnName="OLD_PEMBUKA_NO_PASSWORD")
	private String oldPembukaNoPassword;
	
	@NISPEntitySerialize(field="OLD_BODY")
	@NISPEntity(columnName="OLD_BODY")
	private String oldBody;
	
	@NISPEntitySerialize(field="OLD_PENUTUP")
	@NISPEntity(columnName="OLD_PENUTUP")
	private String oldPenutup;
	
	@NISPEntitySerialize(field="OLD_FOOTER")
	@NISPEntity(columnName="OLD_FOOTER")
	private String oldFooter;
	
	@NISPEntitySerialize(field="UPDATED_BY")
	@NISPEntity(columnName="UPDATED_BY")
	private String updatedBy;
	
	@NISPEntitySerialize(field="REVIEWED_STATUS")
	@NISPEntity(columnName="REVIEWED_STATUS")
	private Integer reviewedStatus;
	
	@NISPEntitySerialize(field="REVIEWED_BY")
	@NISPEntity(columnName="REVIEWED_BY")
	private String reviewedBy;
	
	@NISPEntitySerialize(field="SHOW_NOTIF_STATUS")
	@NISPEntity(columnName="SHOW_NOTIF_STATUS")
	private Integer showNotifStatus;

	@NISPEntitySerialize(field="TYPE")
	@NISPEntity(columnName="TYPE")
	private Integer type;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmailContentId() {
		return emailContentId;
	}

	public void setEmailContentId(Long emailContentId) {
		this.emailContentId = emailContentId;
	}

	public String getNewHeader() {
		return newHeader;
	}

	public void setNewHeader(String newHeader) {
		this.newHeader = newHeader;
	}

	public String getNewPembukaPassword() {
		return newPembukaPassword;
	}

	public void setNewPembukaPassword(String newPembukaPassword) {
		this.newPembukaPassword = newPembukaPassword;
	}

	public String getNewPembukaNoPassword() {
		return newPembukaNoPassword;
	}

	public void setNewPembukaNoPassword(String newPembukaNoPassword) {
		this.newPembukaNoPassword = newPembukaNoPassword;
	}

	public String getNewBody() {
		return newBody;
	}

	public void setNewBody(String newBody) {
		this.newBody = newBody;
	}

	public String getNewPenutup() {
		return newPenutup;
	}

	public void setNewPenutup(String newPenutup) {
		this.newPenutup = newPenutup;
	}

	public String getNewFooter() {
		return newFooter;
	}

	public void setNewFooter(String newFooter) {
		this.newFooter = newFooter;
	}

	public String getOldHeader() {
		return oldHeader;
	}

	public void setOldHeader(String oldHeader) {
		this.oldHeader = oldHeader;
	}

	public String getOldPembukaPassword() {
		return oldPembukaPassword;
	}

	public void setOldPembukaPassword(String oldPembukaPassword) {
		this.oldPembukaPassword = oldPembukaPassword;
	}

	public String getOldPembukaNoPassword() {
		return oldPembukaNoPassword;
	}

	public void setOldPembukaNoPassword(String oldPembukaNoPassword) {
		this.oldPembukaNoPassword = oldPembukaNoPassword;
	}

	public String getOldBody() {
		return oldBody;
	}

	public void setOldBody(String oldBody) {
		this.oldBody = oldBody;
	}

	public String getOldPenutup() {
		return oldPenutup;
	}

	public void setOldPenutup(String oldPenutup) {
		this.oldPenutup = oldPenutup;
	}

	public String getOldFooter() {
		return oldFooter;
	}

	public void setOldFooter(String oldFooter) {
		this.oldFooter = oldFooter;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	public Integer getReviewedStatus() {
		return reviewedStatus;
	}

	public void setReviewedStatus(Integer reviewedStatus) {
		this.reviewedStatus = reviewedStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

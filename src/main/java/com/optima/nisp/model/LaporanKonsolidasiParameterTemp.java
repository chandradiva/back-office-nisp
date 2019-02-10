package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName = "BO_TBL_KONSOLIDASI_PARAM_TMP", dbSchema = CommonCons.FE_DB_SCHEMA)
public class LaporanKonsolidasiParameterTemp implements Serializable {

	private static final long serialVersionUID = 5825092538989477557L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="KEY")
	@NISPEntity(columnName="KEY")
	private String key;
	
	@NISPEntitySerialize(field="OLD_VALUE")
	@NISPEntity(columnName="OLD_VALUE")
	private String oldValue;
	
	@NISPEntitySerialize(field="NEW_VALUE")
	@NISPEntity(columnName="NEW_VALUE")
	private String newValue;
	
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

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
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

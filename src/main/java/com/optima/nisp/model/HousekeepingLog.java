package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.optima.nisp.annotations.NISPEntity;

@NISPEntity(tableName = "TBL_HOUSEKEEPING_LOG")
public class HousekeepingLog implements Serializable {

	private static final long serialVersionUID = -9180864541742529592L;

	@NISPEntity(columnName = "ID")
	private Long id;
	
	@NISPEntity(columnName = "START_DATE")
	private Date startDate;
	
	@NISPEntity(columnName = "END_DATE")
	private Date endDate;
	
	@NISPEntity(columnName = "STATUS")
	private int status;
	
	@NISPEntity(columnName = "REMARKS")
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}

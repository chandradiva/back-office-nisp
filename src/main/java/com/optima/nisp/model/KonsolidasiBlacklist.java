package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName = "BO_TBL_KONSOLIDASI_BLACKLIST", dbSchema = CommonCons.FE_DB_SCHEMA)
public class KonsolidasiBlacklist implements Serializable {

	private static final long serialVersionUID = 7203391941648357187L;

	@NISPEntitySerialize(field = "ID")
	@NISPEntity(columnName = "ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field = "CIF_ORI")
	@NISPEntity(columnName = "CIF_ORI")
	private String cifOri;
	
	@NISPEntitySerialize(field = "CIF_PAD")
	@NISPEntity(columnName = "CIF_PAD")
	private String cifPad;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Jakarta")
	@NISPEntitySerialize(field = "CREATED_DATE")
	@NISPEntity(columnName = "CREATED_DATE")
	private Date createdDate;
	
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Jakarta")
	@NISPEntitySerialize(field = "UPDATED_DATE")
	@NISPEntity(columnName = "UPDATED_DATE")
	private Date updatedDate;
	
	@NISPEntitySerialize(field = "FLAG")
	@NISPEntity(columnName = "FLAG")
	private Long flag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCifOri() {
		return cifOri;
	}

	public void setCifOri(String cifOri) {
		this.cifOri = cifOri;
	}

	public String getCifPad() {
		return cifPad;
	}

	public void setCifPad(String cifPad) {
		this.cifPad = cifPad;
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

	public Long getFlag() {
		return flag;
	}

	public void setFlag(Long flag) {
		this.flag = flag;
	}
	
}

package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_KONSOLIDASI_EMAIL_LOG", dbSchema = CommonCons.DB_SCHEMA)
public class KonsolidasiEmailLog implements Serializable {

	private static final long serialVersionUID = 2367584684663197406L;

	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="SUBJECT")
	@NISPEntity(columnName="SUBJECT")
	private String subject;
	
	@NISPEntitySerialize(field="MAIL_TO")
	@NISPEntity(columnName="MAIL_TO")
	private String mailTo;
	
	@NISPEntitySerialize(field="TIME")
	@NISPEntity(columnName="TIME")
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	@NISPEntitySerialize(field="STATUS")
	@NISPEntity(columnName="STATUS")
	private Integer status;

	@NISPEntitySerialize(field="NAMA_REKENING")
	@NISPEntity(columnName="NAMA_REKENING")
	private String namaRekening;
	
	@NISPEntitySerialize(field="CIF_KEY")
	@NISPEntity(columnName="CIF_KEY")
	private String cifKey;
	
	@NISPEntitySerialize(field="ACCOUNT_NUMBER")
	@NISPEntity(columnName="ACCOUNT_NUMBER")
	private String accountNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getNamaRekening() {
		return namaRekening;
	}

	public void setNamaRekening(String namaRekening) {
		this.namaRekening = namaRekening;
	}

	public String getCifKey() {
		return cifKey;
	}

	public void setCifKey(String cifKey) {
		this.cifKey = cifKey;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}

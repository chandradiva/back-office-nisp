package com.optima.nisp.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class KonsolidasiEmailLogResponse {

private Integer number;
	
	private Long id;
	
	private String subject;
	
	private String mailTo;
	
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date time;
	
	private String namaRekening;
	
	private String cifKey;

	private String accountNumber;
	
	private String status;

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

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}

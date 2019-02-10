package com.optima.nisp.model.api;

import java.util.List;

public class EmailContentCCApi {
	private Long id;
	private String header;
	private String pembuka1;	//Pembuka dengan Password
	private String pembuka2;	//Pembuka tanpa password
	private String body;
	private String penutup;
	private String footer;
	private String oldHeader;
	private String oldPembuka1;
	private String oldPembuka2;
	private String oldBody;
	private String oldPenutup;
	private String oldFooter;
	private List<EmailAttachmentCCApi> attachments;

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
	
	public String getPembuka1() {
		return pembuka1;
	}
	public void setPembuka1(String pembuka) {
		this.pembuka1 = pembuka;
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
	public String getOldHeader() {
		return oldHeader;
	}
	public void setOldHeader(String oldHeader) {
		this.oldHeader = oldHeader;
	}
	public String getOldPembuka1() {
		return oldPembuka1;
	}
	public void setOldPembuka1(String oldPembuka) {
		this.oldPembuka1 = oldPembuka;
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
	public String getPembuka2() {
		return pembuka2;
	}
	public void setPembuka2(String pembuka2) {
		this.pembuka2 = pembuka2;
	}
	public String getOldPembuka2() {
		return oldPembuka2;
	}
	public void setOldPembuka2(String oldPembuka2) {
		this.oldPembuka2 = oldPembuka2;
	}
	public List<EmailAttachmentCCApi> getAttachments() {
		return attachments;
	}
	public void setAttachments(List<EmailAttachmentCCApi> attachments) {
		this.attachments = attachments;
	}
}

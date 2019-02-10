package com.optima.nisp.model.api;

public class EmailContentCASAApi {

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
	private EmailAttachmentApi attachment1;
	private EmailAttachmentApi attachment2;
	private EmailAttachmentApi attachment3;
	private EmailAttachmentApi attachment4;
	private EmailAttachmentApi attachment5;
	private EmailAttachmentApi attachment6;

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
	public EmailAttachmentApi getAttachment1() {
		return attachment1;
	}
	public void setAttachment1(EmailAttachmentApi attachment1) {
		this.attachment1 = attachment1;
	}
	public EmailAttachmentApi getAttachment2() {
		return attachment2;
	}
	public void setAttachment2(EmailAttachmentApi attachment2) {
		this.attachment2 = attachment2;
	}
	public EmailAttachmentApi getAttachment3() {
		return attachment3;
	}
	public void setAttachment3(EmailAttachmentApi attachment3) {
		this.attachment3 = attachment3;
	}
	public EmailAttachmentApi getAttachment4() {
		return attachment4;
	}
	public void setAttachment4(EmailAttachmentApi attachment4) {
		this.attachment4 = attachment4;
	}
	public EmailAttachmentApi getAttachment5() {
		return attachment5;
	}
	public void setAttachment5(EmailAttachmentApi attachment5) {
		this.attachment5 = attachment5;
	}
	public EmailAttachmentApi getAttachment6() {
		return attachment6;
	}
	public void setAttachment6(EmailAttachmentApi attachment6) {
		this.attachment6 = attachment6;
	}
}

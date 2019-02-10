package com.optima.nisp.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_SEND_EMAIL", dbSchema=CommonCons.DB_SCHEMA)
public class SendEmail implements Serializable {

	private static final long serialVersionUID = -7160216311013910082L;
	
	@NISPEntitySerialize(field="ID")
	@NISPEntity(columnName="ID")
	@NISPEntityId
	private Long id;
	
	@NISPEntitySerialize(field="CIF_KEY")
	@NISPEntity(columnName="CIF_KEY")
	private String cifKey;
	
	@NISPEntitySerialize(field="ACCOUNT_NUMBER")
	@NISPEntity(columnName="ACCOUNT_NUMBER")
	private String accountNumber;
	
	@JsonFormat(pattern="dd/MM/yyyy", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="TGL_CETAK")
	@NISPEntity(columnName="TGL_CETAK")
	private Date tglCetak;
	
	@NISPEntitySerialize(field="STATUS")
	@NISPEntity(columnName="STATUS")
	private int status;	//0 = Belum Terkirim, 1 = Terkirim, 2 = Gagal Kirim, 3 = Expired, 4 = Processing
	
	@JsonFormat(pattern="dd/MM/yyyy", timezone="Asia/Jakarta")
	@NISPEntitySerialize(field="TGL_KIRIM")
	@NISPEntity(columnName="TGL_KIRIM")
	private Date tglKirim;
	
	@NISPEntitySerialize(field="FILENAME")
	@NISPEntity(columnName="FILENAME")
	private String filename;
	
	@NISPEntitySerialize(field="SENDER")
	@NISPEntity(columnName="SENDER")
	private Long sender;
	
	@NISPEntitySerialize(field="PERIODE")
	@NISPEntity(columnName="PERIODE")
	private String periode;
	
	@NISPEntitySerialize(field="CATEGORY")
	@NISPEntity(columnName="CATEGORY")
	private Integer category;
	
	@NISPEntitySerialize(field="NAMA_REKENING")
	@NISPEntity(columnName="NAMA_REKENING")
	private String namaRekening;
	
	@NISPEntitySerialize(field="PRODUCT_NAME")
	@NISPEntity(columnName="PRODUCT_NAME")
	private String productName;
	
	@NISPEntitySerialize(field="IS_PASSWORD_DEFAULT")
	@NISPEntity(columnName="IS_PASSWORD_DEFAULT")
	private Integer isPasswordDefault;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Date getTglCetak() {
		return tglCetak;
	}
	public void setTglCetak(Date tglCetak) {
		this.tglCetak = tglCetak;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getTglKirim() {
		return tglKirim;
	}
	public void setTglKirim(Date tglKirim) {
		this.tglKirim = tglKirim;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Long getSender() {
		return sender;
	}
	public void setSender(Long sender) {
		this.sender = sender;
	}
	public String getPeriode() {
		return periode;
	}
	public void setPeriode(String periode) {
		this.periode = periode;
	}
	public Integer getCategory() {
		return category;
	}
	public void setCategory(Integer category) {
		this.category = category;
	}
	public String getNamaRekening() {
		return namaRekening;
	}
	public void setNamaRekening(String namaRekening) {
		this.namaRekening = namaRekening;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getIsPasswordDefault() {
		return isPasswordDefault;
	}
	public void setIsPasswordDefault(Integer isPasswordDefault) {
		this.isPasswordDefault = isPasswordDefault;
	}
	
}

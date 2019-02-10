package com.optima.nisp.response;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HouseKeepingResponse implements Serializable{
	private static final long serialVersionUID = 1937409973727741082L;
	private int number;
	private String proses;
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date waktuMulai;
	@JsonFormat(pattern="dd/MM/yyyy - HH:mm:ss", timezone="Asia/Jakarta")
	private Date waktuSelesai;
	private String status;
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getProses() {
		return proses;
	}
	public void setProses(String proses) {
		this.proses = proses;
	}
	public Date getWaktuMulai() {
		return waktuMulai;
	}
	public void setWaktuMulai(Date waktuMulai) {
		this.waktuMulai = waktuMulai;
	}
	public Date getWaktuSelesai() {
		return waktuSelesai;
	}
	public void setWaktuSelesai(Date waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}

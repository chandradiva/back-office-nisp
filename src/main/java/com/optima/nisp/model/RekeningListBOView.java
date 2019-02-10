package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntitySerialize;

public class RekeningListBOView implements Serializable {
	private static final long serialVersionUID = 2026933206839602206L;

	@NISPEntitySerialize(field="CIF_NO")
	private String cifKey;
	
	@NISPEntitySerialize(field="NAMA_NASABAH_1")
	private String namaNasabah1;
	
	@NISPEntitySerialize(field="NAMA_NASABAH_2")
	private String namaNasabah2;
	
	@NISPEntitySerialize(field="NAMA_NASABAH_3")
	private String namaNasabah3;
	
	@NISPEntitySerialize(field="ACCOUNT_NUMBER")
	private String accountNo;
	
	@NISPEntitySerialize(field="CURRENCY_TYPE")
	private String currency;

	@NISPEntitySerialize(field="R")
	private int number;
	
	@NISPEntitySerialize(field = "PRODUCT_NAME")
	private String productName;
	
	@NISPEntitySerialize(field = "BANK_BRANCH_NAME")
	private String branchName;
	
	@NISPEntitySerialize(field = "BRANCH_NUMBER")
	private String branchCode;
	
	@NISPEntitySerialize(field = "ACCOUNT_TYPE")
	private String accountType;
	
	@NISPEntitySerialize(field = "FLAG")
	private int flag;

	public String getCifKey() {
		return cifKey;
	}

	public void setCifKey(String cifKey) {
		this.cifKey = cifKey;
	}

	public String getNamaNasabah1() {
		return namaNasabah1;
	}

	public void setNamaNasabah1(String namaNasabah1) {
		this.namaNasabah1 = namaNasabah1;
	}

	public String getNamaNasabah2() {
		return namaNasabah2;
	}

	public void setNamaNasabah2(String namaNasabah2) {
		this.namaNasabah2 = namaNasabah2;
	}

	public String getNamaNasabah3() {
		return namaNasabah3;
	}

	public void setNamaNasabah3(String namaNasabah3) {
		this.namaNasabah3 = namaNasabah3;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
}

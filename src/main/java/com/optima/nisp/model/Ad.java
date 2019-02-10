package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntitySerialize;

public class Ad implements Serializable {

	private static final long serialVersionUID = -4350625238946350202L;
	
	@NISPEntitySerialize(field="AD_ID")
	private Long adId;
	
	@NISPEntitySerialize(field="NAME")
	private String name;
	
	@NISPEntitySerialize(field="DESCRIPTION")
	private String description;

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}		
}

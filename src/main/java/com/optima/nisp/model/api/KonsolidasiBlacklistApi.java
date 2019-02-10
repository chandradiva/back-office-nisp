package com.optima.nisp.model.api;

import java.io.Serializable;

public class KonsolidasiBlacklistApi implements Serializable {

	private static final long serialVersionUID = -2193024213539736968L;
	private Long id;
	private String cif;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	
}

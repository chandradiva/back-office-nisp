package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_BUTTON", dbSchema=CommonCons.FE_DB_SCHEMA)
public class Button implements Serializable {

	private static final long serialVersionUID = 2749337078441248338L;
	
	@NISPEntitySerialize(field="BUTTON_ID")
	@NISPEntity(columnName="BUTTON_ID")
	@NISPEntityId
	private Long buttonId;
	
	@NISPEntitySerialize(field="BUTTON_TITLE")
	@NISPEntity(columnName="BUTTON_TITLE")
	private String buttonTitle;
	
	@NISPEntitySerialize(field="BUTTON_URL")
	@NISPEntity(columnName="BUTTON_URL")
	private String buttonUrl;
	
	@NISPEntitySerialize(field="BUTTON_ACTION")
	@NISPEntity(columnName="BUTTON_ACTION")
	private String buttonAction;
	
	public Long getButtonId() {
		return buttonId;
	}

	public void setButtonId(Long buttonId) {
		this.buttonId = buttonId;
	}

	public String getButtonTitle() {
		return buttonTitle;
	}

	public void setButtonTitle(String titleBtn) {
		this.buttonTitle = titleBtn;
	}

	public String getButtonUrl() {
		return buttonUrl;
	}

	public void setButtonUrl(String buttonUrl) {
		this.buttonUrl = buttonUrl;
	}

	public String getButtonAction() {
		return buttonAction;
	}

	public void setButtonAction(String buttonAction) {
		this.buttonAction = buttonAction;
	}
}

package com.optima.nisp.model;

import java.io.Serializable;

import com.optima.nisp.annotations.NISPEntity;
import com.optima.nisp.annotations.NISPEntityId;
import com.optima.nisp.annotations.NISPEntitySerialize;
import com.optima.nisp.constanta.CommonCons;

@NISPEntity(tableName="BO_TBL_GROUP_AD", dbSchema=CommonCons.FE_DB_SCHEMA)
public class GroupAd implements Serializable {

	private static final long serialVersionUID = -8141064556775038468L;
	
	@NISPEntitySerialize(field="GROUP_AD_ID")
	@NISPEntity(columnName="GROUP_AD_ID")
	@NISPEntityId
	private Long groupAdId;
	
	@NISPEntitySerialize(field="GROUP_ID")
	@NISPEntity(columnName="GROUP_ID")
	private Long groupId;
	
	@NISPEntitySerialize(field="AD_ID")
	@NISPEntity(columnName="AD_ID")
	private Long adId;

	public Long getGroupAdId() {
		return groupAdId;
	}

	public void setGroupAdId(Long groupAdId) {
		this.groupAdId = groupAdId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getAdId() {
		return adId;
	}

	public void setAdId(Long adId) {
		this.adId = adId;
	}
}

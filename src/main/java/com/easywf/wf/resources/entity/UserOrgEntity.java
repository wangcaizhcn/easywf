package com.easywf.wf.resources.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_user_org")
public class UserOrgEntity {

	@Id
	private String id;
	
	@Column(name = "user_id")
	private String userId;
	
	@Column(name = "org_id")
	private String orgId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
}

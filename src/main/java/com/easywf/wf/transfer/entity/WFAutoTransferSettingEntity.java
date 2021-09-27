package com.easywf.wf.transfer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 流程自动转办
 */
@Entity
@Table(name = "easy_wf_auto_transfer_setting")
public class WFAutoTransferSettingEntity {

	public WFAutoTransferSettingEntity() {}
	
	@Id
	private String id;
	
	@Column(name = "approver_id")
	private String approverId;
	
	@Column(name = "begin_time")
	private Date beginTime;
	
	@Column(name = "end_time")
	private Date endTime;
	
	@Column(name = "transfer_user_id")
	private String transferUserId;
	
	@Column(name = "update_time")
	private Date updateTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTransferUserId() {
		return transferUserId;
	}

	public void setTransferUserId(String transferUserId) {
		this.transferUserId = transferUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}





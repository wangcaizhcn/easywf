package com.easywf.wf.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_process_hangup",
indexes = {@Index(name = "idx_process_id",  columnList="process_id", unique = false),
		@Index(name = "idx_hangup_time",  columnList="hangup_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_process_hangup", comment = "流程挂起表")
public class ProcessHangupEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "process_id", columnDefinition = "varchar(36) COMMENT '所属流程实例ID'")
	private String processId;
	
	@Column(name = "hangup_time", columnDefinition = "datetime COMMENT '流程挂起时间'")
	private Date hangupTime;
	
	@Column(name = "wakeup_time", columnDefinition = "datetime COMMENT '流程唤醒时间'")
	private Date wakeupTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public Date getHangupTime() {
		return hangupTime;
	}

	public void setHangupTime(Date hangupTime) {
		this.hangupTime = hangupTime;
	}

	public Date getWakeupTime() {
		return wakeupTime;
	}

	public void setWakeupTime(Date wakeupTime) {
		this.wakeupTime = wakeupTime;
	}

}

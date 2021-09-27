package com.easywf.wf.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_workitem_transfer",
indexes = {@Index(name = "idx_workitem_id",  columnList="workitem_id", unique = false),
		@Index(name = "idx_transfer_time",  columnList="transfer_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_workitem_transfer", comment = "审批转办表")
public class WorkitemTransferEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "workitem_id", columnDefinition = "varchar(36) COMMENT '审批工作项ID'")
	private String workitemId;
	
	@Column(name = "approver", columnDefinition = "varchar(36) COMMENT '原审批人'")
	private String approver;
	
	@Column(name = "transfer_approver", columnDefinition = "varchar(36) COMMENT '转办后审批人'")
	private String transferApprover;
	
	@Column(name = "transfer_time", columnDefinition = "datetime COMMENT '转办时间'")
	private Date transferTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getTransferApprover() {
		return transferApprover;
	}

	public void setTransferApprover(String transferApprover) {
		this.transferApprover = transferApprover;
	}

	public Date getTransferTime() {
		return transferTime;
	}

	public void setTransferTime(Date transferTime) {
		this.transferTime = transferTime;
	}
	
}

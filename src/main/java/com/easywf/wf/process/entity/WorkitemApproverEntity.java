package com.easywf.wf.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_workitem_approver",
indexes = {@Index(name = "idx_workitem_id",  columnList="workitem_id", unique = false),
		@Index(name = "idx_approve_time",  columnList="approve_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_workitem_approver", comment = "审批表")
public class WorkitemApproverEntity {
	
	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "workitem_id", columnDefinition = "varchar(36) COMMENT '审批工作项ID'")
	private String workitemId;
	
	@Column(name = "approver", columnDefinition = "varchar(36) COMMENT '审批人'")
	private String approver;
	
	@Column(name = "opinions", columnDefinition = "varchar(100) COMMENT '审批意见'")
	private String opinions;
	
	@Column(name = "approve_result", columnDefinition = "varchar(20) COMMENT '审批结果'")
	private String approveResult;
	
	@Column(name = "approve_time", columnDefinition = "datetime COMMENT '审批时间'")
	private Date approveTime;

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

	public String getOpinions() {
		return opinions;
	}

	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}

	public String getApproveResult() {
		return approveResult;
	}

	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}

	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	
}

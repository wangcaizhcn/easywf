package com.easywf.wf.util.context;

import java.util.Date;

public class TransferContext {

	// 流程ID
	private String processId;
	
	// 工作项ID
	private String workitemId;
	
	// 当前审批人ID
	private String approver;
	
	// 转办后审批人
	private String transferApprover;
	
	// 流程发起者ID
	private String promoter;
	
	// 流程启动时间
	private Date startTime;
	
	// 业务数据ID
	private String recordId;
	
	// 流程标题
	private String title;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public String getPromoter() {
		return promoter;
	}

	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}

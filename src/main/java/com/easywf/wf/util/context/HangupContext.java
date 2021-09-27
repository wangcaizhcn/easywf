package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HangupContext {

	// 流程ID
	private String processId;
	
	// 流程发起者ID
	private String promoter;
	
	// 流程启动时间
	private Date startTime;
	
	// 业务数据ID
	private String recordId;
	
	// 流程标题
	private String title;
	
	// 当前正在审批人
	private List<String> approvers = new ArrayList<>();

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
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

	public List<String> getApprovers() {
		return approvers;
	}

	public void setApprovers(List<String> approvers) {
		this.approvers = approvers;
	}
	
}

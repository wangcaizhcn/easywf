package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 审核上下文对象，业务监听节点办理接口传入的上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ApproveContext {

	//当前审批人ID
	private String approverId;
	
	//审核通过的工作项信息
	private ApprovedItemContext approved;
	
	//待审核工作项  TODO 考虑下并发多个分支， 并发的时候，考虑已完成工作项另外分支不完成的情况， 考虑下已通过工作项是否需要多个（并发的时候）
	private List<ApprovePendingItemContext> approvePendings = new ArrayList<>();
	
	// 流程上下文信息
	private ProcessApproveContext processContext;

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public ApprovedItemContext getApproved() {
		return approved;
	}

	public void setApproved(ApprovedItemContext approved) {
		this.approved = approved;
	}

	public List<ApprovePendingItemContext> getApprovePendings() {
		return approvePendings;
	}

	public void setApprovePendings(List<ApprovePendingItemContext> approvePendings) {
		this.approvePendings = approvePendings;
	}

	public ProcessApproveContext getProcessContext() {
		return processContext;
	}

	public void setProcessContext(ProcessApproveContext processContext) {
		this.processContext = processContext;
	}
	
}

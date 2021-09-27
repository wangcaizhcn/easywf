package com.easywf.wf.process.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;
import com.easywf.wf.util.context.ApprovedItemContext;
import com.easywf.wf.util.context.ProcessBaseContext;

/**
 * 获取流程详细信息
 * @author wangcai
 * @version 1.0
 */
public class ProcessDetailVO extends ProcessBaseContext{
	
	// 发起者姓名
	private String promoterName;
	
	// 流程状态
	private WFProcessState processState;
	
	// 流程审批状态
	private WFProcessApproveState processApproveState;
	
	// 审批结束时间、挂起时间、终止时间、驳回时间
	private Date optTime;
	
	// 已办工作项详细信息
	private List<ApprovedItemContext> approved = new ArrayList<>();
	
	// 代办工作项信息
	private List<ApprovedItemContext> approvePending = new ArrayList<>();

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public WFProcessState getProcessState() {
		return processState;
	}

	public void setProcessState(WFProcessState processState) {
		this.processState = processState;
	}

	public WFProcessApproveState getProcessApproveState() {
		return processApproveState;
	}

	public void setProcessApproveState(WFProcessApproveState processApproveState) {
		this.processApproveState = processApproveState;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public List<ApprovedItemContext> getApproved() {
		return approved;
	}

	public void setApproved(List<ApprovedItemContext> approved) {
		this.approved = approved;
	}

	public List<ApprovedItemContext> getApprovePending() {
		return approvePending;
	}

	public void setApprovePending(List<ApprovedItemContext> approvePending) {
		this.approvePending = approvePending;
	}

}

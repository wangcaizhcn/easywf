package com.easywf.wf.process.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;
import com.easywf.wf.util.context.ApprovedItemContext;
import com.easywf.wf.util.context.ProcessBaseContext;

public class ProcessListVO extends ProcessBaseContext {

	// 发起人姓名
	private String promoterName;
	
	// 流程状态
	private WFProcessState state;
	
	// 流程结束审批状态
	private WFProcessApproveState approveState;
	
	// 审批结束时间、挂起时间、驳回时间、终止时间等
	private Date optTime;
	
	// 流程的已审批人
	private List<ApprovedItemContext> approveInfo = new ArrayList<>();
	
	// 待审批信息
	private List<ApprovePendingQueryVO> approvePendings = new ArrayList<>();
	
	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public WFProcessState getState() {
		return state;
	}

	public void setState(WFProcessState state) {
		this.state = state;
	}

	public WFProcessApproveState getApproveState() {
		return approveState;
	}

	public void setApproveState(WFProcessApproveState approveState) {
		this.approveState = approveState;
	}

	public Date getOptTime() {
		return optTime;
	}

	public void setOptTime(Date optTime) {
		this.optTime = optTime;
	}

	public List<ApprovedItemContext> getApproveInfo() {
		return approveInfo;
	}

	public List<ApprovePendingQueryVO> getApprovePendings() {
		return approvePendings;
	}

	public void setApproveInfo(List<ApprovedItemContext> approveInfo) {
		this.approveInfo = approveInfo;
	}

	public void setApprovePendings(List<ApprovePendingQueryVO> approvePendings) {
		this.approvePendings = approvePendings;
	}
	
}

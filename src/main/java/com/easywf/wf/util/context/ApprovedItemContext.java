package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.List;

/**
 * 已办项上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ApprovedItemContext extends WorkitemBaseContext {
	
	// 审批项明细，已办工作项中，只保留用户的通过和不通过状态，其余状态忽略
	private List<ApproveBaseContext> approveInfo = new ArrayList<>();
	
	// 通过的工作项未办理用户
	private List<String> otherApprovals = new ArrayList<>();

	/**
	 * 获取工作项已办用户的审批详细信息
	 * @return 审批详细信息
	 */
	public List<ApproveBaseContext> getApproveInfo() {
		return approveInfo;
	}

	/**
	 * 设置工作项的审批详细信息
	 * @param approveInfo 审批详细信息
	 */
	public void setApproveInfo(List<ApproveBaseContext> approveInfo) {
		this.approveInfo = approveInfo;
	}

	/**
	 * 获取其他未审批的用户ID集合
	 * @return 未审批用户ID集合
	 */
	public List<String> getOtherApprovals() {
		return otherApprovals;
	}

	/**
	 * 设置未审批的用户ID集合
	 * @param otherApprovals 未审批用户ID集合
	 */
	public void setOtherApprovals(List<String> otherApprovals) {
		this.otherApprovals = otherApprovals;
	}
	
}

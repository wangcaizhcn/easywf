package com.easywf.wf.process.vo;

import java.util.ArrayList;
import java.util.List;

import com.easywf.wf.util.context.ApproveBaseContext;
import com.easywf.wf.util.context.ProcessBaseContext;

/**
 * 待办项对象
 * @author wangcai
 * @version 1.0
 */
public class ProcessTodoVO extends ProcessBaseContext{
	
	// 发起人姓名
	private String promoterName;
	
	// 工作项ID
	private String workitemId;
	
	// 当前审批节点
	private String itemName;
	
	// 当前工作项的审批情况，包含已审批和未审批的信息
	private List<ApproveBaseContext> approver = new ArrayList<>();
	
	// 审批项ID
	private String approveId;

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * 获取当前工作项全部的审批情况<br>
	 * 包括已审核的和未审核的<br>
	 * 如果只需要获取当前未审核的用户，建议直接调用 {@link ProcessTodoVO#getTodoUserId()}、{@link ProcessTodoVO#getTodoUserName()}<br>
	 * 也可以通过 {@link ApproveBaseContext#getApproveTime()} == null 判断未审核用户
	 * @return 当前工作项全部的审批情况
	 */
	public List<ApproveBaseContext> getApprover() {
		return approver;
	}

	public void setApprover(List<ApproveBaseContext> approver) {
		this.approver = approver;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}

	/**
	 * 获取未办理的审批人ID集合
	 * @return 未办理的审批人ID集合
	 */
	public List<String> getTodoUserId(){
		List<String> list = new ArrayList<>();
		for(ApproveBaseContext approve : approver) {
			if(approve.getApproveTime() == null) {
				list.add(approve.getApproverId());
			}
		}
		return list;
	}
	
	/**
	 * 获取未办理的审批人姓名集合
	 * @return 未办理的审批人姓名集合
	 */
	public List<String> getTodoUserName(){
		List<String> list = new ArrayList<>();
		for(ApproveBaseContext approve : approver) {
			if(approve.getApproveTime() == null) {
				list.add(approve.getApproverName());
			}
		}
		return list;
	}
}

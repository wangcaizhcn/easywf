package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easywf.wf.template.constant.WFApprovalMode;
import com.easywf.wf.template.constant.WFApprovalPassType;

/**
 * 待审批项上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ApprovePendingItemContext extends WorkitemBaseContext{

	// 审批者ID
	private List<String> approver = new ArrayList<>();
	
	// 转办信息， Key = 正常审批人， value = 转办后的审批人
	private Map<String, String> transfer = new HashMap<>();
		
	// 审批模式，竞争办理、多人办理
	private WFApprovalMode approvalMode;
	
	// 通过方式，单人通过、半数通过等
	private WFApprovalPassType approvalPassType;

	/**
	 * 获取待审批用户ID集合
	 * @return 待审批用户ID集合
	 */
	public List<String> getApprover() {
		return approver;
	}

	/**
	 * 设置待审批用户集合
	 * @param approver 待审批用户ID集合
	 */
	public void setApprover(List<String> approver) {
		this.approver = approver;
	}

	/**
	 * 获取节点的审批模式
	 * @return 审批模式
	 */
	public WFApprovalMode getApprovalMode() {
		return approvalMode;
	}

	/**
	 * 设置节点的审批模式
	 * @param approvalMode 审批模式
	 */
	public void setApprovalMode(WFApprovalMode approvalMode) {
		this.approvalMode = approvalMode;
	}

	/**
	 * 获取节点的通过类型
	 * @return 通过类型
	 */
	public WFApprovalPassType getApprovalPassType() {
		return approvalPassType;
	}

	/**
	 * 设置节点的通过类型
	 * @param approvalPassType 通过类型
	 */
	public void setApprovalPassType(WFApprovalPassType approvalPassType) {
		this.approvalPassType = approvalPassType;
	}

	public Map<String, String> getTransfer() {
		return transfer;
	}

	public void setTransfer(Map<String, String> transfer) {
		this.transfer = transfer;
	}
	
}

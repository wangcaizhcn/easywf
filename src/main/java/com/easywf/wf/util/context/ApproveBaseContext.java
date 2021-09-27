package com.easywf.wf.util.context;

import java.util.Date;

import com.easywf.wf.process.constant.WFWorkitemApproveResult;

/**
 * 审批明细基础上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ApproveBaseContext {
	
	// 审批人ID
	private String approverId;
	
	// 审批人
	private String approverName;
	
	// 审批时间
	private Date approveTime;
	
	// 审批结果
	private WFWorkitemApproveResult result;
	
	// 审批意见
	private String opinions;

	/**
	 * 获取审批人ID
	 * @return 审批人ID
	 */
	public String getApproverId() {
		return approverId;
	}

	/**
	 * 设置审批人ID
	 * @param approverId 审批人ID
	 */
	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	/**
	 * 获取审批时间
	 * @return 审批时间
	 */
	public Date getApproveTime() {
		return approveTime;
	}

	/**
	 * 设置审批时间
	 * @param approveTime 审批时间
	 */
	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	/**
	 * 获取审批结果
	 * @return 审批结果
	 */
	public WFWorkitemApproveResult getResult() {
		return result;
	}

	/**
	 * 设置审批结果
	 * @param result 审批结果
	 */
	public void setResult(WFWorkitemApproveResult result) {
		this.result = result;
	}

	/**
	 * 获取审批意见
	 * @return 审批意见
	 */
	public String getOpinions() {
		return opinions;
	}

	/**
	 * 设置审批意见
	 * @param opinions 审批意见
	 */
	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}

	/**
	 * 获取审批人姓名
	 * @return 审批人姓名
	 */
	public String getApproverName() {
		return approverName;
	}

	/**
	 * 设置审批人姓名
	 * @param approverName 审批人姓名
	 */
	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

}

package com.easywf.wf.process.vo;

import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;

/**
 * 流程状态对象
 * @author wangcai
 * @version 1.0
 */
public class ProcessStateVO {

	// 流程模板ID
	private String templateId;
	
	// 流程实例ID
	private String processId;
	
	// 业务系统记录ID
	private String recordId;
	
	// 流程状态
	private WFProcessState state;
	
	// 流程结束审批状态
	private WFProcessApproveState approveState;

	/**
	 * 获取流程实例ID
	 * @return 流程实例ID
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * 设置流程实例ID
	 * @param processId 流程实例ID
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * 获取流程状态
	 * @return 流程状态
	 */
	public WFProcessState getState() {
		return state;
	}

	/**
	 * 设置流程状态
	 * @param state 流程状态
	 */
	public void setState(WFProcessState state) {
		this.state = state;
	}

	/**
	 * 获取流程审批状态，当流程处于结束状态 
	 * {@link com.easywf.wf.process.constant.WFProcessState#FINISH}
	 * 时，才能获取到该审批状态
	 * @return 流程审批状态
	 */
	public WFProcessApproveState getApproveState() {
		return approveState;
	}

	/**
	 * 设置流程审批状态，当流程处于结束状态 
	 * {@link com.easywf.wf.process.constant.WFProcessState#FINISH}
	 * 时，才设置审批状态
	 * @param approveState 流程审批状态
	 */
	public void setApproveState(WFProcessApproveState approveState) {
		this.approveState = approveState;
	}

	/**
	 * 获取流程模板ID
	 * @return 流程模板ID
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置流程模板ID
	 * @param templateId 流程模板ID
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 获取业务系统记录ID
	 * @return 业务系统记录ID
	 */
	public String getRecordId() {
		return recordId;
	}

	/**
	 * 设置业务系统记录ID
	 * @param recordId 业务系统记录ID
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
}

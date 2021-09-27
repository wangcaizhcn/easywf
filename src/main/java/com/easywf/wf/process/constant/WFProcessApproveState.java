package com.easywf.wf.process.constant;

/**
 * 流程审核状态
 * @author wangcai
 * @version 1.0
 */
public enum WFProcessApproveState {
	/**
	 * 流程通过
	 */
	PASS, 
	/**
	 * 流程不通过
	 */
	FAILED, 
	/**
	 * 流程驳回
	 */
	REJECT
}

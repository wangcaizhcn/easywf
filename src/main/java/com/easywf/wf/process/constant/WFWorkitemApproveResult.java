package com.easywf.wf.process.constant;

/**
 * 审批结果
 * @author wangcai
 * @version 1.0
 */
public enum WFWorkitemApproveResult {
	/**
	 * 通过
	 */
	PASS, 
	/**
	 * 不通过
	 */
	FAILED, 
	/**
	 * 驳回
	 */
	REJECT, 
	/**
	 * 停止
	 */
	STOP, 
	/**
	 * 挂起
	 */
	HANGUP
}

package com.easywf.wf.process.constant;

/**
 * 流程状态
 * @author wangcai
 * @version 1.0
 */
public enum WFProcessState {
	/**
	 * 创建状态
	 */
	CREATE, 
	/**
	 * 正常运行中
	 */
	NORMAL, 
	/**
	 * 流程终止
	 */
	TERMINATE, 
	/**
	 * 流程结束
	 */
	FINISH, 
	/**
	 * 流程驳回
	 */
	REJECT, 
	/**
	 * 流程挂起
	 */
	HANGUP
}

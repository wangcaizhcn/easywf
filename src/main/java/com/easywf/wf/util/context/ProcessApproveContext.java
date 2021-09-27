package com.easywf.wf.util.context;

import com.easywf.wf.process.constant.WFProcessApproveState;

/**
 * 流程审批的上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ProcessApproveContext extends ProcessBaseContext {

	// 流程状态， 启动
	public static final String STATE_START = "start";
	
	// 流程状态， 审批中
	public static final String STATE_APPROVE = "approve";
	
	// 流程状态， 结束
	public static final String STATE_FINISH = "finish";
	
	// 流程状态
	private String state;
	
	// 流程结束后审批结论
	private WFProcessApproveState result;

	//TODO 考虑下流程参数的返回
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public WFProcessApproveState getResult() {
		return result;
	}

	public void setResult(WFProcessApproveState result) {
		this.result = result;
	}
	
}

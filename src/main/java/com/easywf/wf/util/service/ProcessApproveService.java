package com.easywf.wf.util.service;

import com.easywf.wf.util.context.ApproveContext;

/**
 * 业务系统监听的流程审批接口
 * @author wangcai
 * @version 1.0
 */
public interface ProcessApproveService {

	void handle(ApproveContext pc);
	
	boolean support(ApproveContext pc);
}

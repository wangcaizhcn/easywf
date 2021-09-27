package com.easywf.wf.util.service;

import com.easywf.wf.util.context.HangupContext;

/**
 * 终止流程回调业务方法
 * @author wangcai
 *
 */
public interface ProcessTerminateService {

	// TODO 上下文对象需要变名
	void terminate(HangupContext pc);
	
	boolean support(HangupContext pc);
}

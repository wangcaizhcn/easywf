package com.easywf.wf.util.service;

import com.easywf.wf.util.context.ProcessContext;

/**
 * 自定义判断工作项是否可以通过<br>
 * 用在审批节点、多人审批下
 * @author wangcai
 * @version 1.0
 */
public interface WorkitemPassService {

	boolean pass(ProcessContext pc);
}

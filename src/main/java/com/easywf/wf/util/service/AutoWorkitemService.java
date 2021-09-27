package com.easywf.wf.util.service;

import com.easywf.wf.util.context.ProcessContext;

/**
 * 自动节点的实现类接口
 * @author wangcai
 * @version 1.0
 */
public interface AutoWorkitemService {

	// TODO 接口与逻辑调用地方，考虑下是否注入回调服务。
	
	/**
	 * 执行实现类的功能
	 * @param pc 流程上线文
	 * @return true:流程继续执行， false:流程停止不动<br>
	 * 通常情况都是执行一些计算，如果某种情况下，经过计算需要终止、挂起流程等，请调用对应方法，然后返回false
	 	最好是返回一个服务接口实现类，流程回调，或者传入实现类，供业务调用
	 */
	boolean run(ProcessContext pc);
}

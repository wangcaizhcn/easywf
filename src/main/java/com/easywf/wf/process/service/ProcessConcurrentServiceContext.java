package com.easywf.wf.process.service;

/**
 * 流程上下文服务类
 * @author wangcai
 * @version 1.0
 */
public class ProcessConcurrentServiceContext {

	private static ThreadLocal<String> concurrentId = new ThreadLocal<String>();
	
	public static void setConcurrentId(String concurrentId) {
		ProcessConcurrentServiceContext.concurrentId.set(concurrentId);
	}
	
	public static void init(){
		concurrentId.set(null);
	}
	
	public static String getConcurrentId() {
		return concurrentId.get();
	}
	
}

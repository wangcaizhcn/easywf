package com.easywf.wf.process.service;

import com.easywf.wf.util.context.ApproveContext;

public class ApproveServiceContext {

	private static ThreadLocal<ApproveContext> approveContext = new ThreadLocal<ApproveContext>();
	
	public static void setApproveContext(ApproveContext approveContext) {
		ApproveServiceContext.approveContext.set(approveContext);
	}
	
	public static ApproveContext getApproveContext() {
		if(approveContext.get() == null) {
			approveContext.set(new ApproveContext());
		}
		return approveContext.get();
	}
	
	public static void init(){
		approveContext.set(null);
	}
}

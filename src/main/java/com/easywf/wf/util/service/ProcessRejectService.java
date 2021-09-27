package com.easywf.wf.util.service;

import com.easywf.wf.util.context.ProcessContext;

public interface ProcessRejectService {

	void reject(ProcessContext pc);
	
	boolean support(ProcessContext pc);
}

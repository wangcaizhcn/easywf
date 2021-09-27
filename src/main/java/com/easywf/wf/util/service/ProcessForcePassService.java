package com.easywf.wf.util.service;

import com.easywf.wf.util.context.HangupContext;

public interface ProcessForcePassService {

	void forcePass(HangupContext hc);
	
	boolean support(HangupContext hc);
}

package com.easywf.wf.util.service;

import com.easywf.wf.util.context.HangupContext;

public interface ProcessHangupService {

	void hangup(HangupContext hc);
	
	void wakeup(HangupContext hc);
	
	boolean support(HangupContext hc);
}

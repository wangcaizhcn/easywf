package com.easywf.wf.util.service;

import com.easywf.wf.util.context.TransferContext;

public interface ProcessTransferService {

	void transfer(TransferContext tc);
	
	boolean support(TransferContext tc);
	
}

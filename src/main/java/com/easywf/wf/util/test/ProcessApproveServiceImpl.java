package com.easywf.wf.util.test;

import org.springframework.stereotype.Service;

import com.easywf.wf.util.context.ApproveContext;
import com.easywf.wf.util.service.ProcessApproveService;

@Service
public class ProcessApproveServiceImpl implements ProcessApproveService{

	@Override
	public void handle(ApproveContext pc) {
		// TODO Auto-generated method stub
		System.out.println("==========");
	}

	@Override
	public boolean support(ApproveContext pc) {
		// TODO Auto-generated method stub
		return true;
	}

}

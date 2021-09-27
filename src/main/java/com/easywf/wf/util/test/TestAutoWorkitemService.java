package com.easywf.wf.util.test;

import org.springframework.stereotype.Service;

import com.easywf.wf.util.context.ProcessContext;
import com.easywf.wf.util.service.AutoWorkitemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("TestAutoWorkitemService")
public class TestAutoWorkitemService implements AutoWorkitemService {

	@Override
	public boolean run(ProcessContext pc) {
		
//		ObjectMapper mapper = new ObjectMapper();
		System.out.println("========自动节点==========" + pc.getProcessId() + "->" + pc.getWorkitemId());
		// TODO Auto-generated method stub
//		try {
//			System.out.println("========自动节点==========" + mapper.writeValueAsString(pc));
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return true;
	}

}

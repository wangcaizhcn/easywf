package com.easywf.wf.util.test;

import org.springframework.stereotype.Service;

import com.easywf.wf.util.context.ProcessContext;
import com.easywf.wf.util.service.ProcessRejectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FinishTest implements ProcessRejectService {


	@Override
	public boolean support(ProcessContext pc) {
		return true;
	}

	@Override
	public void reject(ProcessContext pc) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			String jsonStr = mapper.writeValueAsString(pc);
			System.out.println(jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

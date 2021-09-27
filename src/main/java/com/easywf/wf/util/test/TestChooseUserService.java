package com.easywf.wf.util.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.easywf.wf.util.context.ApproveUserContext;
import com.easywf.wf.util.service.ApproveUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("TestChooseUserService")
public class TestChooseUserService implements ApproveUserService{

	@Override
	public List<String> findUsers(ApproveUserContext auc) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			String jsonStr = mapper.writeValueAsString(auc);
			System.out.println(jsonStr);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> users = new ArrayList<>();
		users.add("11"); // 主岗秘书
		
		return users;
	}

}

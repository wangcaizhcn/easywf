package com.easywf.wf.process.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easywf.wf.util.context.WorkitemBaseContext;

public class ApprovePendingQueryVO extends WorkitemBaseContext{

	private List<Map<String, String>> approvers = new ArrayList<>();

	public List<Map<String, String>> getApprovers() {
		return approvers;
	}
	
	public void addApprover(String userId, String userName) {
		Map<String, String> user = new HashMap<>();
		user.put("id", userId);
		user.put("name", userName);
		approvers.add(user);
	}
}

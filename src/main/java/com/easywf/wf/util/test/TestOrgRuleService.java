package com.easywf.wf.util.test;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.easywf.wf.process.service.OrgRuleService;

@Primary
@Service
public class TestOrgRuleService implements OrgRuleService {

	@Override
	public List<String> allParentId(String orgId) {
		// TODO Auto-generated method stub
		return null;
	}

}

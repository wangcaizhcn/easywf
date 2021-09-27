package com.easywf.wf.process.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.easywf.wf.resources.entity.OrgEntity;
import com.easywf.wf.resources.repository.OrgJpaRepo;

//@Configuration
//@ConditionalOnProperty(prefix="easywf", name = "org-rule", havingValue = "base-org-rule")
public class WFBaseOrgRuleServiceProvider implements OrgRuleService {

	@Autowired
	@Qualifier("wfOrgJpaRepo")
	private OrgJpaRepo orgJpaRepo;
	
	/**
	 * 不提供path属性，则直接用parentId依次获取
	 */
	@Override
	public List<String> allParentId(String orgId) {
		
		List<OrgEntity> allOrgs = orgJpaRepo.findAll();
		Map<String, OrgEntity> orgMap = new HashMap<>();
		for(OrgEntity org : allOrgs) {
			orgMap.put(org.getId(), org);
		}
		List<String> orgIdList = new ArrayList<>();
		getParentId(orgIdList, orgId, orgMap);
		return orgIdList;
	}

	private void getParentId(List<String> result, String orgId, Map<String, OrgEntity> map) {
		if(!result.contains(orgId)) {
			result.add(orgId);
		}
		
		if(map.containsKey(orgId) && map.containsKey(map.get(orgId).getParentId())) {
			getParentId(result, map.get(orgId).getParentId(), map);
		}
	}
}

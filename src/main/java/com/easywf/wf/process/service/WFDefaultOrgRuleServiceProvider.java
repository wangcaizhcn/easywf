package com.easywf.wf.process.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import com.easywf.wf.resources.entity.OrgEntity;
import com.easywf.wf.resources.repository.OrgJpaRepo;

/**
 * 工作流默认的组织机构接口实现类
 * @author wangcai
 * @version 1.0
 */
//@Configuration
//@ConditionalOnBean(OrgRuleService.class)
public class WFDefaultOrgRuleServiceProvider implements OrgRuleService {

	@Autowired
	@Qualifier("wfOrgJpaRepo")
	private OrgJpaRepo orgJpaRepo;
	
	/**
	 * 依照组织机构表的path字段获取上级组织机构
	 */
	@Override
	public List<String> allParentId(String orgId) {
		List<String> orgIdList = new ArrayList<>();
		orgIdList.add(orgId);
		List<OrgEntity> orgs = orgJpaRepo.findByIdIn(orgIdList);
		for(OrgEntity org : orgs) {
			String[] orgIds = org.getPath().split("/");
			for(String id : orgIds) {
				if(StringUtils.isNotBlank(id)) {
					orgIdList.add(id);
				}
			}
		}
		return orgIdList;
	}
}

package com.easywf.wf.process.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrgRuleConfiguration {

	// TODO Autowired 怎么处理，应该自动好使
	
	@Bean
	@ConditionalOnProperty(prefix="easywf", name = "org-rule", havingValue = "base-org-rule")
	public OrgRuleService baseOrgRuleService() {
		return new WFBaseOrgRuleServiceProvider();
	}
	
	@Bean
	@ConditionalOnMissingBean(OrgRuleService.class)
	public OrgRuleService defaultOrgRuleService() {
		return new WFDefaultOrgRuleServiceProvider();
	}
	
	//lazyload 方式加载报表，分块
	// 配sql， 配置service方式抽取数据
	
}

package com.easywf.wf.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;

public interface WFTemplateNodeSettingJpaRepo extends JpaRepository<WFTemplateNodeSettingEntity, String>{
	
	WFTemplateNodeSettingEntity findByNodeId(String nodeId);
	
}

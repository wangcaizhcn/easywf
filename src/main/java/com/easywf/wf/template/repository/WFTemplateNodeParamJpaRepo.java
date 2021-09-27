package com.easywf.wf.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.template.entity.WFTemplateNodeParamEntity;

public interface WFTemplateNodeParamJpaRepo extends JpaRepository<WFTemplateNodeParamEntity, String>{
	
	List<WFTemplateNodeParamEntity> findByNodeId(String nodeId);
}

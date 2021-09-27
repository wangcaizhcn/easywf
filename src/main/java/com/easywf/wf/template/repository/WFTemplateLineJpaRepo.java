package com.easywf.wf.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.template.entity.WFTemplateLineEntity;

public interface WFTemplateLineJpaRepo extends JpaRepository<WFTemplateLineEntity, String>{
	
	List<WFTemplateLineEntity> findByBeginNodeIdOrderByPriority(String beginNodeId);
	
	List<WFTemplateLineEntity> findByEndNodeId(String endNodeId);
	
	List<WFTemplateLineEntity> findByTemplateIdOrderByBeginNodeId(String templateId);
	
	List<WFTemplateLineEntity> findByTemplateId(String templateId);
}

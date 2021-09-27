package com.easywf.wf.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.template.entity.WFTemplateNodeEntity;

public interface WFTemplateNodeJapRepo extends JpaRepository<WFTemplateNodeEntity, String>{
	
	List<WFTemplateNodeEntity> findByTemplateIdOrderByLevelAsc(String templateId);
	
	List<WFTemplateNodeEntity> findByTemplateIdAndTypeOrderByLevelAsc(String templateId, String type);
}

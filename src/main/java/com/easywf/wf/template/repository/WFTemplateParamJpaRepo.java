package com.easywf.wf.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easywf.wf.template.entity.WFTemplateParamEntity;

public interface WFTemplateParamJpaRepo extends JpaRepository<WFTemplateParamEntity, String>{
	
	WFTemplateParamEntity findByTemplateIdAndProperty(@Param("templateId") String templateId, @Param("property") String property);
	
	List<WFTemplateParamEntity> findByTemplateId(String templateId);
	
	List<WFTemplateParamEntity> findByIdIn(List<String> ids);
}

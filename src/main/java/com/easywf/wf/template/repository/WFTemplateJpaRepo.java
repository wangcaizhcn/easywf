package com.easywf.wf.template.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easywf.wf.template.entity.WFTemplateEntity;

public interface WFTemplateJpaRepo extends JpaRepository<WFTemplateEntity, String>{

	List<WFTemplateEntity> findByState(String state);
	
	@Query(nativeQuery = true, value = "SELECT * FROM easy_wf_template order by CONVERT(NAME USING gbk)")
	List<WFTemplateEntity> findAllOrderByName();
	
	@Query(nativeQuery = true, value = "SELECT MAX(VERSION) FROM easy_wf_template WHERE template_id = :templateId")
	Integer findMaxVersion(@Param("templateId") String templateId);
	
	//根据模板ID，按照最高版本号排序
	List<WFTemplateEntity> findByBaseTemplateIdOrderByVersionDesc(String templateId);
}

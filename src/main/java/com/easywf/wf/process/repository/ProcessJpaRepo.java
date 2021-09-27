package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.ProcessEntity;

public interface ProcessJpaRepo extends JpaRepository<ProcessEntity, String>{
	
	List<ProcessEntity> findByPromoterOrderByCreateTimeDesc(String promoter);
	
	List<ProcessEntity> findByPromoterAndTemplateIdOrderByCreateTimeDesc(String promoter, String templateId);

//	List<ProcessEntity> findByPromoterAndTemplateIdAndProcessStateAndApproveType(String promoter, String templateId, String processState, String approveType, Pageable page);
//	
//	List<ProcessEntity> findByPromoterAndTemplateIdAndProcessState(String promoter, String templateId, String processState, Pageable page);
//	
//	List<ProcessEntity> findByPromoterAndTemplateId(String promoter, String templateId, Pageable page);
}

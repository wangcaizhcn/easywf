package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.ProcessParamEntity;

public interface ProcessParamJpaRepo extends JpaRepository<ProcessParamEntity, String>{
	
	List<ProcessParamEntity> findByProcessId(String processId);
	
	ProcessParamEntity findByProcessIdAndProperty(String processId, String property);
}

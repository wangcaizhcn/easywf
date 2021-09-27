package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.ProcessConcurrentEntity;

public interface ProcessConcurrentJpaRepo extends JpaRepository<ProcessConcurrentEntity, String>{
	
	List<ProcessConcurrentEntity> findByWorkitemId(String workitemId);
}

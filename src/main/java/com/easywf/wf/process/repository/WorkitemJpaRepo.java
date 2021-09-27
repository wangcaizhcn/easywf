package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.WorkitemEntity;

public interface WorkitemJpaRepo extends JpaRepository<WorkitemEntity, String>{
	
	List<WorkitemEntity> findByProcessId(String processId);
	
	List<WorkitemEntity> findByConcurrentId(String concurrentId);
	
	List<WorkitemEntity> findByProcessIdAndTypeOrderByCreateTimeAsc(String processId, String type);
}

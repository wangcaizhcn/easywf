package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.ProcessConcurrentParamEntity;

public interface ProcessConcurrentParamJpaRepo extends JpaRepository<ProcessConcurrentParamEntity, String>{

	List<ProcessConcurrentParamEntity> findByConcurrentId(String concurrentId);
}

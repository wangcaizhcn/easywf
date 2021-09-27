package com.easywf.wf.process.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.WorkitemTransferEntity;

public interface WorkitemTransferJpaRepo extends JpaRepository<WorkitemTransferEntity, String>{
	
}

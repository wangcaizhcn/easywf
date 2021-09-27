package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.easywf.wf.process.entity.ProcessHangupEntity;

public interface ProcessHangupJpaRepo extends JpaRepository<ProcessHangupEntity, String>{
	
	List<ProcessHangupEntity> findByProcessId(String processId);
}

package com.easywf.wf.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easywf.wf.resources.entity.UserOrgEntity;

@Repository("wfUserOrgJpaRepo")
public interface UserOrgJpaRepo extends JpaRepository<UserOrgEntity, String>{
	
	List<UserOrgEntity> findByUserId(String userId);
}

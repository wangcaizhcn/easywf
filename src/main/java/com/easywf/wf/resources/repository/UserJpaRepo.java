package com.easywf.wf.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easywf.wf.resources.entity.UserEntity;

@Repository("wfUserJpaRepo")
public interface UserJpaRepo extends JpaRepository<UserEntity, String>{

	List<UserEntity> findByIdIn(List<String> ids);
}

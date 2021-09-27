package com.easywf.wf.resources.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.easywf.wf.resources.entity.RoleEntity;
import com.easywf.wf.resources.entity.UserEntity;
import com.easywf.wf.resources.repository.RoleJpaRepo;
import com.easywf.wf.resources.repository.UserJpaRepo;

@Service
public class WFResourcesService {

	@Autowired
	@Qualifier("wfRoleJpaRepo")
	private RoleJpaRepo roleJpaRepo;
	
	@Autowired
	@Qualifier("wfUserJpaRepo")
	private UserJpaRepo userJpaRepo;
	
	public List<RoleEntity> getRoleList(){
		return roleJpaRepo.findAll();
	}
	
	public List<UserEntity> getUserList(){
		return userJpaRepo.findAll();
	}
	
	public RoleEntity findRole(String roleId) {
		return roleJpaRepo.findById(roleId).orElseGet(() -> {return null;});
	}
}

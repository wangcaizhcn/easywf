package com.easywf.wf.resources.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easywf.wf.resources.entity.RoleEntity;

@Repository("wfRoleJpaRepo")
public interface RoleJpaRepo extends JpaRepository<RoleEntity, String>{

}

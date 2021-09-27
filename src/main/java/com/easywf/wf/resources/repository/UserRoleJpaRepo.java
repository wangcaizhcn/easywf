package com.easywf.wf.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easywf.wf.resources.entity.UserRoleEntity;

@Repository("wfUserRoleJpaRepo")
public interface UserRoleJpaRepo extends JpaRepository<UserRoleEntity, String>{

	List<UserRoleEntity> findByRoleId(String roleId);
	
	List<UserRoleEntity> findByRoleIdIn(List<String> roleIds);
	
	@Query("select distinct e from UserRoleEntity e, UserOrgEntity uo where uo.orgId in :deptIds and e.roleId in :roleIds and e.userId = uo.userId")
	List<UserRoleEntity> findOrgRole(@Param("deptIds") List<String> deptIds, @Param("roleIds") List<String> roleIds);
}

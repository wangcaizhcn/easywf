package com.easywf.wf.resources.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easywf.wf.resources.entity.OrgEntity;

@Repository("wfOrgJpaRepo")
public interface OrgJpaRepo extends JpaRepository<OrgEntity, String>{

	List<OrgEntity> findByIdIn(List<String> ids);
}

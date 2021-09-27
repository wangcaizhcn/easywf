package com.easywf.wf.transfer.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easywf.wf.transfer.entity.WFAutoTransferSettingEntity;

public interface WFAutoTransferSettingJpaRepo extends JpaRepository<WFAutoTransferSettingEntity, String>{
	
	List<WFAutoTransferSettingEntity> findByApproverIdOrderByBeginTimeDesc(String userId);
	
	List<WFAutoTransferSettingEntity> findByIdIn(List<String> idList);
	
	@Query("select e from WFAutoTransferSettingEntity e where e.approverId = :approverId and :date between e.beginTime and e.endTime order by e.updateTime desc")
	List<WFAutoTransferSettingEntity> findTransferSetting(@Param("approverId") String approverId, @Param("date") Date date);
}

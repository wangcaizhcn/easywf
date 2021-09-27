package com.easywf.wf.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easywf.wf.process.entity.WorkitemApproverEntity;

public interface WorkitemApproverJpaRepo extends JpaRepository<WorkitemApproverEntity, String>{
	
	@Query("select e from WorkitemApproverEntity e where e.approveResult IS not NULL and e.approveResult != '' and e.workitemId in :workitemIds order by e.approveTime")
	List<WorkitemApproverEntity> findApproveDetail(@Param("workitemIds") List<String> workitemIds);
	
	WorkitemApproverEntity findByWorkitemIdAndApprover(String workitemId, String approver);
	
	List<WorkitemApproverEntity> findByWorkitemIdIn(List<String> workitemIds);
	
	List<WorkitemApproverEntity> findByWorkitemIdOrderByApproveTimeAsc(String workitemId);
	
	@Query(nativeQuery = true, value = "SELECT t2.name, t1.approver FROM easy_wf_workitem t, "
			+ " easy_wf_workitem_approver t1, easy_wf_template_node t2 "
			+ " WHERE t.id = t1.workitem_id AND t.pass_time IS NULL AND (t1.approve_result IS NULL OR t1.approve_result = '')"
			+ " AND t.template_node_id = t2.id AND t.process_id = :processId")
	List<Object[]> findNotApprove(@Param("processId") String processId);
	
	@Query("SELECT e FROM WorkitemApproverEntity e WHERE e.approver=:userId AND (e.approveResult IS NULL OR e.approveResult = '')")
	List<WorkitemApproverEntity> findmyDB(@Param("userId") String userId);
	
	@Query(nativeQuery = true, value = "SELECT approver FROM easy_wf_workitem_approver WHERE workitem_id IN :workitemIds "
			+ "AND approve_result IN('PASS', 'FAILED') ORDER BY approve_time DESC LIMIT 1")
	String findLastApprover(@Param("workitemIds") List<String> workitemIds);
	
	@Query("select e from WorkitemApproverEntity e, WorkitemEntity w where e.workitemId = w.id and w.processId = :processId AND w.handleMode = 'APPROVAL' AND e.approveResult='PASS' AND e.approver = :userId")
	List<WorkitemApproverEntity> getUserAlreadyApprove(@Param("processId") String processId, @Param("userId") String userId);
}

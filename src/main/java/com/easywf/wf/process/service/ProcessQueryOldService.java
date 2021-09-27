package com.easywf.wf.process.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;
import com.easywf.wf.process.constant.WFWorkitemApproveResult;
import com.easywf.wf.process.entity.ProcessEntity;
import com.easywf.wf.process.entity.ProcessHangupEntity;
import com.easywf.wf.process.entity.WorkitemApproverEntity;
import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.process.repository.ProcessHangupJpaRepo;
import com.easywf.wf.process.repository.ProcessJpaRepo;
import com.easywf.wf.process.repository.WorkitemApproverJpaRepo;
import com.easywf.wf.process.repository.WorkitemJpaRepo;
import com.easywf.wf.process.vo.ApproveDetailVO;
import com.easywf.wf.process.vo.ApprovePendingQueryVO;
import com.easywf.wf.process.vo.ProcessDetailVO;
import com.easywf.wf.process.vo.ProcessListQuery;
import com.easywf.wf.process.vo.ProcessListQueryReturnVO;
import com.easywf.wf.process.vo.ProcessListVO;
import com.easywf.wf.process.vo.ProcessStateVO;
import com.easywf.wf.process.vo.ProcessTodoVO;
import com.easywf.wf.resources.entity.UserEntity;
import com.easywf.wf.resources.repository.UserJpaRepo;
import com.easywf.wf.template.constant.WFNodeHandleMode;
import com.easywf.wf.template.constant.WFNodeType;
import com.easywf.wf.template.entity.WFTemplateEntity;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.repository.WFTemplateNodeJapRepo;
import com.easywf.wf.template.repository.WFTemplateJpaRepo;
import com.easywf.wf.util.context.ApproveBaseContext;
import com.easywf.wf.util.context.ApprovedItemContext;

/**
 * 流程相关查询接口
 * @author wangcai
 * @version 1.0
 */
@Service
public class ProcessQueryOldService {

	@Autowired
	private ProcessJpaRepo processJpaRepo;
	
	@Autowired
	private WFTemplateJpaRepo wfTemplateJpaRepo;
	
	@Autowired
	private ProcessHangupJpaRepo processHangupJpaRepo;
	
	@Autowired
	private WorkitemJpaRepo workitemJpaRepo;
	
	@Autowired
	private WorkitemApproverJpaRepo workitemApproverJpaRepo;
	
	@Autowired
	private WFTemplateNodeJapRepo wfTemplateNodeJapRepo;
	
	@Autowired
	@Qualifier("wfUserJpaRepo")
	private UserJpaRepo userJpaRepo;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private WFTemplateNodeJapRepo wfTemplateItemJapRepo;
	
	/**
	 * 获取流程状态
	 * @param processId 流程实例ID
	 * @return 流程状态<br>
	 * 当流程不存在时，返回null<br>
	 * 流程处于结束状态{@link com.easywf.wf.process.constant.WFProcessState#FINISH}，会返回流程的审批状态
	 */
	public ProcessStateVO getProcessState(String processId) {
		ProcessEntity process = processJpaRepo.findById(processId).orElseGet(() -> {return null;});
		return process2StateVO(process);
	}
	
	
	/**
	 * 通过给定的流程ID集合，批量获取这些流程的状态信息
	 * @param processIds 流程ID集合
	 * @return 流程状态信息<br>
	 * 流程处于结束状态{@link com.easywf.wf.process.constant.WFProcessState#FINISH}，会返回流程的审批状态<br>
	 * 若传入的是空集合，或者对应的流程均不存在，则返回空列表（即list.size() == 0），而不是null
	 */
	public List<ProcessStateVO> getProcessStateBatch(List<String> processIds){
		List<ProcessStateVO> list = new ArrayList<>();
		
		if(CollectionUtils.isEmpty(processIds)) {
			return list;
		}
		
		List<ProcessEntity> processList = processJpaRepo.findAllById(processIds);
		for(ProcessEntity process : processList) {
			list.add(process2StateVO(process));
		}
		
		return list;
	}
	
	/**
	 * 获取发起人某一类流程的状态<br>
	 * @param promoterId 发起人ID
	 * @param templateId 流程模板ID
	 * @return 发起人某一类流程的状态信息<br>
	 * 流程处于结束状态{@link com.easywf.wf.process.constant.WFProcessState#FINISH}，会返回流程的审批状态<br>
	 * 若发起人没有任何流程，则返回空列表（即list.size() == 0），而不是null
	 */
	public List<ProcessStateVO> getApplyProcessState(String promoterId, String templateId){
		List<ProcessEntity> processList = null;
		
		// 查询全部
		if(StringUtils.isBlank(templateId)) {
			processList = processJpaRepo.findByPromoterOrderByCreateTimeDesc(promoterId);
		}else {
			// 在指定流程模板中查找
			processList = processJpaRepo.findByPromoterAndTemplateIdOrderByCreateTimeDesc(promoterId, templateId);
		}
		
		List<ProcessStateVO> list = new ArrayList<>();
		if(CollectionUtils.isEmpty(processList)) {
			return list;
		}
		
		for(ProcessEntity process : processList) {
			list.add(process2StateVO(process));
		}
		return list;
	}
	
	private ProcessStateVO process2StateVO(ProcessEntity process) {
		ProcessStateVO vo = new ProcessStateVO();
		vo.setProcessId(process.getId());
		vo.setState(WFProcessState.valueOf(process.getProcessState()));
		if(WFProcessState.FINISH == vo.getState()) {
			vo.setApproveState(WFProcessApproveState.valueOf(process.getApproveType()));
		}
		vo.setRecordId(process.getRecordId());
		vo.setTemplateId(process.getTemplateId());
		return vo;
	}
	
	/**
	 * 获取发起人全部流程的状态
	 * @param promoterId 流程发起人ID
	 * @return 发起人全部流程的状态信息<br>
	 * 流程处于结束状态{@link com.easywf.wf.process.constant.WFProcessState#FINISH}，会返回流程的审批状态<br>
	 * 若发起人没有任何流程，则返回空列表（即list.size() == 0），而不是null
	 */
	public List<ProcessStateVO> getApplyProcessState(String promoterId){
		return getApplyProcessState(promoterId, "");
	}
	
	/**
	 * 获取待办项列表，只能获取到当前流程处于审批状态的待办项列表<br>
	 * 流程处于其他状态，需要获取的话，请采用getTodoListAllState方法
	 * @param approverId
	 * @return
	 */
	public List<ProcessTodoVO> getTodoList(String approverId) {
		return getTodoList(approverId, "");
	}
	
	/**
	 * 获取指定类型的待办项列表，只获取处于审批状态的待办项列表
	 * @param approverId
	 * @param templateId
	 * @return
	 */
	public List<ProcessTodoVO> getTodoList(String approverId, String templateId){
		
		return null;
	}
	
	/**
	 * 获取审批人的全部代办项数量<br>
	 * 只有流程处于审批中的状态时，才是待办项
	 * @param approverId 审批人ID
	 * @return 代办数量
	 */
	public long countTodoList(String approverId) {
		return countTodoList(approverId, "");
	}
	
	/**
	 * 获取审批人的指定类型的代办数量<br>
	 * 只有流程处于审批中的状态时，才是待办项
	 * @param approverId 审批人ID
	 * @param templateId 流程模板ID
	 * @return 代办数量
	 */
	public long countTodoList(String approverId, String templateId) {
		return 0;
	}
	
	/**
	 * 获取待办项列表，包含全部流程状态
	 * @param approverId
	 * @return
	 */
	public List<ProcessTodoVO> getTodoListAllState(String approverId) {
		return getTodoListAllState(approverId, "");
	}
	
	/**
	 * 获取指定类型的待办项列表，包含全部流程状态
	 * @param approverId
	 * @param templateId
	 * @return
	 */
	public List<ProcessTodoVO> getTodoListAllState(String approverId, String templateId){
		return null;
	}
	
	
	/**
	 * 获取流程列表
	 * @param query 查询对象
	 * @return 查询结果
	 */
	public ProcessListQueryReturnVO getProcessList(ProcessListQuery query) {
		
		ProcessListQueryReturnVO vo = new ProcessListQueryReturnVO();
		vo.setQuery(query);
		
		List<Object> params = new ArrayList<>();
		// 目前template必须传入， TODO 后续支持不传入，查询全部的流程
		String baseQuery = " FROM easy_wf_process t, easy_wf_user t1 WHERE t.promoter = t1.id and t.template_id = ? ";
		params.add(query.getTemplateId());
		
		// 流程状态查询条件
		if(query.getProcessState() != null) {
			baseQuery = baseQuery + " and t.process_state = ? ";
			params.add(query.getProcessState().toString());
		}
		// 流程审批状态查询条件
		if(query.getApproveState() != null) {
			baseQuery = baseQuery + " and t.approve_type = ? ";
			params.add(query.getApproveState().toString());
		}
		
		// 查询发起者的流程
		if(StringUtils.isNotBlank(query.getPromoterId())) {
			baseQuery = baseQuery + " and t1.id = ? ";
			params.add(query.getPromoterId());
		}else if(StringUtils.isNotBlank(query.getPromoterName())) {
			// 根据发起人姓名查询流程
			baseQuery = baseQuery + " AND t1.name LIKE ? ";
			params.add("%" + query.getPromoterName() + "%");
		}
		
		String countSQL = "select count(*) " + baseQuery;
		
		// 查询总条数
		long count = jdbcTemplate.queryForObject(countSQL, params.toArray(), Long.class);
		vo.setCount(count);
		
		List<ProcessListVO> list = new ArrayList<>();
		
		// 总条数大于0，再次执行SQL语句获取行信息
		if(count > 0) {
			// 设置排序和翻页
			baseQuery = baseQuery + " ORDER BY t.create_time DESC LIMIT ?, ? ";
			params.add((query.getPageNum() - 1) * query.getPageSize());
			params.add(query.getPageSize());
			
			String querySQL = "SELECT t.template_id, t.id, t.promoter, t1.name, t.start_time, t.record_id, t.title, " +
					"t.process_state, t.approve_type, t.finish_time, t.reject_time, t.terminate_time " + baseQuery;
			
			SqlRowSet rs = jdbcTemplate.queryForRowSet(querySQL, params.toArray());
			
			WFTemplateEntity template = wfTemplateJpaRepo.findById(query.getTemplateId()).get();
			
			while(rs.next()) {
				ProcessListVO pl = new ProcessListVO();
				pl.setTemplateId(rs.getString("template_id"));
				pl.setProcessId(rs.getString("id"));
				pl.setProcessName(template.getName());
				pl.setPromoter(rs.getString("promoter"));
				pl.setPromoterName(rs.getString("name"));
				pl.setStartTime(rs.getDate("start_time"));
				pl.setRecordId(rs.getString("record_id"));
				pl.setTitle(rs.getString("title"));
				pl.setState(WFProcessState.valueOf(rs.getString("process_state")));
				if(pl.getState() == WFProcessState.FINISH) {
					pl.setApproveState(WFProcessApproveState.valueOf(rs.getString("approve_type")));
					pl.setOptTime(rs.getDate("finish_time"));
				}else if(WFProcessState.TERMINATE == pl.getState()) {
					pl.setOptTime(rs.getDate("terminate_time"));
				} else if(WFProcessState.REJECT == pl.getState()) {
					pl.setOptTime(rs.getDate("reject_time"));
				} else if(WFProcessState.HANGUP == pl.getState()) {
					List<ProcessHangupEntity> hangups = processHangupJpaRepo.findByProcessId(rs.getString("id"));
					for(ProcessHangupEntity hangup : hangups) {
						if(hangup.getWakeupTime() == null) {
							pl.setOptTime(hangup.getHangupTime());
							break;
						}
					}
				}
				
				list.add(pl);
			}
		}
		
		vo.setProcessList(list);
		
		return vo;
	}
	
	/**
	 * 获取我的发起流程列表<br>
	 * 有效的参数为： templateId、processState、promoterId、submitDateBegin、submitDateEnd、finishDateBegin、finishDateEnd、pageSize、pageNum<br>
	 * 其中 发起人ID（promoterId）必须
	 * @param query 参数
	 * @return
	 */
	public ProcessListQueryReturnVO myProcessList(ProcessListQuery query) {
		
		ProcessListQueryReturnVO vo = new ProcessListQueryReturnVO();
		vo.setQuery(query);
		
		String baseSql = " FROM easy_wf_process t WHERE t.promoter = ? ";
		List<Object> params = new ArrayList<>();
		params.add(query.getPromoterId());
		
		// 流程模板查询条件
		if(query.getProcessState() != null) {
			baseSql = baseSql + " and t.template_id = ? ";
			params.add(query.getTemplateId());
		}
		
		// 流程状态查询条件
		if(query.getProcessState() != null) {
			baseSql = baseSql + " and t.process_state = ? ";
			params.add(query.getProcessState().toString());
		}
		
		// 提交开始时间查询条件
		if(query.getSubmitDateBegin() != null) {
			baseSql = baseSql + " and t.start_time >= ? ";
			params.add(query.getSubmitDateBegin());
		}
		// 提交终止时间查询条件
		if(query.getSubmitDateEnd() != null) {
			baseSql = baseSql + " and t.start_time <= ? ";
			params.add(query.getSubmitDateEnd());
		}
		
		// 审批开始时间查询条件
		if(query.getApproveDateBegin() != null) {
			baseSql = baseSql + " and t.finish_time >= ? ";
			params.add(query.getFinishDateBegin());
		}
		// 审批结束时间查询条件
		if(query.getApproveDateEnd() != null) {
			baseSql = baseSql + " and t.finish_time <= ? ";
			params.add(query.getFinishDateEnd());
		}
		
		String countSql = "select count(*) " + baseSql;
		
		// 查询总条数
		long count = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
		vo.setCount(count);
		
		List<ProcessListVO> list = new ArrayList<>();
		
		// 总条数大于0，再次执行SQL语句获取行信息
		if(count > 0) {
			String querySql = "SELECT t.template_id, t.id, t.promoter, t.start_time, t.record_id, t.title, " +
					"t.process_state, t.approve_type, t.finish_time " + baseSql + " ORDER BY t.create_time DESC LIMIT ?, ? ";
			params.add((query.getPageNum() - 1) * query.getPageSize());
			params.add(query.getPageSize());
			
			SqlRowSet rs = jdbcTemplate.queryForRowSet(querySql, params.toArray());
			
//			WFTemplateEntity template = wfTemplateJpaRepo.findOne(query.getTemplateId());
			
			while(rs.next()) {
				ProcessListVO pl = new ProcessListVO();
				pl.setTemplateId(rs.getString("template_id"));
				pl.setProcessId(rs.getString("id"));
				WFTemplateEntity template = wfTemplateJpaRepo.findById(query.getTemplateId()).get();
				pl.setProcessName(template.getName());
				pl.setPromoter(query.getPromoterId());
//				pl.setPromoterName(rs.getString("name"));
				pl.setStartTime(rs.getDate("start_time"));
				pl.setRecordId(rs.getString("record_id"));
				pl.setTitle(rs.getString("title"));
				pl.setState(WFProcessState.valueOf(rs.getString("process_state")));
				if(pl.getState() == WFProcessState.FINISH) {
					pl.setApproveState(WFProcessApproveState.valueOf(rs.getString("approve_type")));
					pl.setOptTime(rs.getDate("finish_time"));
				}
				
				// 找到流程节点
				List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessIdAndTypeOrderByCreateTimeAsc(pl.getProcessId(), WFNodeType.WORK.toString());
				for(WorkitemEntity workitem : workitems) {
					WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(workitem.getTemplateNodeId()).get();
					// 已审批工作项
					if(workitem.getPassTime() != null) {
						
						List<String> workitemIds = new ArrayList<>();
						workitemIds.add(workitem.getId());
						List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findApproveDetail(workitemIds);
						
						ApprovedItemContext approved = new ApprovedItemContext();
						approved.setNodeId(workitem.getTemplateNodeId());
						approved.setName(node.getName());
						approved.setWorkitemId(workitem.getId());
						
						for(WorkitemApproverEntity entry : approves) {
							ApproveBaseContext approveDetail = new ApproveBaseContext();
							approveDetail.setApproverId(entry.getApprover());
							approveDetail.setApproverName(userJpaRepo.findById(entry.getApprover()).get().getName());
							approveDetail.setApproveTime(entry.getApproveTime());
							approveDetail.setOpinions(entry.getOpinions());
							approveDetail.setResult(WFWorkitemApproveResult.valueOf(entry.getApproveResult()));
							
							approved.getApproveInfo().add(approveDetail);
						}
						
						pl.getApproveInfo().add(approved);
					}else {
						// 未审批工作项
						ApprovePendingQueryVO approvePending = new ApprovePendingQueryVO();
						
						approvePending.setNodeId(workitem.getTemplateNodeId());
						approvePending.setName(node.getName());
						approvePending.setWorkitemId(workitem.getId());
						
						List<WorkitemApproverEntity> approvers = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
						for(WorkitemApproverEntity entry : approvers) {
							UserEntity user = userJpaRepo.findById(entry.getApprover()).get();
							if(user == null) {
								System.out.println(entry.getApprover() + "===" + workitem.getId());
							}else {
								approvePending.addApprover(entry.getApprover(), user.getName());
							}
							
						}
						pl.getApprovePendings().add(approvePending);
					}
				}
				
				list.add(pl);
			}
		}
		
		vo.setProcessList(list);
		return vo;	
	}
	
	/**
	 * 获取我的待办项列表<br>
	 * 有效的参数为： templateId、promoterName、submitDateBegin、submitDateEnd、pageSize、pageNum<br>
	 * @param approverId 审批人ID
	 * @param query 查询条件
	 * @return
	 */
	public ProcessListQueryReturnVO myApprovePendingProcessList(String approverId, ProcessListQuery query) {
		
		ProcessListQueryReturnVO vo = new ProcessListQueryReturnVO();
		vo.setQuery(query);
		
		String baseSql = " FROM easy_wf_process p, easy_wf_user u, easy_wf_workitem w, easy_wf_workitem_approver a, easy_wf_template t " +
				" WHERE p.promoter = u.id AND p.process_state = 'NORMAL' /*流程处于审批中*/ AND p.id = w.process_id AND w.id = a.workitem_id "
				+ " AND w.pass_time IS NULL /*进行中的工作项*/ AND a.approver = ? AND p.template_id = t.id ";
		List<Object> params = new ArrayList<>();
		params.add(approverId);
		
		// 流程模板查询条件
		if(query.getTemplateId() != null) {
			baseSql = baseSql + " and p.template_id = ? ";
			params.add(query.getTemplateId());
		}
		
		// 流程发起人查询条件
		if(query.getPromoterName() != null) {
			baseSql = baseSql + " and u.name LIKE ? ";
			params.add("%" + query.getPromoterName() + "%");
		}
		
		// 提交开始时间查询条件
		if(query.getSubmitDateBegin() != null) {
			baseSql = baseSql + " and p.start_time >= ? ";
			params.add(query.getSubmitDateBegin());
		}
		// 提交终止时间查询条件
		if(query.getSubmitDateEnd() != null) {
			baseSql = baseSql + " and p.start_time <= ? ";
			params.add(query.getSubmitDateEnd());
		}
		
		String countSql = "select count(*) " + baseSql;
		
		// 查询总条数
		long count = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
		vo.setCount(count);
		
		List<ProcessListVO> list = new ArrayList<>();
		
		// 总条数大于0，再次执行SQL语句获取行信息
		if(count > 0) {
			String querySql = "SELECT p.id AS process_id, p.template_id, t.name AS template_name, p.promoter, "
					+ "u.name promoter_name, p.start_time,p.record_id, p.title,w.id AS workitem_id " +
					baseSql + " ORDER BY p.start_time DESC LIMIT ?, ? ";
			params.add((query.getPageNum() - 1) * query.getPageSize());
			params.add(query.getPageSize());
			
			SqlRowSet rs = jdbcTemplate.queryForRowSet(querySql, params.toArray());
			while(rs.next()) {
				ProcessListVO pl = new ProcessListVO();
				pl.setTemplateId(rs.getString("template_id"));
				pl.setProcessId(rs.getString("process_id"));
				pl.setProcessName(rs.getString("template_name"));
				pl.setPromoter(rs.getString("promoter"));
				pl.setPromoterName(rs.getString("promoter_name"));
				pl.setStartTime(rs.getDate("start_time"));
				pl.setRecordId(rs.getString("record_id"));
				pl.setTitle(rs.getString("title"));
				pl.setState(WFProcessState.NORMAL);
				
				// 找到流程节点
				List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessIdAndTypeOrderByCreateTimeAsc(pl.getProcessId(), WFNodeType.WORK.toString());
				for(WorkitemEntity workitem : workitems) {
					WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(workitem.getTemplateNodeId()).get();
					// 已审批工作项  待办项的返回，只将已办的工作项信息组装完整即可， 然后单独处理当前办理节点。
					// TODO 如果需求修改成 显示全部办理人， 只需要将下面的else放开即可，并且循环后面的单独处理去掉
					if(workitem.getPassTime() != null) {
						
						List<String> workitemIds = new ArrayList<>();
						workitemIds.add(workitem.getId());
						List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findApproveDetail(workitemIds);
						
						ApprovedItemContext approved = new ApprovedItemContext();
						approved.setNodeId(workitem.getTemplateNodeId());
						approved.setName(node.getName());
						approved.setWorkitemId(workitem.getId());
						
						for(WorkitemApproverEntity entry : approves) {
							ApproveBaseContext approveDetail = new ApproveBaseContext();
							approveDetail.setApproverId(entry.getApprover());
							approveDetail.setApproverName(userJpaRepo.findById(entry.getApprover()).get().getName());
							approveDetail.setApproveTime(entry.getApproveTime());
							approveDetail.setOpinions(entry.getOpinions());
							approveDetail.setResult(WFWorkitemApproveResult.valueOf(entry.getApproveResult()));
							
							approved.getApproveInfo().add(approveDetail);
						}
						
						pl.getApproveInfo().add(approved);
					}
					/*
					else {
						// 未审批工作项
						ApprovePendingQueryVO approvePending = new ApprovePendingQueryVO();
						
						approvePending.setNodeId(workitem.getTemplateItemId());
						approvePending.setName(node.getName());
						approvePending.setWorkitemId(workitem.getId());
						
						List<WorkitemApproverEntity> approvers = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
						for(WorkitemApproverEntity entry : approvers) {
							UserEntity user = userJpaRepo.findOne(entry.getApprover());
							if(user == null) {
								System.out.println(entry.getApprover() + "===" + workitem.getId());
							}else {
								approvePending.addApprover(entry.getApprover(), user.getName());
							}
							
						}
						pl.getApprovePendings().add(approvePending);
					}*/
				}
				
				// TODO 如果需求需要显示全部的待审批节点， 将下面单独处理的注释掉，然后放开上面循环中注释掉的内容
				{
					WorkitemEntity workitem = workitemJpaRepo.findById(rs.getString("workitem_id")).get();
					ApprovePendingQueryVO approvePending = new ApprovePendingQueryVO();
					WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(workitem.getTemplateNodeId()).get();
					approvePending.setNodeId(workitem.getTemplateNodeId());
					if(node == null) {
						System.out.println(workitem.getTemplateNodeId() + "===" + rs.getString("workitem_id"));
					}else {
						approvePending.setName(node.getName());
						approvePending.setWorkitemId(workitem.getId());
						
						List<WorkitemApproverEntity> approvers = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
						for(WorkitemApproverEntity entry : approvers) {
							UserEntity user = userJpaRepo.findById(entry.getApprover()).get();
							if(user == null) {
								System.out.println(entry.getApprover() + "===" + workitem.getId());
							}else {
								approvePending.addApprover(entry.getApprover(), user.getName());
							}
							
						}
						pl.getApprovePendings().add(approvePending);
					}
				}
				
				list.add(pl);
			}
		}
		
		vo.setProcessList(list);
		return vo;
	}
	
	/**
	 * 我的已办项<br>
	 * 有效的参数为：  templateId、processState、promoterName、submitDateBegin、submitDateEnd、approveDateBegin、approveDateEnd、pageSize、pageNum<br>
	 * @param approverId 审批用户ID
	 * @param query 查询条件
	 * @return
	 */
	public ProcessListQueryReturnVO myApprovedProcessList(String approverId, ProcessListQuery query) {
		
		ProcessListQueryReturnVO vo = new ProcessListQueryReturnVO();
		vo.setQuery(query);
		
		String baseSql = " FROM easy_wf_process p, easy_wf_user u, easy_wf_workitem w, easy_wf_workitem_approver a, easy_wf_template t " +
				" WHERE p.promoter = u.id AND p.id = w.process_id AND w.id = a.workitem_id "
				+ " AND a.approve_result IS NOT NULL /*审批过的节点(多人审批时，该节点未必审批完成)*/ AND a.approver = ? AND p.template_id = t.id ";
		List<Object> params = new ArrayList<>();
		params.add(approverId);
		
		// 流程模板查询条件
		if(query.getProcessState() != null) {
			baseSql = baseSql + " and p.template_id = ? ";
			params.add(query.getTemplateId());
		}
		
		// 流程发起人查询条件
		if(query.getProcessState() != null) {
			baseSql = baseSql + " and u.name LIKE ? ";
			params.add("%" + query.getPromoterName() + "%");
		}
		
		// 提交开始时间查询条件
		if(query.getSubmitDateBegin() != null) {
			baseSql = baseSql + " and p.start_time >= ? ";
			params.add(query.getSubmitDateBegin());
		}
		// 提交终止时间查询条件
		if(query.getSubmitDateEnd() != null) {
			baseSql = baseSql + " and p.start_time <= ? ";
			params.add(query.getSubmitDateEnd());
		}
		
		// 流程状态查询条件
		if(query.getProcessState() != null) {
			baseSql = baseSql + " and p.process_state = ? ";
			params.add(query.getProcessState().toString());
		}
		
		// 提交开始时间查询条件
		if(query.getSubmitDateBegin() != null) {
			baseSql = baseSql + " and a.approve_time >= ? ";
			params.add(query.getApproveDateBegin());
		}
		// 提交终止时间查询条件
		if(query.getSubmitDateEnd() != null) {
			baseSql = baseSql + " and a.approve_time <= ? ";
			params.add(query.getApproveDateEnd());
		}
		
		String countSql = "select count(*) " + baseSql;
		
		// 查询总条数
		long count = jdbcTemplate.queryForObject(countSql, params.toArray(), Long.class);
		vo.setCount(count);
		
		List<ProcessListVO> list = new ArrayList<>();
		
		// 总条数大于0，再次执行SQL语句获取行信息
		if(count > 0) {
			String querySql = "SELECT p.id AS process_id, p.template_id, t.name AS template_name, p.promoter, "
					+ "u.name promoter_name, p.start_time,p.record_id, p.title,w.id AS workitem_id, p.process_state, p.approve_type, p.finish_time " +
					baseSql + " ORDER BY p.start_time DESC LIMIT ?, ? ";
			params.add((query.getPageNum() - 1) * query.getPageSize());
			params.add(query.getPageSize());
			
			SqlRowSet rs = jdbcTemplate.queryForRowSet(querySql, params.toArray());
			while(rs.next()) {
				ProcessListVO pl = new ProcessListVO();
				pl.setTemplateId(rs.getString("template_id"));
				pl.setProcessId(rs.getString("process_id"));
				pl.setProcessName(rs.getString("template_name"));
				pl.setPromoter(rs.getString("promoter"));
				pl.setPromoterName(rs.getString("promoter_name"));
				pl.setStartTime(rs.getDate("start_time"));
				pl.setRecordId(rs.getString("record_id"));
				pl.setTitle(rs.getString("title"));
				pl.setState(WFProcessState.valueOf(rs.getString("process_state")));
				if(pl.getState() == WFProcessState.FINISH) {
					pl.setApproveState(WFProcessApproveState.valueOf(rs.getString("approve_type")));
					pl.setOptTime(rs.getDate("finish_time"));
				}
				
				// 找到流程节点
				List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessIdAndTypeOrderByCreateTimeAsc(pl.getProcessId(), WFNodeType.WORK.toString());
				for(WorkitemEntity workitem : workitems) {
					WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(workitem.getTemplateNodeId()).get();
					// 已审批工作项 
					if(workitem.getPassTime() != null) {
						
						List<String> workitemIds = new ArrayList<>();
						workitemIds.add(workitem.getId());
						List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findApproveDetail(workitemIds);
						
						ApprovedItemContext approved = new ApprovedItemContext();
						approved.setNodeId(workitem.getTemplateNodeId());
						approved.setName(node.getName());
						approved.setWorkitemId(workitem.getId());
						
						for(WorkitemApproverEntity entry : approves) {
							ApproveBaseContext approveDetail = new ApproveBaseContext();
							approveDetail.setApproverId(entry.getApprover());
							approveDetail.setApproverName(userJpaRepo.findById(entry.getApprover()).get().getName());
							approveDetail.setApproveTime(entry.getApproveTime());
							approveDetail.setOpinions(entry.getOpinions());
							approveDetail.setResult(WFWorkitemApproveResult.valueOf(entry.getApproveResult()));
							
							approved.getApproveInfo().add(approveDetail);
						}
						
						pl.getApproveInfo().add(approved);
					}else {
						// 未审批工作项
						ApprovePendingQueryVO approvePending = new ApprovePendingQueryVO();
						
						approvePending.setNodeId(workitem.getTemplateNodeId());
						approvePending.setName(node.getName());
						approvePending.setWorkitemId(workitem.getId());
						
						List<WorkitemApproverEntity> approvers = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
						for(WorkitemApproverEntity entry : approvers) {
							UserEntity user = userJpaRepo.findById(entry.getApprover()).get();
							if(user == null) {
								System.out.println(entry.getApprover() + "===" + workitem.getId());
							}else {
								approvePending.addApprover(entry.getApprover(), user.getName());
							}
							
						}
						pl.getApprovePendings().add(approvePending);
					}
				}
				
				list.add(pl);
			}
		}
		
		vo.setProcessList(list);
		return vo;
	}
	
	public List<ProcessListVO> myProcess(String promoter) {
		List<ProcessListVO> list = new ArrayList<>();
		List<ProcessEntity> process = processJpaRepo.findByPromoterOrderByCreateTimeDesc(promoter);
		for(ProcessEntity entry : process) {
			ProcessListVO vo = new ProcessListVO();
			
			vo.setProcessId(entry.getId());
			vo.setStartTime(entry.getStartTime());
			vo.setPromoter(entry.getPromoter());
			vo.setPromoterName(userJpaRepo.findById(entry.getPromoter()).get().getName());
			vo.setProcessName(wfTemplateJpaRepo.findById(entry.getTemplateId()).get().getName());
			
			if("FINISH".equals(entry.getProcessState())) {
				vo.setOptTime(entry.getFinishTime());
				vo.setState(WFProcessState.FINISH);
//				vo.setResult(entry.getApproveType().equals("FAILED")? "审批不通过" : "通过");
			}else if("REJECT".equals(entry.getProcessState())) {
				vo.setOptTime(entry.getRejectTime());
				vo.setState(WFProcessState.REJECT);
//				vo.setResult("驳回");
			}else if("NORMAL".equals(entry.getProcessState())) {
				vo.setState(WFProcessState.NORMAL);
				List<Object[]> approver = workitemApproverJpaRepo.findNotApprove(entry.getId());
				Set<String> set = new HashSet<>();
				List<String> approverIds = new ArrayList<>();
				for(Object[] obj : approver) {
					set.add(obj[0].toString());
					approverIds.add(obj[1].toString());
				}
//				vo.setItemName(StringUtils.join(set.toArray(), ","));
				
				List<UserEntity> users = userJpaRepo.findByIdIn(approverIds);
				String userStr = "";
				for(UserEntity u : users) {
					userStr = userStr + u.getName() + ",";
				}
//				vo.setApprover(userStr);
//				vo.setResult("审批中");
			}
			
			list.add(vo);
		}
		return list;
	}
	
	public List<ApproveDetailVO> approveDetail(String processId){
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(processId);
		List<String> workitemIds = new ArrayList<>();
		for(WorkitemEntity entry : workitems) {
			workitemIds.add(entry.getId());
		}
		List<WorkitemApproverEntity> list = workitemApproverJpaRepo.findApproveDetail(workitemIds);
		List<ApproveDetailVO> r = new ArrayList<>();
		for(WorkitemApproverEntity entry : list) {
			ApproveDetailVO vo = new ApproveDetailVO();
			vo.setApproveResult(entry.getApproveResult());
			vo.setApproverId(entry.getApprover());
			vo.setApproverName(userJpaRepo.findById(entry.getApprover()).get().getName());
			vo.setApproveTime(entry.getApproveTime());
			vo.setOpinions(entry.getOpinions());
			r.add(vo);
		}
		return r;
	}
	

	public List<ProcessListVO> myDB(String userId){
		List<ProcessListVO> list = new ArrayList<>();
		List<WorkitemApproverEntity> list1 = workitemApproverJpaRepo.findmyDB(userId);
		for(WorkitemApproverEntity entry : list1) {
			WorkitemEntity item = workitemJpaRepo.findById(entry.getWorkitemId()).get();
			if(item.getPassTime() != null) {
				continue;
			}
			
			// 只有流程正常运行状态，才显示代办， 其他挂起等等状态，均不显示， 同时并发中，有一个分支不通过， 另外分支审批人也看不到代办
			// TODO 其他状态是否可以也返回给前台， 比如审批者可以看到待办项，但是待办项标注了挂起，审批不了
			ProcessEntity process = processJpaRepo.findById(item.getProcessId()).get();
			if(!"NORMAL".equals(process.getProcessState())) {
				continue;
			}
			
			ProcessListVO vo = new ProcessListVO();
			vo.setProcessId(process.getId());
			vo.setStartTime(process.getStartTime());
			vo.setPromoter(process.getPromoter());
			vo.setPromoterName(userJpaRepo.findById(process.getPromoter()).get().getName());
			vo.setProcessName(wfTemplateJpaRepo.findById(process.getTemplateId()).get().getName());
//			vo.setItemId(entry.getWorkitemId());
//			vo.setApproveId(entry.getId());
			List<Object[]> approver = workitemApproverJpaRepo.findNotApprove(process.getId());
			Set<String> set = new HashSet<>();
			List<String> approverIds = new ArrayList<>();
			for(Object[] obj : approver) {
				set.add(obj[0].toString());
				approverIds.add(obj[1].toString());
			}
//			vo.setItemName(StringUtils.join(set.toArray(), ","));
			
			List<UserEntity> users = userJpaRepo.findByIdIn(approverIds);
			String userStr = "";
			for(UserEntity u : users) {
				userStr = userStr + u.getName() + ",";
			}
//			vo.setApprover(userStr);
			
			list.add(vo);
		}
		
		return list;
	}
	
	/**
	 * 查看流程详细信息，包括审批信息
	 * @param processId 流程实例ID
	 * @return 流程详细信息
	 */
	public ProcessDetailVO getProcessApprove(String processId) {
		ProcessEntity process = processJpaRepo.findById(processId).get();
		return getProcessApprove(process);
	}
	
	private ProcessDetailVO getProcessApprove(ProcessEntity process) {
		
		ProcessDetailVO vo = new ProcessDetailVO();
		if(process == null) {
			return vo;
		}
		
		vo.setTemplateId(process.getTemplateId());
		vo.setProcessId(process.getId());
		vo.setProcessName(wfTemplateJpaRepo.findById(process.getTemplateId()).get().getName());
		vo.setPromoter(process.getPromoter());
		vo.setPromoterName(userJpaRepo.findById(process.getPromoter()).get().getName());
		vo.setStartTime(process.getStartTime());
		vo.setRecordId(process.getRecordId());
		vo.setTitle(process.getTitle());
		vo.setProcessState(WFProcessState.valueOf(process.getProcessState()));
		// 流程结束， 设置流程的审批状态和结束时间
		if(WFProcessState.FINISH == vo.getProcessState()) {
			vo.setProcessApproveState(WFProcessApproveState.valueOf(process.getApproveType()));
			vo.setOptTime(process.getFinishTime());
		} else if(WFProcessState.TERMINATE == vo.getProcessState()) {
			vo.setOptTime(process.getTerminateTime());
		} else if(WFProcessState.REJECT == vo.getProcessState()) {
			vo.setOptTime(process.getRejectTime());
		} else if(WFProcessState.HANGUP == vo.getProcessState()) {
			List<ProcessHangupEntity> hangups = processHangupJpaRepo.findByProcessId(process.getId());
			for(ProcessHangupEntity hangup : hangups) {
				if(hangup.getWakeupTime() == null) {
					vo.setOptTime(hangup.getHangupTime());
					break;
				}
			}
		}
		
		// 设置已审批项、待审批项
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessIdAndTypeOrderByCreateTimeAsc(process.getId(), WFNodeType.WORK.toString());
		for(WorkitemEntity workitem : workitems) {
			ApprovedItemContext aic = new ApprovedItemContext();
			aic.setNodeId(workitem.getTemplateNodeId());
			aic.setWorkitemId(workitem.getId());
			
			WFTemplateNodeEntity node = wfTemplateItemJapRepo.findById(workitem.getTemplateNodeId()).get();
			aic.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
			aic.setApproveType(WFNodeHandleMode.valueOf(workitem.getHandleMode()));
		
			// 设置审批情况
			List<WorkitemApproverEntity> approveInfos = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
			
			if(workitem.getPassTime() != null) {
				// 已办项，将审核用户与未审核用户分开，审核用户将全部信息补充完整，未审核用户直接放入未审核列表
				for(WorkitemApproverEntity entity : approveInfos) {
					
					// 但未经过该用户审批， 节点不通过的时候，如果存在多人办理，那么该参数也是获取当前办理人
					if(entity.getApproveTime() == null) {
						aic.getOtherApprovals().add(entity.getApprover());
					}
					
					// 用户审批
					if(WFWorkitemApproveResult.PASS.toString().equals(entity.getApproveResult()) ||
						WFWorkitemApproveResult.FAILED.toString().equals(entity.getApproveResult()) ||
						WFWorkitemApproveResult.REJECT.toString().equals(entity.getApproveResult())) {
						ApproveBaseContext approveBaseContext = new ApproveBaseContext();
						approveBaseContext.setApproverId(entity.getApprover());
						approveBaseContext.setApproveTime(entity.getApproveTime());
						approveBaseContext.setResult(WFWorkitemApproveResult.valueOf(entity.getApproveResult()));
						approveBaseContext.setOpinions(entity.getOpinions());
						approveBaseContext.setApproverName(userJpaRepo.findById(entity.getApprover()).get().getName());
						
						aic.getApproveInfo().add(approveBaseContext);
					}
				}
				
				vo.getApproved().add(aic);
				
			}else {
				
				// 待办项，把工作项里面的审批用户全部列出来
				for(WorkitemApproverEntity entity : approveInfos) {
					ApproveBaseContext approveBaseContext = new ApproveBaseContext();
					approveBaseContext.setApproverId(entity.getApprover());
					approveBaseContext.setApproveTime(entity.getApproveTime());
					approveBaseContext.setOpinions(entity.getOpinions());
					approveBaseContext.setApproverName(userJpaRepo.findById(entity.getApprover()).get().getName());
					
					aic.getApproveInfo().add(approveBaseContext);
					aic.getOtherApprovals().add(entity.getApprover());
				}
				vo.getApprovePending().add(aic);
			}
		}
		
		return vo;
	}
}

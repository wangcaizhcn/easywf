package com.easywf.wf.process.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.easywf.wf.common.EasyWFException;
import com.easywf.wf.common.LoginUserContext;
import com.easywf.wf.common.ResultCodeConstant;
import com.easywf.wf.common.WFSpringContextUtil;
import com.easywf.wf.process.bean.WFProcessCreateBean;
import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;
import com.easywf.wf.process.constant.WFWorkitemApproveResult;
import com.easywf.wf.process.entity.ProcessConcurrentEntity;
import com.easywf.wf.process.entity.ProcessConcurrentParamEntity;
import com.easywf.wf.process.entity.ProcessEntity;
import com.easywf.wf.process.entity.ProcessHangupEntity;
import com.easywf.wf.process.entity.ProcessParamEntity;
import com.easywf.wf.process.entity.WorkitemApproverEntity;
import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.process.entity.WorkitemTransferEntity;
import com.easywf.wf.process.event.ProcessStartEvent;
import com.easywf.wf.process.event.WorkitemCreateEvent;
import com.easywf.wf.process.event.WorkitemPassEvent;
import com.easywf.wf.process.repository.ProcessConcurrentJpaRepo;
import com.easywf.wf.process.repository.ProcessConcurrentParamJpaRepo;
import com.easywf.wf.process.repository.ProcessHangupJpaRepo;
import com.easywf.wf.process.repository.ProcessJpaRepo;
import com.easywf.wf.process.repository.ProcessParamJpaRepo;
import com.easywf.wf.process.repository.WorkitemApproverJpaRepo;
import com.easywf.wf.process.repository.WorkitemJpaRepo;
import com.easywf.wf.process.repository.WorkitemTransferJpaRepo;
import com.easywf.wf.process.vo.ProcessParamVO;
import com.easywf.wf.resources.repository.UserJpaRepo;
import com.easywf.wf.resources.repository.UserOrgJpaRepo;
import com.easywf.wf.resources.repository.UserRoleJpaRepo;
import com.easywf.wf.template.constant.WFApprovalMode;
import com.easywf.wf.template.constant.WFApprovalPassType;
import com.easywf.wf.template.constant.WFApproverStrategy;
import com.easywf.wf.template.constant.WFNodeHandleMode;
import com.easywf.wf.template.constant.WFNodeType;
import com.easywf.wf.template.constant.WFParamValueType;
import com.easywf.wf.template.constant.WFTemplateState;
import com.easywf.wf.template.entity.WFTemplateEntity;
import com.easywf.wf.template.entity.WFTemplateLineEntity;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.entity.WFTemplateNodeParamEntity;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.entity.WFTemplateParamEntity;
import com.easywf.wf.template.repository.WFTemplateJpaRepo;
import com.easywf.wf.template.repository.WFTemplateLineJpaRepo;
import com.easywf.wf.template.repository.WFTemplateNodeJapRepo;
import com.easywf.wf.template.repository.WFTemplateNodeParamJpaRepo;
import com.easywf.wf.template.repository.WFTemplateNodeSettingJpaRepo;
import com.easywf.wf.template.repository.WFTemplateParamJpaRepo;
import com.easywf.wf.transfer.entity.WFAutoTransferSettingEntity;
import com.easywf.wf.transfer.repository.WFAutoTransferSettingJpaRepo;
import com.easywf.wf.util.context.ApproveBaseContext;
import com.easywf.wf.util.context.ApproveContext;
import com.easywf.wf.util.context.ApprovePendingItemContext;
import com.easywf.wf.util.context.ApproveUserContext;
import com.easywf.wf.util.context.ApprovedItemContext;
import com.easywf.wf.util.context.HangupContext;
import com.easywf.wf.util.context.ProcessApproveContext;
import com.easywf.wf.util.context.ProcessContext;
import com.easywf.wf.util.context.TransferContext;
import com.easywf.wf.util.context.WorkitemBaseContext;
import com.easywf.wf.util.service.ApproveUserService;
import com.easywf.wf.util.service.ParamValidityService;
import com.easywf.wf.util.service.ProcessApproveService;
import com.easywf.wf.util.service.ProcessForcePassService;
import com.easywf.wf.util.service.ProcessHangupService;
import com.easywf.wf.util.service.ProcessRejectService;
import com.easywf.wf.util.service.ProcessTerminateService;
import com.easywf.wf.util.service.ProcessTransferService;
import com.easywf.wf.util.test.TestWorkitemPassService;

/**
 * 流程服务类
 * @author wangcai
 * @version 1.0
 */
@Service
public class ProcessInstanceService {
	
	@Autowired
	private ExpressionEngineService expressionEngineService;
	
	@Autowired
	private ProcessJpaRepo processJpaRepo;
	
	@Autowired
	private WorkitemJpaRepo workitemJpaRepo;
	
	@Autowired
	private WFTemplateLineJpaRepo wfTemplateLineJpaRepo;
	
	@Autowired
	private WFTemplateNodeJapRepo wfTemplateItemJapRepo;
	
	@Autowired
	private WFTemplateNodeSettingJpaRepo wfTemplateNodeSettingJpaRepo;
	
	@Autowired
	@Qualifier("wfUserRoleJpaRepo")
	private UserRoleJpaRepo userRoleJpaRepo;
	
	@Autowired
	private WorkitemApproverJpaRepo workitemApproverJpaRepo;
	
	@Autowired
	private ProcessParamJpaRepo processParamJpaRepo;
	
	@Autowired
	@Qualifier("wfUserOrgJpaRepo")
	private UserOrgJpaRepo userOrgJpaRepo;
	
	@Autowired
	@Qualifier("wfUserJpaRepo")
	private UserJpaRepo userJpaRepo;
	
	@Autowired
	private WFTemplateJpaRepo wfTemplateJpaRepo;
	
	@Autowired
	private WorkitemTransferJpaRepo workitemTransferJpaRepo;
	
	@Autowired
	private ProcessHangupJpaRepo processHangupJpaRepo;
	
	@Autowired
	private WFTemplateParamJpaRepo wfTemplateParamJpaRepo;
	
	@Autowired
	private ProcessConcurrentJpaRepo processConcurrentJpaRepo;
	
	@Autowired
	private WFTemplateNodeParamJpaRepo wfTemplateNodeParamJpaRepo;
	
	@Autowired
	private ProcessConcurrentParamJpaRepo processConcurrentParamJpaRepo;
	
	@Autowired
	private WFAutoTransferSettingJpaRepo wfAutoTransferSettingJpaRepo;
	
	// 自定义实现类的时候，通过@primary 覆盖默认实现类
	@Autowired
	private LoginUserContext loginUserContext;
	
	/**
	 *  未在流程模板中定义的，流程实例额外传入的参数，特殊标识前缀
	 */
	public static final String PROCESS_PARAM_CUSTOM_PREFIX = "$_C_";
	
	/**
	 * 创建流程实例
	 * @param templateId 流程模板ID
	 * @param promoter 流程发起人
	 * @param recordId 业务记录ID
	 * @param title 流程标题
	 * @param reason 流程事由
	 * @param params 实例参数
	 * @return 新创建的流程实例ID
	 */
	@Transactional
	public String createProcess(WFProcessCreateBean processCreateBean) {

		// 从最高版本号依次向下找到发布中的流程
		List<WFTemplateEntity> templates = wfTemplateJpaRepo.findByBaseTemplateIdOrderByVersionDesc(processCreateBean.getBaseTemplateId());
		WFTemplateEntity template = null;
		
		for(WFTemplateEntity t : templates) {
			if(WFTemplateState.PUBLISH.toString().equals(t.getState())) {
				template = t;
				break;
			}
		}
		
		Assert.notNull(template, "流程不存在或者未发布");
		
		// TODO 校验子流程是否已经发布， 防止主流程发布，而子流程下线， 或者子流程失效的时候，同步将引用的主流程全部置成失效
		
		Date now = new Date();
		// 生成流程实例对象
		ProcessEntity process = new ProcessEntity();
		process.setId(UUID.randomUUID().toString());
		process.setTemplateId(template.getId());
		process.setTemplateVersion(template.getVersion());
		process.setBaseTemplateId(template.getBaseTemplateId());
		process.setPromoter(processCreateBean.getPromoter());
		process.setCreateTime(now);
		process.setProcessState(WFProcessState.CREATE.toString());
		process.setUpdateTime(now);
		process.setUpdateUserId(loginUserContext.getLoginUserId());
		
		process.setRecordId(processCreateBean.getRecordId());
		process.setTitle(processCreateBean.getTitle());
		process.setReason(processCreateBean.getReason());
		// TODO version为流程实例的version，用于乐观锁。 暂时未实现，后续采用jpa自动实现version
		process.setVersion(1);
		process.setSaap(template.isSaap());
		process.setPaap(template.isPaap());
		
		processJpaRepo.saveAndFlush(process);

		List<WFTemplateParamEntity> templateParams = wfTemplateParamJpaRepo.findByTemplateId(template.getId());
		
		// 将流程参数保存起来
		for(WFTemplateParamEntity templateParam : templateParams) {
			ProcessParamEntity param = new ProcessParamEntity();
			param.setId(UUID.randomUUID().toString());
			param.setProcessId(process.getId());
			param.setName(templateParam.getName());
			param.setProperty(templateParam.getProperty());
			param.setValueType(templateParam.getValueType());
			
			// 从传入参数中获取对应值,否则参数值为初始参数值
			if(processCreateBean.getParams().containsKey(templateParam.getProperty())) {
				param.setValue(processCreateBean.getParams().get(templateParam.getProperty()));
				processCreateBean.getParams().remove(templateParam.getProperty());
			}else {
				param.setValue(templateParam.getInitValue());
			}
			
			// 创建流程的时候，不一定一次性将参数设置全
			if(StringUtils.isNotBlank(param.getValue())) {
				ParamValidityService.check(param.getValue(), param.getValueType());
			}
			
			processParamJpaRepo.saveAndFlush(param);
		}
		
		// 将额外传入的参数记录起来，工作流引擎中不会使用，可以提供给业务系统，全部按字符串算
		if(processCreateBean.getParams().size() > 0) {
			for(Map.Entry<String, String> entry : processCreateBean.getParams().entrySet()) {
				ProcessParamEntity param = new ProcessParamEntity();
				param.setId(UUID.randomUUID().toString());
				param.setProcessId(process.getId());
				param.setProperty(PROCESS_PARAM_CUSTOM_PREFIX + entry.getKey());
				param.setValueType(WFParamValueType.STRING.toString());
				param.setValue(entry.getValue());
				processParamJpaRepo.saveAndFlush(param);
			}
		}
		
		return process.getId();
	}
	
	/**
	 * 启动流程
	 * @param processId 流程实例ID
	 */
	@Transactional
	public void startProcess(String processId) {
		ProcessEntity process = processJpaRepo.findById(processId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程不存在"));
		// TODO 线程推进采用另外线程，并且采用join来完成线程等待。 多个工作项的处理。可以拆成更小的类单独服务，多个线程可能事务无法控制
		// TODO 并发时需要测试下，如果使用乐观锁，是否是直接就可以避免并发，否则需要增加分布式锁，并且确保接口幂等性
		
		// 不是创建状态不能启动，防止其他状态重复启动
		if(!WFProcessState.CREATE.toString().equals(process.getProcessState())) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程不是创建状态，不能启动流程");
		}
		
		Date now = new Date();
		process.setStartTime(now);
		process.setProcessState(WFProcessState.NORMAL.toString());
		process.setUpdateTime(now);
		process.setUpdateUserId(loginUserContext.getLoginUserId());
		
		processJpaRepo.saveAndFlush(process);
		
		// == 录制流程上下文 ==
		// TODO 并发中的参数需要考虑放入到item中
		ApproveServiceContext.init();
		ApproveContext approveContext = ApproveServiceContext.getApproveContext();
		ProcessApproveContext pac = new ProcessApproveContext();
		pac.setState(ProcessApproveContext.STATE_START);
		pac.setProcessId(process.getId());
		WFTemplateEntity template = wfTemplateJpaRepo.findById(process.getTemplateId()).get();
		pac.setProcessName(template.getName());
		pac.setPromoter(process.getPromoter());
		pac.setRecordId(process.getRecordId());
		pac.setStartTime(process.getStartTime());
		pac.setTitle(process.getTitle());
		pac.setTemplateId(template.getId());
		
		approveContext.setProcessContext(pac);
		ApproveServiceContext.setApproveContext(approveContext);
		// === end ===
		
		// 抛出启动事件，解耦后续操作
		ProcessConcurrentServiceContext.init();
		ProcessStartEvent startEvent = new ProcessStartEvent(process);
		WFSpringContextUtil.getApplicationContext().publishEvent(startEvent);
		
		// 回调业务方法，将审批信息的上下文传递过去
		// TODO 最后写测试类，看看是否可以捕获到上线文信息
		for (ProcessApproveService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessApproveService.class).values()) {
			if(serice.support(ApproveServiceContext.getApproveContext())) {
				serice.handle(ApproveServiceContext.getApproveContext());
			}
		}
	}
	
	/**
	 * 创建流程实例并发起流程
	 * @param templateId 流程模板ID
	 * @param promoter 发起人
	 * @param recordId 业务数据记录ID
	 * @param title 流程标题
	 * @param reason 流程事由
	 * @param params 流程参数
	 * @return 新创建的流程实例ID
	 */
	@Transactional
	public String createAndStartProcess(WFProcessCreateBean processCreateBean) {
		String processId = createProcess(processCreateBean);
		startProcess(processId);
		return processId;
	}
	
	
	/**
	 * 创建工作项，该方法仅提供生成工作项，
	 * @param processId 流程实例ID
	 * @param templateItem 流程模板中的用于创建工作项的节点对象
	 * @return 工作项ID
	 */
	public String createWorkitem(String processId, WFTemplateNodeEntity templateNode) {
		
		WorkitemEntity workitem = new WorkitemEntity(processId, templateNode.getId());
		workitem.setType(templateNode.getType());
		
		if(WFNodeType.AUTO.toString().equals(templateNode.getType())) {
			workitem.setAutoService(templateNode.getAutoService());
		}
		// TODO 前面节点时条件，再设置line的时候，必需指定 路由条件和优先级
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(templateNode.getId());
		if(nodeSetting != null) {
			workitem.setFunctions(nodeSetting.getFunctions());
			workitem.setHandleMode(nodeSetting.getHandleMode());
			workitem.setPassType(nodeSetting.getPassType());
			workitem.setApprovalMode(nodeSetting.getApprovalMode());
			workitem.setPassService(nodeSetting.getPassService());
		}
		
		workitem.setConcurrentId(ProcessConcurrentServiceContext.getConcurrentId());
		
		// 结束节点和并发结束节点，不设置并发ID参数
		// 存在嵌套情况，因此当并发结束节点结后，仍然再并发中
		if (WFNodeType.END.toString()
				.equals(templateNode.getType())/*
												 * || WFNodeType.CONCURRENTEND.toString().equals(templateNode.getType())
												 */) {
			workitem.setConcurrentId(null);
		}
		
		workitemJpaRepo.save(workitem);
		
		if(nodeSetting != null) {
			
			ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
			// ==== 设置工作项办理人的上下文  ====
			ApproveContext approveContext = ApproveServiceContext.getApproveContext();
			ApprovePendingItemContext itemContext = new ApprovePendingItemContext();
			
			if(WFNodeHandleMode.APPROVAL.toString().equals(workitem.getHandleMode())) {
				itemContext.setApprovalMode(WFApprovalMode.valueOf(workitem.getApprovalMode()));
			}
			if(WFApprovalMode.MULTI.toString().equals(workitem.getApprovalMode())) {
				itemContext.setApprovalPassType(WFApprovalPassType.valueOf(workitem.getPassType()));
			}
			itemContext.setApproveType(WFNodeHandleMode.valueOf(workitem.getHandleMode()));
			itemContext.setWorkitemId(workitem.getId());
			itemContext.setNodeId(workitem.getTemplateNodeId());
			WFTemplateNodeEntity node = wfTemplateItemJapRepo.findById(workitem.getTemplateNodeId()).get();
			itemContext.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
			
			String service = "";
			if(WFApproverStrategy.ROLE.toString().equals(nodeSetting.getStrategy())) {
				service = "role_user_approve";
			} else if(WFApproverStrategy.PARAMUSER.toString().equals(nodeSetting.getStrategy())){
				service = "param_user_approve";
			} else if(WFApproverStrategy.ORG_ROLE.toString().equals(nodeSetting.getStrategy())){
				service = "org_role_user_approve";
			} else if(StringUtils.isNotBlank(nodeSetting.getApproverService())) {
				service = nodeSetting.getApproverService();
			}
			
			// 选人操作，TODO 选人后，应该把自动审批抽取出去，该步骤只设置审批人，并且采用事件方式解耦后续操作
			if(StringUtils.isNotBlank(service)) {
				Object clazz = WFSpringContextUtil.getBean(service);
				if(clazz != null) {
					ApproveUserService aus = (ApproveUserService)clazz;
					
					ApproveUserContext auc = new ApproveUserContext();
					// 设置流程基础信息
					auc.setTemplateId(process.getTemplateId());
					auc.setProcessId(process.getId());
					auc.setProcessName(wfTemplateJpaRepo.findById(process.getTemplateId()).get().getName());
					auc.setPromoter(process.getPromoter());
					auc.setStartTime(process.getStartTime());
					auc.setRecordId(process.getRecordId());
					auc.setTitle(process.getTitle());
					// 设置找人的工作项信息
					WorkitemBaseContext wbc = new WorkitemBaseContext();
					wbc.setApproveType(WFNodeHandleMode.valueOf(workitem.getHandleMode()));
					wbc.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
					wbc.setNodeId(node.getId());
					wbc.setWorkitemId(workitem.getId());
					
					auc.setWorkitem(wbc);
					
					// 设置流程参数
					List<ProcessParamEntity> params = processParamJpaRepo.findByProcessId(workitem.getProcessId());
					for(ProcessParamEntity param : params) {
						ProcessParamVO vo = new ProcessParamVO();
						vo.setProperty(param.getProperty());
						vo.setValueType(param.getValueType());
						vo.setValue(param.getValue());
						auc.getParams().add(vo);
					}
					
					// 回调业务方法，获取审批人类表
					List<String> approvers = aus.findUsers(auc);
					
					// 审批人去重
					Set<String> approverSet = new HashSet<>(approvers);
					approvers = new ArrayList<>(approverSet);
					
					Date now = new Date();
					// 设置审批人
					for(String approver : approvers) {
						// 查询对应的转办人，如果有转办人，直接设置转办人即可
						List<WFAutoTransferSettingEntity> settings = wfAutoTransferSettingJpaRepo.findTransferSetting(approver, now);
						
						// 转办人
						String realApprover = approver;
						if(settings != null && settings.size() > 0) {
							realApprover = settings.get(0).getTransferUserId();
							if(approverSet.contains(realApprover)){
								continue;
							}
							itemContext.getTransfer().put(approver, realApprover);
						}
						
						WorkitemApproverEntity entry = new WorkitemApproverEntity();
						entry.setId(UUID.randomUUID().toString());
						entry.setWorkitemId(workitem.getId());
						entry.setApprover(realApprover);
						
						workitemApproverJpaRepo.saveAndFlush(entry);
						
						itemContext.getApprover().add(realApprover);
					}
				}
				approveContext.getApprovePendings().add(itemContext);
				ApproveServiceContext.setApproveContext(approveContext);
			}
		}
		
		// 抛出创建事件   需要拿到下方， 选人  设置完成后，  会进行自动审批判断，然后再pass，或者非审批节点 pass
		WorkitemCreateEvent event = new WorkitemCreateEvent("");
		event.setTemplateNode(templateNode);
		event.setWorkitem(workitem);
		event.setNodeSetting(nodeSetting);
		WFSpringContextUtil.getApplicationContext().publishEvent(event);
		
		return workitem.getId();
	}
	
	/**
	 * 工作项通过
	 * @param workItemId  工作项ID
	 * @param approveType 工作项结论
	 */
	public void passWorkitem(String workItemId, WFProcessApproveState approveType) {
		WorkitemEntity workitem = workitemJpaRepo.findById(workItemId).get();
		workitem.setPassTime(new Date());
		workitemJpaRepo.save(workitem);
		// create之后，listener处理是否可以pass， 并发结束节点能否pass应该做校验
		WorkitemPassEvent event = new WorkitemPassEvent(workitem);
		event.setApproveType(approveType);
		
		WFSpringContextUtil.getApplicationContext().publishEvent(event);
	}
	
	/**
	 * 查找下个节点
	 * @param workitem 工作项
	 */
	public void nextWorkitem(WorkitemEntity workitem) {
		
		// 存放下个节点的集合，由于并发会有多个分支，所以采用集合的方式
		List<String> nodeIds = new ArrayList<>();
		
		// TODO condition 支持内置 default的表达式，表示其他不满足的时候，就走这个
		
		// 当前节点是条件节点，则通过路由表达式判断出来下个节点
		if(WFNodeType.CONDITION.toString().equals(workitem.getType())) {
			// 条件路由执行
			List<WFTemplateLineEntity> routeLines = wfTemplateLineJpaRepo.findByBeginNodeIdOrderByPriority(workitem.getTemplateNodeId());
			
			// 查找流程变量， 如果是并发分支，则逐级向上找，直到主流程变量
			Map<String, ProcessParamVO> processParamMap = new HashMap<>();
			getProcessParams(workitem.getProcessId(), workitem.getConcurrentId(), processParamMap);
			
			List<ProcessParamVO> paramVOs = new ArrayList<>();
			for(Map.Entry<String, ProcessParamVO> mapEntry : processParamMap.entrySet()) {
				paramVOs.add(mapEntry.getValue());
			}
			
			for(WFTemplateLineEntity entity : routeLines) {
				// 路由条件计算
				boolean result = expressionEngineService.match(entity.getRouteExpression(), paramVOs);
				if(result == true) {
					nodeIds.add(entity.getEndNodeId());
					break;
				}
			}
			
		} else {
			// 直接创建下个节点，并找到对应审批用户
			List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByBeginNodeIdOrderByPriority(workitem.getTemplateNodeId());
			
			for(WFTemplateLineEntity line : lines) {
				nodeIds.add(line.getEndNodeId());
			}
		}
		// TODO AUTO 接口内应该提供设置审批人的，必如自动审核，设置一个审核人，审核通过就完成流程，审核不通过走到下一个节点等的需求（其实可以在业务中自己做，只有需要审核的再发起流程即可）
		// 直接创建下个节点，并找到对应审批用户
//		List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByBeginNodeIdOrderByPriority(workitem.getTemplateNodeId());
		
		Map<String, ProcessConcurrentParamEntity> paramMap = new HashMap<>();
		
		if(WFNodeType.CONCURRENTBEGIN.toString().equals(workitem.getType())) {
			
			// 如果是嵌套参数，则需要一层一层往上找，最终都没有，则采用主流程参数
			List<WFTemplateNodeParamEntity> nodeParams = wfTemplateNodeParamJpaRepo.findByNodeId(workitem.getTemplateNodeId());
			List<String> paramIds = new ArrayList<>();
			for(WFTemplateNodeParamEntity npe : nodeParams) {
				paramIds.add(npe.getParamId());
			}
			if(paramIds.size() > 0) {
				List<WFTemplateParamEntity> params = wfTemplateParamJpaRepo.findByIdIn(paramIds);
				Map<String, String> target = new HashMap<>();
				for(WFTemplateParamEntity tpe : params) {
					target.put(tpe.getProperty(), "");
				}
				
				if(target.size() > 0) {
					findConcurrentParams(workitem.getProcessId(), workitem.getConcurrentId(), target, paramMap);
				}
			}
		}
		
		for(String nodeId : nodeIds) {
			if(WFNodeType.CONCURRENTBEGIN.toString().equals(workitem.getType())) {
				ProcessConcurrentEntity pce = new ProcessConcurrentEntity();
				pce.setId(UUID.randomUUID().toString());
				pce.setCreateTime(new Date());
				pce.setParentConcurrentId(workitem.getConcurrentId());
				pce.setProcessId(workitem.getProcessId());
				pce.setWorkitemId(workitem.getId());
				processConcurrentJpaRepo.save(pce);
				
				// 克隆参数
				for(Map.Entry<String, ProcessConcurrentParamEntity> entry : paramMap.entrySet()) {
					ProcessConcurrentParamEntity e = entry.getValue();
					e.setId(UUID.randomUUID().toString());
					e.setConcurrentId(pce.getId());
					processConcurrentParamJpaRepo.save(e);
				}
				
				// 设置并发ID和对应的起始工作项ID
				ProcessConcurrentServiceContext.setConcurrentId(pce.getId());
			}else {
				// 如果不是并发节点，则从当前工作项中找到并发ID
				ProcessConcurrentServiceContext.setConcurrentId(workitem.getConcurrentId());
			}
			
			WFTemplateNodeEntity templateItem = wfTemplateItemJapRepo.findById(nodeId).get();
			// 下个节点应该是并发结束节点，则不能直接创建，需要判断
			// 并发结束节点， 不能直接创建，需要进行判断
			if(WFNodeType.CONCURRENTEND.toString().equals(templateItem.getType())) {
				
				// 首先回写分支，状态是完成
				ProcessConcurrentEntity p = processConcurrentJpaRepo.findById(workitem.getConcurrentId()).get();
				p.setFinishTime(new Date());
				processConcurrentJpaRepo.save(p);
				
				// 找到所有的分支节点，并判断分支节点是否全部完成（---由于一个事务，可能查询不来的当前分支ID是未完成，所以不用考虑当前分支节点）
				List<ProcessConcurrentEntity> pclist = processConcurrentJpaRepo.findByWorkitemId(p.getWorkitemId());
				for(ProcessConcurrentEntity en : pclist) {
					if(en.getId().equals(p.getId())) {
						continue;
					}
					// 存在未完成的，则等待,不能创建下个节点
					if(en.getFinishTime() == null) {
						return;
					}
				}
				
				// 如果全完成，则创建节点, 此时需要情况并发节点ID
				// 嵌套并发，当里层并发节点结束后，设置上层的并发节点
				ProcessConcurrentServiceContext.setConcurrentId(p.getParentConcurrentId());
			}
			
			//String workitemId = 
			createWorkitem(workitem.getProcessId(), templateItem);
			
			// 在创建工作项最后，已经抛出了创建后事件，该事件中已经进行了pass操作
			
		}
		
	}
	
	private void getProcessParams(String processId, String concurrentId, Map<String, ProcessParamVO> paramsMap) {
		if(StringUtils.isBlank(concurrentId)) {
			List<ProcessParamEntity> params = processParamJpaRepo.findByProcessId(processId);
			for(ProcessParamEntity param : params) {
				if(!paramsMap.containsKey(param.getProperty())) {
					ProcessParamVO vo = new ProcessParamVO();
					vo.setProperty(param.getProperty());
					vo.setValueType(param.getValueType());
					vo.setValue(param.getValue());
					paramsMap.put(param.getProperty(), vo);
				}
			}
		}else {
			List<ProcessConcurrentParamEntity> list = processConcurrentParamJpaRepo.findByConcurrentId(concurrentId);
			for(ProcessConcurrentParamEntity pcpe : list) {
				if(!paramsMap.containsKey(pcpe.getProperty())) {
					ProcessParamVO vo = new ProcessParamVO();
					vo.setProperty(pcpe.getProperty());
					vo.setValueType(pcpe.getValueType());
					vo.setValue(pcpe.getValue());
					paramsMap.put(pcpe.getProperty(), vo);
				}
			}
			
			// 递归方式，继续向上级找
			ProcessConcurrentEntity concurrent = processConcurrentJpaRepo.findById(concurrentId).get();
			if(concurrent != null) {
				getProcessParams(processId, concurrent.getParentConcurrentId(), paramsMap);
			}
		}
	}
	
	/**
	 * 找到并发流程中的参数，依次向上级并发流程中获取参数，如果本次没有，上级有，就写入，否则保持参数不动
	 * 这个过程就是从底层往外层找，凡是找到了，就不用再找了，一直找到最顶层，也就是主流程中.
	 * 全部的参数都需要传递
	 * @param params
	 */
	private void findConcurrentParams(String processId, String concurrentId, Map<String, String> target, Map<String, ProcessConcurrentParamEntity> result) {
		// 主流程中找
		if(StringUtils.isBlank(concurrentId)) {
			List<ProcessParamEntity> ppes = processParamJpaRepo.findByProcessId(processId);
			for(ProcessParamEntity ppe : ppes) {
				if(target.containsKey(ppe.getProperty()) && !result.containsKey(ppe.getProperty())) {
					ProcessConcurrentParamEntity entry = new ProcessConcurrentParamEntity();
//					entry.setId(UUID.randomUUID().toString());
					entry.setName(ppe.getName());
					entry.setProcessId(processId);
					entry.setProperty(ppe.getProperty());
					entry.setValueType(ppe.getValueType());
					entry.setValue(ppe.getValue());
					result.put(ppe.getProperty(), entry);
					if(target.size() == result.size()) {
						return;
					}
				}
			}
		}else {
			// 上级并发中找
			List<ProcessConcurrentParamEntity> list = processConcurrentParamJpaRepo.findByConcurrentId(concurrentId);
			for(ProcessConcurrentParamEntity pcpe : list) {
				if(target.containsKey(pcpe.getProperty()) && !result.containsKey(pcpe.getProperty())) {
					
					ProcessConcurrentParamEntity entry = new ProcessConcurrentParamEntity();
					entry.setId(UUID.randomUUID().toString());
					entry.setName(pcpe.getName());
					entry.setProcessId(processId);
					entry.setProperty(pcpe.getProperty());
					entry.setValueType(pcpe.getValueType());
					entry.setValue(pcpe.getValue());
					
					result.put(pcpe.getProperty(), entry);
					if(target.size() == result.size()) {
						return;
					}
				}
			}
			
			// 递归方式，继续向上级找
			ProcessConcurrentEntity concurrent = processConcurrentJpaRepo.findById(concurrentId).get();
			if(concurrent != null) {
				findConcurrentParams(processId, concurrent.getParentConcurrentId(), target, result);
			}
		}
	}
	
	
	/**
	 * 办理
	 * @param approver 办理人
	 * @param workitemId 工作项ID
	 * @param approveResult 审批结论
	 * @param opinions 意见
	 */
	@Transactional
	public void approve(String approver, String workitemId, WFWorkitemApproveResult approveResult, String opinions) {
		// TODO 如果当前多个节点都是同一个人审批，这个人审批一个节点之后，其他的节点是否自动通过？
		
		WorkitemApproverEntity approveEntry = workitemApproverJpaRepo.findByWorkitemIdAndApprover(workitemId, approver);
		// 如果需要办理的工作项不存在，则抛出异常
		if(approveEntry == null) {
			throw new RuntimeException("找不到办理工作， 审批人：" + approveEntry + "，工作项：" + workitemId);
		}
		
		if(approveEntry.getApproveTime() != null || StringUtils.isNotBlank(approveEntry.getApproveResult())) {
			throw new RuntimeException("工作项已办理，不能重复办理");
		}
		
		// 查找工作项
		WorkitemEntity workitem = workitemJpaRepo.findById(workitemId).get();
		// 防止未审核就通过的工作项，再次被审核
		if(workitem.getPassTime() != null) {
			return;
		}
		
		ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
		
		// 设置上下文
		ApproveServiceContext.init();
		ApproveContext approveContext = ApproveServiceContext.getApproveContext();
		
		// 当前审批人
		approveContext.setApproverId(approver);
		
		// 流程相关信息
		ProcessApproveContext pac = new ProcessApproveContext();
		pac.setState(ProcessApproveContext.STATE_APPROVE);
		pac.setProcessId(workitem.getProcessId());
		WFTemplateEntity template = wfTemplateJpaRepo.findById(process.getTemplateId()).get();
		pac.setProcessName(template.getName());
		pac.setPromoter(process.getPromoter());
		pac.setRecordId(process.getRecordId());
		pac.setStartTime(process.getStartTime());
		pac.setTitle(process.getTitle());
		pac.setTemplateId(template.getId());
		
		approveContext.setProcessContext(pac);
		ApproveServiceContext.setApproveContext(approveContext);
		
		// 将办理信息存储起来
		approveEntry.setApproveResult(approveResult.toString());
		approveEntry.setOpinions(opinions);
		approveEntry.setApproveTime(new Date());
		workitemApproverJpaRepo.saveAndFlush(approveEntry);
		
		// 审批情况， 需要根据审批通过设置，判断工作项是否通过
		if("APPROVAL".equals(workitem.getHandleMode())) {
			// 竞争办理，一个人通过则通过，一个人不通过则不通过
			if(workitem.getApprovalMode().equals(WFApprovalMode.COMPETE.toString())) {
				
				// == 设置通过的工作项上下文信息 ===
				ApprovedItemContext approved = new ApprovedItemContext();
				approved.setApproveType(WFNodeHandleMode.APPROVAL);
				approved.setWorkitemId(workitemId);
				approved.setNodeId(workitem.getTemplateNodeId());
				WFTemplateNodeEntity node = wfTemplateItemJapRepo.findById(workitem.getTemplateNodeId()).get();
				approved.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
				
				List<WorkitemApproverEntity> approveInfos = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
				List<String> otherApprovals = new ArrayList<>();
				for(WorkitemApproverEntity entity : approveInfos) {
					if(entity.getApproveTime() == null && StringUtils.isBlank(entity.getApproveResult())) {
						otherApprovals.add(entity.getApprover());
					}
					
					if(WFWorkitemApproveResult.PASS.toString().equals(entity.getApproveResult()) ||
						WFWorkitemApproveResult.FAILED.toString().equals(entity.getApproveResult())) {
						ApproveBaseContext approveBaseContext = new ApproveBaseContext();
						approveBaseContext.setApproverId(entity.getApprover());
						approveBaseContext.setApproveTime(entity.getApproveTime());
						approveBaseContext.setResult(WFWorkitemApproveResult.valueOf(entity.getApproveResult()));
						approveBaseContext.setOpinions(entity.getOpinions());
						approveBaseContext.setApproverName(userJpaRepo.findById(entity.getApprover()).get().getName());
						approved.getApproveInfo().add(approveBaseContext);
					}
				}
				approved.setOtherApprovals(otherApprovals);
				approveContext.setApproved(approved);
				ApproveServiceContext.setApproveContext(approveContext);
				// ==== end ===
				
				if(approveResult.toString().equals("PASS")) {
					
					passWorkitem(workitemId, WFProcessApproveState.PASS);
					
					// 流程结束在结束方法中直接回调，其余的在该方法中回调
					if(!ProcessApproveContext.STATE_FINISH.equals(ApproveServiceContext.getApproveContext().getProcessContext().getState())) {
						for (ProcessApproveService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessApproveService.class).values()) {
							if(serice.support(ApproveServiceContext.getApproveContext())) {
								serice.handle(ApproveServiceContext.getApproveContext());
							}
						}
					}
					return;
				}else {
					// 结束办理工作项
					workitem.setPassTime(new Date());
					workitemJpaRepo.save(workitem);
					
					// 未通过，直接创建终止流程节点
					List<WFTemplateNodeEntity> items = wfTemplateItemJapRepo.findByTemplateIdAndTypeOrderByLevelAsc(process.getTemplateId(), WFNodeType.END.toString());
					ProcessConcurrentServiceContext.setConcurrentId(workitem.getConcurrentId());
					String finishWorkitemId = createWorkitem(process.getId(), items.get(0));
					
					passWorkitem(finishWorkitemId, WFProcessApproveState.FAILED);
					return;
				}
			}else {
				// 多人办理， 所有人先办理完成，然后再进行判断，保证多人全部审批完成
				List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
				int passCount = 0;
				for(WorkitemApproverEntity approve : approves) {
					if(StringUtils.isBlank(approve.getApproveResult())) {
						// 有未审核的，则等待审核
						return;
					}else if(!WFWorkitemApproveResult.PASS.toString().equals(approve.getApproveResult())
							&& !WFWorkitemApproveResult.FAILED.toString().equals(approve.getApproveResult())) {
						// TODO 判断挂起状态， 可以通过其他方式判断
						return;
					}
					if(WFWorkitemApproveResult.PASS.toString().equals(approve.getApproveResult())) {
						passCount ++;
					}
				}
				
				boolean canPass = false;
				if(workitem.getPassType().equals(WFApprovalPassType.SINGLE.toString())) {
					if(passCount >= 1) {
						canPass = true;
					}
				}else if(workitem.getPassType().equals(WFApprovalPassType.HALF.toString())) {
					if((double)passCount / (double)approves.size() > 0.5) {
						canPass = true;
					}
				}else if(workitem.getPassType().equals(WFApprovalPassType.ALL.toString())) {
					if(passCount == approves.size()) {
						canPass = true;
					}
				}else if(workitem.getPassType().equals(WFApprovalPassType.SERVICE.toString())) {
					
					ProcessContext pc = new ProcessContext();
					pc.setProcessId(workitem.getProcessId());
					
//					ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
					
					pc.setFinishTime(process.getFinishTime());
					pc.setPromoter(process.getPromoter());
					pc.setRecordId(process.getRecordId());
					pc.setResult(workitem.getHandleMode());
					pc.setStartTime(process.getStartTime());
					pc.setTitle(process.getTitle());
					
					// TODO 需要让业务类回调方法，否则只能判断通过和不通过， 如果终止等其他状态则不能进行处理，其他状态几乎不存在
					TestWorkitemPassService service = (TestWorkitemPassService)WFSpringContextUtil.getApplicationContext().getBean(workitem.getPassService());
					canPass = service.pass(pc);
				}
				
				// == 设置通过的工作项上下文信息 ===
				ApprovedItemContext approved = new ApprovedItemContext();
				approved.setApproveType(WFNodeHandleMode.APPROVAL);
				approved.setWorkitemId(workitemId);
				approved.setNodeId(workitem.getTemplateNodeId());
				WFTemplateNodeEntity node = wfTemplateItemJapRepo.findById(workitem.getTemplateNodeId()).get();
				approved.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
				
				List<WorkitemApproverEntity> approveInfos = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
				List<String> otherApprovals = new ArrayList<>();
				for(WorkitemApproverEntity entity : approveInfos) {
					if(entity.getApproveTime() == null && StringUtils.isBlank(entity.getApproveResult())) {
						otherApprovals.add(entity.getApprover());
					}
					
					if(WFWorkitemApproveResult.PASS.toString().equals(entity.getApproveResult()) ||
						WFWorkitemApproveResult.FAILED.toString().equals(entity.getApproveResult())) {
						ApproveBaseContext approveBaseContext = new ApproveBaseContext();
						approveBaseContext.setApproverId(entity.getApprover());
						approveBaseContext.setApproveTime(entity.getApproveTime());
						approveBaseContext.setResult(WFWorkitemApproveResult.valueOf(entity.getApproveResult()));
						approveBaseContext.setOpinions(entity.getOpinions());
						approveBaseContext.setApproverName(userJpaRepo.findById(entity.getApprover()).get().getName());
						approved.getApproveInfo().add(approveBaseContext);
					}
				}
				approved.setOtherApprovals(otherApprovals);
				approveContext.setApproved(approved);
				ApproveServiceContext.setApproveContext(approveContext);
				// ==== end ===
				
				// 工作项通过
				if(canPass == true) {
					
					passWorkitem(workitemId, WFProcessApproveState.PASS);
					
					// 流程结束在结束方法中直接回调，其余的在该方法中回调
					if(!ProcessApproveContext.STATE_FINISH.equals(ApproveServiceContext.getApproveContext().getProcessContext().getState())) {
						for (ProcessApproveService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessApproveService.class).values()) {
							if(serice.support(ApproveServiceContext.getApproveContext())) {
								serice.handle(ApproveServiceContext.getApproveContext());
							}
						}
					}
					
					return;
				}else {
					// 工作项办理完，结果不通过
					// TODO 代码与经证办理类似，考虑抽取出来
					workitem.setPassTime(new Date());
					workitemJpaRepo.save(workitem);
					
//					ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
					// 未通过，直接创建终止流程节点
					List<WFTemplateNodeEntity> items = wfTemplateItemJapRepo.findByTemplateIdAndTypeOrderByLevelAsc(process.getTemplateId(), WFNodeType.END.toString());
					ProcessConcurrentServiceContext.setConcurrentId(workitem.getConcurrentId());
					String finishWorkitemId = createWorkitem(process.getId(), items.get(0));
					passWorkitem(finishWorkitemId, WFProcessApproveState.FAILED);
					return;
				}
			}
		}else {
			// 意见没有通过不通过，所有人办理完才进行下个节点
			List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
			for(WorkitemApproverEntity approve : approves) {
				if(StringUtils.isBlank(approve.getApproveResult())) {
					// 有未审核的，则等待审核
					return;
				}else if(!WFWorkitemApproveResult.PASS.toString().equals(approve.getApproveResult())
						&& !WFWorkitemApproveResult.FAILED.toString().equals(approve.getApproveResult())) {
					// TODO 判断挂起状态， 可以通过其他方式判断
					return;
				}
			}
			
			
			// == 设置通过的工作项上下文信息 ===
			ApprovedItemContext approved = new ApprovedItemContext();
			approved.setApproveType(WFNodeHandleMode.OPINION);
			approved.setWorkitemId(workitemId);
			approved.setNodeId(workitem.getTemplateNodeId());
			WFTemplateNodeEntity node = wfTemplateItemJapRepo.findById(workitem.getTemplateNodeId()).get();
			approved.setName(StringUtils.isBlank(node.getDisplay()) ? node.getName() : node.getDisplay());
			
			List<WorkitemApproverEntity> approveInfos = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
			List<String> otherApprovals = new ArrayList<>();
			for(WorkitemApproverEntity entity : approveInfos) {
				if(entity.getApproveTime() == null && StringUtils.isBlank(entity.getApproveResult())) {
					otherApprovals.add(entity.getApprover());
				}
				
				if(WFWorkitemApproveResult.PASS.toString().equals(entity.getApproveResult()) ||
					WFWorkitemApproveResult.FAILED.toString().equals(entity.getApproveResult())) {
					ApproveBaseContext approveBaseContext = new ApproveBaseContext();
					approveBaseContext.setApproverId(entity.getApprover());
					approveBaseContext.setApproveTime(entity.getApproveTime());
					approveBaseContext.setResult(WFWorkitemApproveResult.valueOf(entity.getApproveResult()));
					approveBaseContext.setOpinions(entity.getOpinions());
					approveBaseContext.setApproverName(userJpaRepo.findById(entity.getApprover()).get().getName());
					approved.getApproveInfo().add(approveBaseContext);
				}
			}
			approved.setOtherApprovals(otherApprovals);
			approveContext.setApproved(approved);
			ApproveServiceContext.setApproveContext(approveContext);
			// ==== end ===
			
			// 全部办理完成， 直接通过节点
			passWorkitem(workitemId, WFProcessApproveState.PASS);
			
			// 流程结束在结束方法中直接回调，其余的在该方法中回调
			if(!ProcessApproveContext.STATE_FINISH.equals(ApproveServiceContext.getApproveContext().getProcessContext().getState())) {
				for (ProcessApproveService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessApproveService.class).values()) {
					if(serice.support(ApproveServiceContext.getApproveContext())) {
						serice.handle(ApproveServiceContext.getApproveContext());
					}
				}
			}
			
			return;
		}
	}
	
	//TODO 驳回的工作流，可以重新启动， 其他异常情况是否仍然可以restart？
	public void restart() {
		
	}
	
	
	/**
	 * 流程结束 -- 正常结束（通过、不通过）
	 * @param processId 流程实例ID
	 * @param approveType 流程最终审批结论
	 */
	public void finish(String processId, WFProcessApproveState approveType) {
		
		ProcessEntity process = processJpaRepo.findById(processId).get();
		process.setFinishTime(new Date());
		process.setProcessState(WFProcessState.FINISH.toString());
		process.setApproveType(approveType.toString());
		processJpaRepo.save(process);
		
		ApproveContext ac = ApproveServiceContext.getApproveContext();
		// 结束状态
		ac.getProcessContext().setState(ProcessApproveContext.STATE_FINISH);
		// 审批结果
		ac.getProcessContext().setResult(approveType);
		ApproveServiceContext.setApproveContext(ac);
		
		// 回调接口方法， 业务系统实现该接口，处理后续业务
		// TODO 目前接口调用最好放到消息队列中调用执行，不在同一个事务中控制， 补全上下文对象
		for (ProcessApproveService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessApproveService.class).values()) {
			if(serice.support(ApproveServiceContext.getApproveContext())) {
				serice.handle(ApproveServiceContext.getApproveContext());
			}
		}
	}
	
	/**
	 * 意见节点，管理员可以强制通过，避免某些原因卡在这个流程节点
	 * @param workitemId 工作项ID
	 */
	public void forcePass(String workitemId) {
		// 校验，只有意见类型节点，才能特殊通过
		WorkitemEntity workitem = workitemJpaRepo.findById(workitemId).get();
		if(!WFNodeHandleMode.OPINION.toString().equals(workitem.getHandleMode())) {
			System.out.println("该工作项不是意见类型，不能通过，请使用转办功能！");
			return;
		}
		
		List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitemId);
		List<String> approvers = new ArrayList<>();
		for(WorkitemApproverEntity approve : approves) {
			if(StringUtils.isBlank(approve.getOpinions())) {
				// 其他审判者直接停止审批
				approve.setApproveTime(new Date());
				approve.setOpinions("未发表意见");
				workitemApproverJpaRepo.save(approve);
				approvers.add(approve.getApprover());
			}
		}
		
		// 通过工作项
		passWorkitem(workitemId, WFProcessApproveState.PASS);
		
		// 回调业务接口，主要用来通知被略过者不用再发表意见了
		// TODO 需要变更下上下文对象，需要增加工作项ID
		HangupContext hc = new HangupContext();
		ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
		hc.setProcessId(process.getId());
		hc.setPromoter(process.getPromoter());
		hc.setRecordId(process.getRecordId());
		hc.setStartTime(process.getStartTime());
		hc.setTitle(process.getTitle());
		hc.setApprovers(approvers);
		
		for (ProcessForcePassService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessForcePassService.class).values()) {
			if(serice.support(hc)) {
				serice.forcePass(hc);
			}
		}
	}
	
	/**
	 * 驳回流程  -- 流程执行过程中， 直接驳回给发起者，该记录可以再次发起
	 * 多人审批的工作项，其中一人驳回，整个流程被驳回，其他人不能再审批
	 * @param processId 流程实例ID
	 * @param rejector 驳回的操作人
	 * @param opinions 意见
	 */
	@Transactional
	public void reject(String processId, String rejector, String opinions) {
		// 更新流程状态
		ProcessEntity process = processJpaRepo.findById(processId).get();
		Date now = new Date();
		process.setRejectTime(now);
		process.setUpdateUserId(rejector);
		process.setUpdateTime(now);
		process.setProcessState(WFProcessState.REJECT.toString());
		processJpaRepo.save(process);
		
		// 更新工作项，将自己的工作项设置驳回状态，更新审批意见，其余审批项均停止
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(processId);
		List<String> workitemIds = new ArrayList<>();
		for(WorkitemEntity entry : workitems) {
			workitemIds.add(entry.getId());
		}
		List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdIn(workitemIds);
		for(WorkitemApproverEntity approve : approves) {
			if(StringUtils.isBlank(approve.getApproveResult())) {
				// 设置驳回的信息
				if(approve.getApprover().equals(rejector)) {
					approve.setApproveResult(WFWorkitemApproveResult.REJECT.toString());
					approve.setOpinions(opinions);
					approve.setApproveTime(now);
					workitemApproverJpaRepo.save(approve);
					continue;
				}
				
				// TODO 并发被审核不通过，其他节点  和 多人竞争办理其他人的状态，应该都设置成STOP
				
				// 其他审判者直接停止审批
				approve.setApproveResult(WFWorkitemApproveResult.STOP.toString());
				workitemApproverJpaRepo.save(approve);
			}
		}
		
		// 回调业务系统的方法，业务系统继续处理后续工作
		ProcessContext pc = new ProcessContext();
		pc.setProcessId(processId);
		pc.setPromoter(process.getPromoter());
		pc.setRecordId(process.getRecordId());
		pc.setResult(WFProcessState.REJECT.toString());
		pc.setStartTime(process.getStartTime());
		pc.setTitle(process.getTitle());
		pc.setRejectorId(rejector);
		pc.setRejectTime(now);
		pc.setOpinions(opinions);
		
		for (ProcessRejectService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessRejectService.class).values()) {
			if(serice.support(pc)) {
				serice.reject(pc);
			}
		}
	}
	
	/**
	 * 转交流程，当前办理人不想办了，转交他人办理
	 * 或者当前正在审批的用户，离开岗位，不在办理中，需要由管理员手动转交他人
	 * @param approveId 审批项ID
	 * @param transferApprover 转交后的审批人ID
	 */
	@Transactional
	public void transfer(String approveId, String transferApprover) {
		// 更改工作项的审批人
		WorkitemApproverEntity approve = workitemApproverJpaRepo.findById(approveId).get();
		String approver = approve.getApprover();
		approve.setApprover(transferApprover);
		workitemApproverJpaRepo.save(approve);
		
		// 记录转办信息
		WorkitemTransferEntity wte = new WorkitemTransferEntity();
		wte.setApprover(approver);
		wte.setId(UUID.randomUUID().toString());
		wte.setTransferApprover(transferApprover);
		wte.setTransferTime(new Date());
		wte.setWorkitemId(approve.getWorkitemId());
		workitemTransferJpaRepo.save(wte);
		
		// 回调业务系统功能
		TransferContext tc = new TransferContext();
		tc.setApprover(approver);
		WorkitemEntity workitem = workitemJpaRepo.findById(approve.getWorkitemId()).get();
		tc.setProcessId(workitem.getProcessId());
		ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
		tc.setPromoter(process.getPromoter());
		tc.setRecordId(process.getRecordId());
		tc.setStartTime(process.getStartTime());
		tc.setTitle(process.getTitle());
		tc.setTransferApprover(transferApprover);
		tc.setWorkitemId(workitem.getId());
		
		for (ProcessTransferService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessTransferService.class).values()) {
			if(serice.support(tc)) {
				serice.transfer(tc);
			}
		}
	}
	
	/**
	 * 挂起流程 --挂起后，流程暂停审批
	 * @param processId 流程实例ID
	 */
	public void hangup(String processId) {
		// 判断是否可以挂起
		List<ProcessHangupEntity> hangups = processHangupJpaRepo.findByProcessId(processId);
		boolean canHangup = true;
		for(ProcessHangupEntity hangup : hangups) {
			if(hangup.getWakeupTime() == null) {
				canHangup = false;
				break;
			}
		}
		
		ProcessEntity process = processJpaRepo.findById(processId).get();
		if(WFProcessState.HANGUP.toString().equals(process.getProcessState())) {
			canHangup = false;
		}
		
		if(canHangup == false) {
			System.out.println("流程处于挂起状态，不能再次挂起");
			return;
		}
		
		// 更新流程状态
		process.setProcessState(WFProcessState.HANGUP.toString());
		processJpaRepo.save(process);
		
		// 记录挂起记录
		ProcessHangupEntity hangup = new ProcessHangupEntity();
		hangup.setId(UUID.randomUUID().toString());
		hangup.setHangupTime(new Date());
		hangup.setProcessId(processId);
		processHangupJpaRepo.save(hangup);
		
		// 更新正在进行中的审批项状态
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(processId);
		List<String> workitemIds = new ArrayList<>();
		for(WorkitemEntity entry : workitems) {
			workitemIds.add(entry.getId());
		}
		List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdIn(workitemIds);
		List<String> approvers = new ArrayList<>();
		for(WorkitemApproverEntity approve : approves) {
			if(StringUtils.isBlank(approve.getApproveResult())) {
				approve.setApproveResult(WFWorkitemApproveResult.HANGUP.toString());
				workitemApproverJpaRepo.save(approve);
				approvers.add(approve.getApprover());
			}
		}
		
		// 回调业务方法
		HangupContext hc = new HangupContext();
		hc.setProcessId(processId);
		hc.setPromoter(process.getPromoter());
		hc.setRecordId(process.getRecordId());
		hc.setStartTime(process.getStartTime());
		hc.setTitle(process.getTitle());
		hc.setApprovers(approvers);
		
		for (ProcessHangupService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessHangupService.class).values()) {
			if(serice.support(hc)) {
				serice.hangup(hc);
			}
		}
		
	}
	
	/**
	 * 唤醒流程， 唤醒挂起后的流程，使流程继续审批
	 * @param processId 流程实例ID
	 */
	public void wakeup(String processId) {
		// 判断是否可以唤醒流程
		ProcessHangupEntity wakeupEntity = null;
		List<ProcessHangupEntity> hangups = processHangupJpaRepo.findByProcessId(processId);
		for(ProcessHangupEntity hangup : hangups) {
			if(hangup.getWakeupTime() == null) {
				wakeupEntity = hangup;
				break;
			}
		}
		
		ProcessEntity process = processJpaRepo.findById(processId).get();
		if(!WFProcessState.HANGUP.toString().equals(process.getProcessState()) || wakeupEntity == null) {
			System.out.println("流程不是挂起状态，不能唤醒");
			return;
		}
		
		// 更新流程状态
		process.setProcessState(WFProcessState.NORMAL.toString());
		processJpaRepo.save(process);
		
		// 更新挂起记录
		wakeupEntity.setWakeupTime(new Date());
		processHangupJpaRepo.save(wakeupEntity);
		
		// 挂起的审批项
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(processId);
		List<String> workitemIds = new ArrayList<>();
		for(WorkitemEntity entry : workitems) {
			workitemIds.add(entry.getId());
		}
		List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdIn(workitemIds);
		List<String> approvers = new ArrayList<>();
		for(WorkitemApproverEntity approve : approves) {
			if(WFWorkitemApproveResult.HANGUP.toString().equals(approve.getApproveResult())) {
				approve.setApproveResult(null);
				workitemApproverJpaRepo.save(approve);
				approvers.add(approve.getApprover());
			}
		}
		
		// 回调业务方法
		HangupContext hc = new HangupContext();
		hc.setProcessId(processId);
		hc.setPromoter(process.getPromoter());
		hc.setRecordId(process.getRecordId());
		hc.setStartTime(process.getStartTime());
		hc.setTitle(process.getTitle());
		hc.setApprovers(approvers);
		
		for (ProcessHangupService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessHangupService.class).values()) {
			if(serice.support(hc)) {
				serice.wakeup(hc);
			}
		}
	}
	
	/**
	 *  终止流程 -- 流程正常进行中，不需要继续处理了
	 * @param processId 流程实例ID
	 */
	public void terminate(String processId) {
		// 更改流程状态
		ProcessEntity process = processJpaRepo.findById(processId).get();
		process.setTerminateTime(new Date());
		process.setProcessState(WFProcessState.TERMINATE.toString());
		processJpaRepo.save(process);
		
		// 将所有审批项终止
		List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(processId);
		List<String> workitemIds = new ArrayList<>();
		for(WorkitemEntity entry : workitems) {
			workitemIds.add(entry.getId());
		}
		List<WorkitemApproverEntity> approves = workitemApproverJpaRepo.findByWorkitemIdIn(workitemIds);
		List<String> approvers = new ArrayList<>();
		for(WorkitemApproverEntity approve : approves) {
			if(StringUtils.isBlank(approve.getApproveResult())) {
				approve.setApproveResult(WFWorkitemApproveResult.STOP.toString());
				workitemApproverJpaRepo.save(approve);
				approvers.add(approve.getApprover());
			}
		}
		
		// 回调业务系统方法
		HangupContext hc = new HangupContext();
		hc.setProcessId(processId);
		hc.setPromoter(process.getPromoter());
		hc.setRecordId(process.getRecordId());
		hc.setStartTime(process.getStartTime());
		hc.setTitle(process.getTitle());
		hc.setApprovers(approvers);
		
		for (ProcessTerminateService serice : WFSpringContextUtil.getApplicationContext().getBeansOfType(ProcessTerminateService.class).values()) {
			if(serice.support(hc)) {
				serice.terminate(hc);
			}
		}
	}
	
	/**
	 * 更新流程参数
	 * @param workitemId 工作项ID
	 * @param paramMap 参数集合
	 */
	@Transactional
	public void updateParam(String workitemId, Map<String, String> paramMap) {
		if(paramMap != null && paramMap.size() > 0) {
			WorkitemEntity workitem = workitemJpaRepo.findById(workitemId).get();
			updateProcessParams(workitem.getProcessId(), workitem.getConcurrentId(), paramMap);
		}
		// TODO 并发里面的工作项，不支持上边多个工作项汇总过来，校验流程正确性的时候判断下。如果需要， 则采用嵌套并发流程实现
	}
	
	private void updateProcessParams(String processId, String concurrentId, Map<String, String> paramMap) {
		
		// TODO 记录变量修改记录
		
		if(paramMap.size() == 0) {
			return;
		}
		// 主流程中找
		if(StringUtils.isBlank(concurrentId)) {
			List<ProcessParamEntity> ppes = processParamJpaRepo.findByProcessId(processId);
			for(ProcessParamEntity ppe : ppes) {
				if(paramMap.containsKey(ppe.getProperty())) {
					ppe.setValue(paramMap.get(ppe.getProperty()));
					processParamJpaRepo.save(ppe);
					paramMap.remove(ppe.getProperty());
				}
			}
		} else {
			// 并发中找
			List<ProcessConcurrentParamEntity> list = processConcurrentParamJpaRepo.findByConcurrentId(concurrentId);
			for(ProcessConcurrentParamEntity pcpe : list) {
				if(paramMap.containsKey(pcpe.getProperty())) {
					pcpe.setValue(paramMap.get(pcpe.getProperty()));
					processConcurrentParamJpaRepo.save(pcpe);
					paramMap.remove(pcpe.getProperty());
				}
			}
			
			// 递归方式，继续向上级找
			ProcessConcurrentEntity concurrent = processConcurrentJpaRepo.findById(concurrentId).get();
			if(concurrent != null) {
				updateProcessParams(processId, concurrent.getParentConcurrentId(), paramMap);
			}
		}
	}
}

package com.easywf.wf.process.listener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.easywf.wf.common.WFSpringContextUtil;
import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.entity.ProcessEntity;
import com.easywf.wf.process.entity.WorkitemApproverEntity;
import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.process.event.WorkitemCreateEvent;
import com.easywf.wf.process.repository.ProcessJpaRepo;
import com.easywf.wf.process.repository.WorkitemApproverJpaRepo;
import com.easywf.wf.process.service.ApproveServiceContext;
import com.easywf.wf.process.service.ProcessInstanceService;
import com.easywf.wf.template.constant.WFNodeHandleMode;
import com.easywf.wf.template.constant.WFNodeType;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.util.context.ApproveContext;
import com.easywf.wf.util.context.ProcessContext;
import com.easywf.wf.util.service.AutoWorkitemService;

@Component
public class WorkitemCreateListener {

	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Autowired
	private ProcessJpaRepo processJpaRepo;
	
	@Autowired
	private WorkitemApproverJpaRepo workitemApproverJpaRepo;
	
	@EventListener
	public void afterCreate(WorkitemCreateEvent event) {
		
		boolean pass = false;
		
		WFTemplateNodeSettingEntity nodeSetting = event.getNodeSetting();
		WorkitemEntity workitem = event.getWorkitem();
		
		// 自动节点
		if(WFNodeType.AUTO.toString().equals(workitem.getType())) {
			AutoWorkitemService service = (AutoWorkitemService)WFSpringContextUtil.getApplicationContext().getBean(workitem.getAutoService());
			ProcessContext pc = new ProcessContext();
			pc.setProcessId(workitem.getProcessId());
			pc.setWorkitemId(workitem.getId());
			// TODO 完善流程变量
			// TODO 抽取回调函数
			boolean flag = service.run(pc);
			if(flag == false) {
				return;
			}
			// TODO auto 需要仔细考虑下， 应该返回一个枚举值， 可以进行终止流程、审批结束等的直接跳转
			pass = true;
		} else if(WFNodeType.WORK.toString().equals(workitem.getType())) {
			boolean autoApprove = false;
			ProcessEntity process = processJpaRepo.findById(workitem.getProcessId()).get();
			
			List<WorkitemApproverEntity> approvers = workitemApproverJpaRepo.findByWorkitemIdOrderByApproveTimeAsc(workitem.getId());
			
			// 当前节点只有一人，并且是发起者，或者之前审批过，则直接通过
			if(approvers.size() == 1 && WFNodeHandleMode.APPROVAL.toString().equals(workitem.getHandleMode())) {
				// 申请人是自己
				if(process.isPaap() && approvers.get(0).getApprover().equals(process.getPromoter())) {
					autoApprove = true;
				}else if(process.isSaap()) {
					ApproveContext approveContext = ApproveServiceContext.getApproveContext();
					// TODO 审批saveAndFlush ,再测试下是否不需要从上下文中获取了
					// TODO  连续自动通过节点，判断下是否也不从从上下文中获取
					if(approvers.get(0).getApprover().equals(approveContext.getApproverId())) {
						// 刚刚审批过（防止事务没提交，数据库直接查询不到）
						autoApprove = true;
					}else {
						// 历史审批过
						List<WorkitemApproverEntity> list = workitemApproverJpaRepo.getUserAlreadyApprove(workitem.getProcessId(), approvers.get(0).getApprover());
						if(list != null && list.size() > 0) {
							autoApprove = true;
						}
					}
				}
			}
			
			// 自动审批通过
			// TODO 之前是instanceService中，写的代码，先判断是否通过，不自动通过的话，再设置审批人，设置审批人的时候进行了自动转半处理
			// TODO 需要测试下  a是发起人， a是审批人，然后a自动转办给b，那么a是审批人的节点则不会自动通过
			// TODO 可以通过转办表找到原始人，然后再判断
			if(autoApprove == true) {
				WorkitemApproverEntity entry = approvers.get(0);
				entry.setOpinions("自动审批通过");
				entry.setApproveResult(WFProcessApproveState.PASS.toString());
				entry.setApproveTime(new Date());
				
				workitemApproverJpaRepo.saveAndFlush(entry);
				
				pass = true;
			}
		}
		
		// TODO 并发结束节点能否pass，需要校验
		
		// TODO 最好是通过节点类型区判断
		if(nodeSetting == null) {
			pass = true;
		}
		
		if(pass) {
			processInstanceService.passWorkitem(workitem.getId(), WFProcessApproveState.PASS);
		}
	}
}

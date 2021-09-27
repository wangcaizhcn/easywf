package com.easywf.wf.process.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.easywf.wf.process.entity.ProcessEntity;
import com.easywf.wf.process.event.ProcessStartEvent;
import com.easywf.wf.process.service.ProcessInstanceService;
import com.easywf.wf.template.constant.WFNodeType;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.repository.WFTemplateNodeJapRepo;

@Component
public class ProcessStartListener {

	@Autowired
	private WFTemplateNodeJapRepo wfTemplateItemJapRepo;
	
	@Autowired
	private ProcessInstanceService processInstanceService;
	
	/**
	 * 流程设置启动后，创建开始节点
	 * @param event
	 */
	@EventListener
	public void start(ProcessStartEvent event) {
		
		// TODO 后续测试下如果方法内部失败，事务会不会回滚，该方法不再增加事务注解，并且未开启异步监听
		
		ProcessEntity process = (ProcessEntity) event.getSource();
		
		// 发起流程后，首先启动开始节点
		List<WFTemplateNodeEntity> items = wfTemplateItemJapRepo.findByTemplateIdAndTypeOrderByLevelAsc(process.getTemplateId(), WFNodeType.BEGIN.toString());
		processInstanceService.createWorkitem(process.getId(), items.get(0));
	}
}

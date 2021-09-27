package com.easywf.wf.process.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.process.event.WorkitemPassEvent;
import com.easywf.wf.process.service.ProcessInstanceService;
import com.easywf.wf.template.constant.WFNodeType;

@Component
public class WorkitemPassListener {

	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@EventListener
	public void afterPass(WorkitemPassEvent event) {
		
		WorkitemEntity workitem = (WorkitemEntity)event.getSource();
		
		// 执行路由，如果是终止节点，那么就执行流程完成操作
		if(WFNodeType.END.toString().equals(workitem.getType())) {
			processInstanceService.finish(workitem.getProcessId(), event.getApproveType());
		}else {
			processInstanceService.nextWorkitem(workitem);
		}

	}
}

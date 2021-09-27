package com.easywf.wf.process.event;

import org.springframework.context.ApplicationEvent;

import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;

public class WorkitemCreateEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	
	private WorkitemEntity workitem;
	
	private WFTemplateNodeEntity templateNode;
	
	private WFTemplateNodeSettingEntity nodeSetting;
	
	public WorkitemCreateEvent(Object source) {
		super(source);
	}

	public WorkitemEntity getWorkitem() {
		return workitem;
	}

	public WFTemplateNodeEntity getTemplateNode() {
		return templateNode;
	}

	public void setWorkitem(WorkitemEntity workitem) {
		this.workitem = workitem;
	}

	public void setTemplateNode(WFTemplateNodeEntity templateNode) {
		this.templateNode = templateNode;
	}

	public WFTemplateNodeSettingEntity getNodeSetting() {
		return nodeSetting;
	}

	public void setNodeSetting(WFTemplateNodeSettingEntity nodeSetting) {
		this.nodeSetting = nodeSetting;
	}

}

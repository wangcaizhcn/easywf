package com.easywf.wf.template.bean;

import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;

public class WFTemplateNodeDetailVO {

	// 流程节点
	private WFTemplateNodeEntity node;
	
	// 节点拓展属性
	private WFTemplateNodeSettingEntity setting;

	public WFTemplateNodeEntity getNode() {
		return node;
	}

	public void setNode(WFTemplateNodeEntity node) {
		this.node = node;
	}

	public WFTemplateNodeSettingEntity getSetting() {
		return setting;
	}

	public void setSetting(WFTemplateNodeSettingEntity setting) {
		this.setting = setting;
	}

}

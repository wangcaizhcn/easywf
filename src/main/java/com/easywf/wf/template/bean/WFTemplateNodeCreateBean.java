package com.easywf.wf.template.bean;

public class WFTemplateNodeCreateBean {
	
	// 流程模板名称
	private String name;
	
	// 显示名称
	private String display;
	
	// 节点描述
	private String description;
	
	// 所属流程模板ID
	private String templateId;
	
	// 节点类型
	private String type;
	
	// 自动节点实现类
	private String autoService;
	
	// 子流程节点所对应子流程模板ID
	private String childTemplateId;
	
	// 子流程结束后是否同步变量到主流程
	private boolean syncVariable;

	public String getName() {
		return name;
	}

	public String getDisplay() {
		return display;
	}

	public String getDescription() {
		return description;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getType() {
		return type;
	}

	public String getAutoService() {
		return autoService;
	}

	public String getChildTemplateId() {
		return childTemplateId;
	}

	public boolean isSyncVariable() {
		return syncVariable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setAutoService(String autoService) {
		this.autoService = autoService;
	}

	public void setChildTemplateId(String childTemplateId) {
		this.childTemplateId = childTemplateId;
	}

	public void setSyncVariable(boolean syncVariable) {
		this.syncVariable = syncVariable;
	}
	
}

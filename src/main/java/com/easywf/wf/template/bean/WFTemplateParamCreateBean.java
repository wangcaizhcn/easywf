package com.easywf.wf.template.bean;

public class WFTemplateParamCreateBean {

	// 所属模板ID
	private String templateId;
	
	// 参数名称
	private String name;
	
	// 参数属性
	private String property;
	
	// 参数初始值
	private String initValue;
	
	// 参数值类型
	private String valueType;

	public String getTemplateId() {
		return templateId;
	}

	public String getName() {
		return name;
	}

	public String getProperty() {
		return property;
	}

	public String getInitValue() {
		return initValue;
	}

	public String getValueType() {
		return valueType;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	
}

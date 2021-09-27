package com.easywf.wf.template.bean;

public class WFTemplateLineCreateBean {

	// 所属流程模板ID
	private String templateId;
	
	// 起始节点ID
	private String beginNodeId;
	
	// 终止节点ID
	private String endNodeId;
	
	// 优先级
	private Integer priority;
	
	// 路由表达式
	private String routeExpression;

	public String getTemplateId() {
		return templateId;
	}

	public String getBeginNodeId() {
		return beginNodeId;
	}

	public String getEndNodeId() {
		return endNodeId;
	}

	public Integer getPriority() {
		return priority;
	}

	public String getRouteExpression() {
		return routeExpression;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public void setBeginNodeId(String beginNodeId) {
		this.beginNodeId = beginNodeId;
	}

	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setRouteExpression(String routeExpression) {
		this.routeExpression = routeExpression;
	}
	
}

package com.easywf.wf.template.bean;

public class WFTemplateLineDetailVO {

	private String id;
	
	private String beginNode;
	
	private String endNode;
	
	private String beginNodeId;
	
	private String endNodeId;
	
	private int priority;
	
	private String routeExpression;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBeginNode() {
		return beginNode;
	}

	public void setBeginNode(String beginNode) {
		this.beginNode = beginNode;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getRouteExpression() {
		return routeExpression;
	}

	public void setRouteExpression(String routeExpression) {
		this.routeExpression = routeExpression;
	}

	public String getBeginNodeId() {
		return beginNodeId;
	}

	public void setBeginNodeId(String beginNodeId) {
		this.beginNodeId = beginNodeId;
	}

	public String getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}
	
}

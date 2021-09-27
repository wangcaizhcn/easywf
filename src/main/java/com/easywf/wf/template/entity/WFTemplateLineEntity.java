package com.easywf.wf.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 流程节点关系实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template_line",
	indexes = {@Index(name = "idx_template_id",  columnList="template_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template_line", comment = "流程模板节点与节点关系表")
public class WFTemplateLineEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "template_id", columnDefinition = "varchar(36) COMMENT '所属流程模板ID'")
	private String templateId;
	
	@Column(name = "begin_node_id", columnDefinition = "varchar(36) COMMENT '关系的起始节点ID'")
	private String beginNodeId;
	
	@Column(name = "end_node_id", columnDefinition = "varchar(36) COMMENT '关系的终止节点ID'")
	private String endNodeId;
	
	@Column(name = "priority", columnDefinition = "int(11) COMMENT '顺序，在条件开始节点的关系中，通过指定顺序来决定后续哪个关系先执行'")
	private int priority;
	
	@Column(name = "route_expression", columnDefinition = "varchar(2000) COMMENT '路由表达式，条件分支时根据该表达式进行判断'")
	private String routeExpression;

	/**
	 * 获取节点关系ID
	 * @return 节点关系ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置节点关系ID
	 * @param id 节点关系ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取所属流程模板ID
	 * @return 所属流程模板ID
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置所属流程模板ID
	 * @param templateId 所属流程模板ID
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
	/**
	 * 获取起始节点ID
	 * @return 起始节点ID
	 */
	public String getBeginNodeId() {
		return beginNodeId;
	}
	
	/**
	 * 设置起始节点ID
	 * @param beginNodeId 起始节点ID
	 */
	public void setBeginNodeId(String beginNodeId) {
		this.beginNodeId = beginNodeId;
	}

	/**
	 * 获取终止节点ID
	 * @return 终止节点ID
	 */
	public String getEndNodeId() {
		return endNodeId;
	}

	/**
	 * 设置终止节点ID
	 * @param endNodeId 终止节点ID
	 */
	public void setEndNodeId(String endNodeId) {
		this.endNodeId = endNodeId;
	}

	/**
	 * 获取流转判断的优先级
	 * @return 流转判断的优先级
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * 设置流转判断的优先级
	 * @param priority 流转判断的优先级
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * 获取流转的条件表达式
	 * @return 流转的条件表达式
	 */
	public String getRouteExpression() {
		return routeExpression;
	}

	/**
	 * 设置流转的条件表达式
	 * @param routeExpression 流转的条件表达式
	 */
	public void setRouteExpression(String routeExpression) {
		this.routeExpression = routeExpression;
	}
	
}

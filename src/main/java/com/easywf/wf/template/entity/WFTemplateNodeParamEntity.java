package com.easywf.wf.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 *  节点参数实体对象<br>
 *  在并发起始节点中设置的流程克隆参数
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template_node_param",
	indexes = {@Index(name = "idx_node_id",  columnList="node_id", unique = false),
		@Index(name = "idx_param_id",  columnList="param_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template_node_param", comment = "流程节点参数，用于并发节点克隆主流程参数")
public class WFTemplateNodeParamEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "node_id", columnDefinition = "varchar(36) COMMENT '流程节点ID'")
	private String nodeId;
	
	@Column(name = "param_id", columnDefinition = "varchar(36) COMMENT '参数ID，引用流程参数ID'")
	private String paramId;

	/**
	 * 获取节点参数ID
	 * @return 节点参数ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置节点参数ID
	 * @param id 节点参数ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取参数所属的并发起始节点ID
	 * @return 参数所属的并发起始节点ID
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 设置参数所属的并发起始节点ID
	 * @param nodeId 参数所属的并发起始节点ID
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 获取克隆的参数ID
	 * @return 克隆的参数ID
	 */
	public String getParamId() {
		return paramId;
	}

	/**
	 * 设置克隆的参数ID
	 * @param paramId 克隆的参数ID
	 */
	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
}

package com.easywf.wf.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 并发克隆参数实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_process_concurrent_param",
indexes = {@Index(name = "idx_process_id",  columnList="process_id", unique = false),
		@Index(name = "idx_concurrent_id",  columnList="concurrent_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_process_concurrent_param", comment = "并发节点每条链路上克隆的参数")
public class ProcessConcurrentParamEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	// 流程实例ID
	@Column(name = "process_id", columnDefinition = "varchar(36) COMMENT '所属流程实例ID'")
	private String processId;
	
	// 克隆所属的并发分支ID
	@Column(name = "concurrent_id", columnDefinition = "varchar(36) COMMENT '所属并发链路ID'")
	private String concurrentId;
	
	// 参数名
	@Column(name = "name", columnDefinition = "varchar(100) COMMENT '参数名称'")
	private String name;
	
	// 参数属性名
	@Column(name = "property", columnDefinition = "varchar(100) COMMENT '参数属性名称'")
	private String property;
	
	// 参数值
	@Column(name = "value", columnDefinition = "varchar(255) COMMENT '参数值'")
	private String value;
	
	// 参数类型
	@Column(name = "value_type", columnDefinition = "varchar(20) COMMENT '值类型，例如整型、日期、字符串等'")
	private String valueType;
	
	/**
	 * 获取并发克隆参数ID
	 * @return  并发克隆参数ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置并发克隆参数ID
	 * @param id 并发克隆参数ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取所属流程ID
	 * @return 流程ID
	 */
	public String getProcessId() {
		return processId;
	}

	/**
	 * 设置所属流程ID
	 * @param processId 流程ID
	 */
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	/**
	 * 获取参数名
	 * @return 参数名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置参数名
	 * @param name 参数名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取参数的属性名
	 * @return 参数的属性名
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 设置参数的属性名
	 * @param property 参数的属性名
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取参数值
	 * @return 参数值
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 设置参数值
	 * @param value 参数值
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * 获取参数值类型
	 * @return 参数值类型
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * 设置参数值类型
	 * @param valueType 参数值类型
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * 获取克隆分支的ID
	 * @return 克隆分支ID
	 */
	public String getConcurrentId() {
		return concurrentId;
	}

	/**
	 * 设置克隆分支ID
	 * @param concurrentId 克隆分支ID
	 */
	public void setConcurrentId(String concurrentId) {
		this.concurrentId = concurrentId;
	}
	
}

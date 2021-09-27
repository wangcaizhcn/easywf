package com.easywf.wf.process.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_process_param",
indexes = {@Index(name = "idx_process_id",  columnList="process_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_process_param", comment = "流程参数表")
public class ProcessParamEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "process_id", columnDefinition = "varchar(36) COMMENT '所属流程实例ID'")
	private String processId;
	
	@Column(name = "name", columnDefinition = "varchar(100) COMMENT '参数名称'")
	private String name;
			  
	@Column(name = "property", columnDefinition = "varchar(100) COMMENT '参数属性名称'")
	private String property;
	
	@Column(name = "value", columnDefinition = "varchar(255) COMMENT '参数值'")
	private String value;
	
	@Column(name = "value_type", columnDefinition = "varchar(20) COMMENT '值类型，例如整型、日期、字符串等'")
	private String valueType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

}

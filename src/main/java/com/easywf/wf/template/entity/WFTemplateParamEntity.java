package com.easywf.wf.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 流程参数实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template_param",
	indexes = {@Index(name = "idx_template_id",  columnList="template_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template_param", comment = "流程模板参数表")
public class WFTemplateParamEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "template_id", columnDefinition = "varchar(36) COMMENT '所属流程模板ID'")
	private String templateId;
	
	@Column(name = "name", columnDefinition = "varchar(100) COMMENT '参数名称'")
	private String name;
	
	@Column(name = "property", columnDefinition = "varchar(100) COMMENT '参数属性名称'")
	private String property;
	
	@Column(name = "init_value", columnDefinition = "varchar(255) COMMENT '初始值'")
	private String initValue;
	
	@Column(name = "value_type", columnDefinition = "varchar(20) COMMENT '值类型，例如整型、日期、字符串等'")
	private String valueType;

	/**
	 * 获取流程参数ID
	 * @return 流程参数ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置流程参数ID
	 * @param id 流程参数ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取参数名称
	 * @return 参数名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置参数名称
	 * @param name 参数名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取参数属性
	 * @return 参数属性
	 */
	public String getProperty() {
		return property;
	}
	
	/**
	 * 设置参数属性
	 * @param property 参数属性
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取初始值
	 * @return 初始值
	 */
	public String getInitValue() {
		return initValue;
	}

	/**
	 * 设置初始值
	 * @param initValue 初始值
	 */
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	/**
	 * 获取值类型
	 * @return 值类型，参考{@link com.easywf.wf.template.constant.WFParamValueType}
	 */
	public String getValueType() {
		return valueType;
	}
	
	/**
	 * 设置值类型
	 * @param valueType 值类型，参考{@link com.easywf.wf.template.constant.WFParamValueType}
	 */
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	/**
	 * 获取所属流程模板的模板ID
	 * @return 所属流程模板的模板ID
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置所属流程模板的模板ID
	 * @param templateId 所属流程模板的模板ID
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}

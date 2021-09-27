package com.easywf.wf.template.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.easywf.wf.template.constant.WFTemplateState;

/**
 * 流程模板实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template",
	indexes = {@Index(name = "idx_base_template_id",  columnList="base_template_id", unique = false),
			@Index(name = "idx_create_time",  columnList="create_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template", comment = "流程模板表")
public class WFTemplateEntity {

	public WFTemplateEntity() {}
	
	/**
	 * 通过名称和描述构造一个流程模板对象<br>
	 * 构造的流程模板对象为草稿状态
	 * @param name 模板名称
	 * @param description 模板描述
	 */
	public WFTemplateEntity(String name, String description) {
		this.id = UUID.randomUUID().toString();
		this.name = name;
		this.description = description;
		this.createTime = new Date();
		this.state = WFTemplateState.DRAFT.toString();
	}
	
	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "base_template_id", columnDefinition = "varchar(36) COMMENT '基础版本的模板ID，第一个版本时，与ID相同'")
	private String baseTemplateId;
	
	@Column(name = "name", columnDefinition = "varchar(100) COMMENT '流程模板名称'")
	private String name;
	
	@Column(name = "description", columnDefinition = "varchar(255) COMMENT '流程模板描述'")
	private String description;
	
	// 审批人为发起者，是否自动审批通过
	@Column(name = "promoter_approve_auto_pass", columnDefinition = "tinyint(1) default 0 COMMENT '审批人为发起者，是否自动审批通过，true自动通过，false不自动通过。默认为false'")
	private boolean paap = false;
	
	// 流程中相同的审批者，是否自动审批通过
	@Column(name = "same_approver_auto_pass", columnDefinition = "tinyint(1) default 0 COMMENT '流程中相同的审批者，之前节点审批通过，后续节点是否自动审批通过，true自动通过，false不自动通过。默认为false'")
	private boolean saap = false;
	
	@Column(name = "state", columnDefinition = "varchar(20) COMMENT '模板状态，草稿、发布、失效等'")
	private String state;
	
	@Column(name = "version", columnDefinition = "int(11) COMMENT '版本号'")
	private int version = 1;
	
	@Column(name = "create_time", columnDefinition = "datetime COMMENT '创建时间'")
	private Date createTime;
	
	@Column(name = "publish_time", columnDefinition = "datetime COMMENT '发布时间'")
	private Date publishTime;
	
	@Column(name = "invalid_time", columnDefinition = "datetime COMMENT '失效时间'")
	private Date invalidTime;

	/**
	 * 获取流程模板ID
	 * @return 流程模板ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置流程模板ID
	 * @param id 流程模板ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取流程模板名称
	 * @return 流程模板名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置流程模板名称
	 * @param name 流程模板名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取流程模板描述信息
	 * @return 流程模板描述信息
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置流程模板描述信息
	 * @param description 流程模板描述信息
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取流程模板创建时间
	 * @return 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置流程模板创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取流程模板版本号
	 * @return 流程模板版本号
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * 设置流程模板版本号
	 * @param version 流程模板版本号
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * 获取流程模板状态
	 * @return 流程模板状态，参考{@link com.easywf.wf.template.constant.WFTemplateState}
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置流程模板状态
	 * @param state 流程模板状态，参考{@link com.easywf.wf.template.constant.WFTemplateState}
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * 获取流程模板发布时间
	 * @return 发布时间
	 */
	public Date getPublishTime() {
		return publishTime;
	}

	/**
	 * 设置流程模板发布时间
	 * @param publishTime 发布时间
	 */
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * 获取流程模板失效时间
	 * @return 失效时间
	 */
	public Date getInvalidTime() {
		return invalidTime;
	}

	/**
	 * 设置流程模板失效时间
	 * @param invalidTime 失效时间
	 */
	public void setInvalidTime(Date invalidTime) {
		this.invalidTime = invalidTime;
	}

	/**
	 * 获取发起人在流程审批中，到达该节点是否自动审批通过
	 * @return
	 */
	public boolean isPaap() {
		return paap;
	}

	/**
	 * 获取相同的审批人在流程审批中，已经审批通过之前的节点，该节点是否自动审批通过
	 * @return
	 */
	public boolean isSaap() {
		return saap;
	}

	/**
	 * 设置发起人在流程审批中，到达该节点是否自动审批通过
	 * @param paap true自动审批通过，false不自动审批通过
	 */
	public void setPaap(boolean paap) {
		this.paap = paap;
	}

	/**
	 * 设置相同的审批人在流程审批中，已经审批通过之前的节点，该节点是否自动审批通过
	 * @param saap true自动审批通过，false不自动审批通过
	 */
	public void setSaap(boolean saap) {
		this.saap = saap;
	}

	public String getBaseTemplateId() {
		return baseTemplateId;
	}

	public void setBaseTemplateId(String baseTemplateId) {
		this.baseTemplateId = baseTemplateId;
	}

}





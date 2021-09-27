package com.easywf.wf.process.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_workitem",
indexes = {@Index(name = "idx_process_id",  columnList="process_id", unique = false),
		@Index(name = "idx_create_time",  columnList="create_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_workitem", comment = "工作项表")
public class WorkitemEntity {

	public WorkitemEntity() {}
	
	public WorkitemEntity(String processId) {
		this.id = UUID.randomUUID().toString();
		this.processId = processId;
		this.createTime = System.currentTimeMillis();
	}
	
	public WorkitemEntity(String processId, String nodeId) {
		this.id = UUID.randomUUID().toString();
		this.processId = processId;
		this.templateNodeId = nodeId;
		this.createTime = System.currentTimeMillis();
	}
	
	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "process_id", columnDefinition = "varchar(36) COMMENT '流程实例ID'")
	private String processId;
	
	@Column(name = "template_node_id", columnDefinition = "varchar(36) COMMENT '工作项对应流程模板ID'")
	private String templateNodeId;
	
	@Column(name = "type", columnDefinition = "varchar(20) COMMENT '工作项类型'")
	private String type;
	
	@Column(name = "create_time", columnDefinition = "bigint COMMENT '工作项生成时间'")
	private long createTime;
	
	@Column(name = "pass_time", columnDefinition = "datetime COMMENT '工作项通过时间'")
	private Date passTime;
	
	@Column(name = "functions", columnDefinition = "varchar(500) COMMENT '节点功能，通过、不通过、驳回等'")
	private String functions;
	
	@Column(name = "handle_mode", columnDefinition = "varchar(20) COMMENT '节点办理类型，审批、意见（会签）'")
	private String handleMode;
	
	@Column(name = "pass_type", columnDefinition = "varchar(20) COMMENT '节点通过类型，单人通过、半数通过等'")
	private String passType;

	@Column(name = "approval_mode", columnDefinition = "varchar(20) COMMENT '审批模式，竞争办理、多人办理'")
	private String approvalMode;
	
	@Column(name = "concurrent_id", columnDefinition = "varchar(36) COMMENT '处于并发分支中的工作项，指定哪个分支'")
	private String concurrentId;
	
	@Column(name = "auto_service", columnDefinition = "varchar(255) COMMENT '流程节点为自动节点时，设置的实现类'")
	private String autoService;
	
	@Column(name = "pass_service", columnDefinition = "varchar(255) COMMENT '自定义节点通过的实现类'")
	private String passService;
	
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

	public String getTemplateNodeId() {
		return templateNodeId;
	}

	public void setTemplateNodeId(String templateNodeId) {
		this.templateNodeId = templateNodeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public String getFunctions() {
		return functions;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	public String getHandleMode() {
		return handleMode;
	}

	public void setHandleMode(String handleMode) {
		this.handleMode = handleMode;
	}

	public String getPassType() {
		return passType;
	}

	public void setPassType(String passType) {
		this.passType = passType;
	}

	public String getApprovalMode() {
		return approvalMode;
	}

	public void setApprovalMode(String approvalMode) {
		this.approvalMode = approvalMode;
	}

	public String getConcurrentId() {
		return concurrentId;
	}

	public void setConcurrentId(String concurrentId) {
		this.concurrentId = concurrentId;
	}

	public String getAutoService() {
		return autoService;
	}

	public void setAutoService(String autoService) {
		this.autoService = autoService;
	}

	public String getPassService() {
		return passService;
	}

	public void setPassService(String passService) {
		this.passService = passService;
	}
	
}

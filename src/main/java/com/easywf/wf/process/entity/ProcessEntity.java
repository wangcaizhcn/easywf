package com.easywf.wf.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_process",
indexes = {@Index(name = "idx_template_id",  columnList="template_id", unique = false),
		@Index(name = "idx_base_template_id",  columnList="base_template_id", unique = false),
		@Index(name = "idx_create_time",  columnList="create_time", unique = false),
		@Index(name = "idx_process_state",  columnList="process_state", unique = false),
		@Index(name = "idx_record_id",  columnList="record_id", unique = false),
		@Index(name = "idx_promoter",  columnList="promoter", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_process", comment = "流程实例表")
public class ProcessEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "template_id", columnDefinition = "varchar(36) COMMENT '流程模板ID'")
	private String templateId;
	
	@Column(name = "base_template_id", columnDefinition = "varchar(36) COMMENT '流程模板基础版本ID'")
	private String baseTemplateId;
	
	@Column(name = "template_version", columnDefinition = "int(11) COMMENT '流程模板的版本号'")
	private int templateVersion;
	
	@Column(name = "promoter", columnDefinition = "varchar(36) COMMENT '流程发起人ID'")
	private String promoter;
	
	@Column(name = "create_time", columnDefinition = "datetime COMMENT '流程创建时间'")
	private Date createTime;
	
	@Column(name = "start_time", columnDefinition = "datetime COMMENT '流程启动时间'")
	private Date startTime;
	
	@Column(name = "finish_time", columnDefinition = "datetime COMMENT '审批结束时间'")
	private Date finishTime;
	
	@Column(name = "reject_time", columnDefinition = "datetime COMMENT '流程驳回时间'")
	private Date rejectTime;
	
	@Column(name = "terminate_time", columnDefinition = "datetime COMMENT '流程终止时间'")
	private Date terminateTime;
	
	@Column(name = "process_state", columnDefinition = "varchar(20) COMMENT '流程状态，创建、运行中、终止、结束、驳回、挂起等'")
	private String processState;
	
	@Column(name = "approve_type", columnDefinition = "varchar(20) COMMENT '审批状态，通过、不通过、驳回等'")
	private String approveType;
	
	@Column(name = "update_time", columnDefinition = "datetime COMMENT '流程更新时间'")
	private Date updateTime;
	
	@Column(name = "update_user_id", columnDefinition = "varchar(36) COMMENT '流程更新人'")
	private String updateUserId;
	
	@Column(name = "record_id", columnDefinition = "varchar(36) COMMENT '业务系统对应的唯一ID'")
	private String recordId;
	
	@Column(name = "_version", columnDefinition = "int(11) COMMENT '流程版本号，采用并发乐观锁'")
	private int version;
	
	@Column(name = "title", columnDefinition = "varchar(100) COMMENT '流程标题'")
	private String title;
	
	@Column(name = "reason", columnDefinition = "varchar(1000) COMMENT '流程发起事由'")
	private String reason;

	// 审批人为发起者，是否自动审批通过
	@Column(name = "promoter_approve_auto_pass", columnDefinition = "tinyint(1) default 0 COMMENT '审批人为发起者，是否自动审批通过，true自动通过，false不自动通过。默认为false'")
	private boolean paap = false;
	
	// 流程中相同的审批者，是否自动审批通过
	@Column(name = "same_approver_auto_pass", columnDefinition = "tinyint(1) default 0 COMMENT '流程中相同的审批者，之前节点审批通过，后续节点是否自动审批通过，true自动通过，false不自动通过。默认为false'")
	private boolean saap = false;
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getTemplateVersion() {
		return templateVersion;
	}

	public void setTemplateVersion(int templateVersion) {
		this.templateVersion = templateVersion;
	}

	public String getPromoter() {
		return promoter;
	}

	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}

	public Date getTerminateTime() {
		return terminateTime;
	}

	public void setTerminateTime(Date terminateTime) {
		this.terminateTime = terminateTime;
	}

	public String getProcessState() {
		return processState;
	}

	public void setProcessState(String processState) {
		this.processState = processState;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getApproveType() {
		return approveType;
	}

	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isPaap() {
		return paap;
	}

	public boolean isSaap() {
		return saap;
	}

	public void setPaap(boolean paap) {
		this.paap = paap;
	}

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

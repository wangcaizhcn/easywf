package com.easywf.wf.process.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 并发分支实体
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_process_concurrent",
indexes = {@Index(name = "idx_process_id",  columnList="process_id", unique = false),
		@Index(name = "idx_workitem_id",  columnList="workitem_id", unique = false),
		@Index(name = "idx_create_time",  columnList="create_time", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_process_concurrent", comment = "并发分支表")
public class ProcessConcurrentEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "process_id", columnDefinition = "varchar(36) COMMENT '流程实例ID'")
	private String processId;
	
	@Column(name = "create_time", columnDefinition = "datetime COMMENT '并发分支的创建时间'")
	private Date createTime;
	
	@Column(name = "finish_time", columnDefinition = "datetime COMMENT '并发分支的完成时间'")
	private Date finishTime;
	
	@Column(name = "parent_concurrent_id", columnDefinition = "varchar(36) COMMENT '所属上级并发分支ID'")
	private String parentConcurrentId;
	
	@Column(name = "workitem_id", columnDefinition = "varchar(36) COMMENT '并发的起始工作项ID'")
	private String workitemId;

	/**
	 * 获取并发分支ID
	 * @return 并发分支ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置并发分支ID
	 * @param id 并发分支ID
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
	 * 获取并发分支的创建时间
	 * @return 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置并发分支的创建时间
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取并发分支的完成时间
	 * @return 完成时间
	 */
	public Date getFinishTime() {
		return finishTime;
	}

	/**
	 * 设置并发分支的完成时间
	 * @param finishTime 完成时间
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	/**
	 * 获取所属上级并发分支的ID
	 * @return 上级并发分支ID
	 */
	public String getParentConcurrentId() {
		return parentConcurrentId;
	}

	/**
	 * 设置所属上级并发分支的ID
	 * @param parentConcurrentId 上级并发分支ID
	 */
	public void setParentConcurrentId(String parentConcurrentId) {
		this.parentConcurrentId = parentConcurrentId;
	}

	/**
	 * 获取并发起始的工作项ID
	 * @return 工作项ID
	 */
	public String getWorkitemId() {
		return workitemId;
	}

	/**
	 * 设置并发起始的工作项ID
	 * @param workitemId 工作项ID
	 */
	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}
	
}

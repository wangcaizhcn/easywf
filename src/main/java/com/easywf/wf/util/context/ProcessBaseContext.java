package com.easywf.wf.util.context;

import java.util.Date;

/**
 * 流程上下文基础对象
 * @author wangcai
 * @version 1.0
 */
public class ProcessBaseContext {

	// 模板ID
	private String templateId;
	
	// 流程ID
	private String processId;
	
	// 流程名称 -> 流程模板的名称
	private String processName;
	
	// 流程发起者
	private String promoter;
	
	// 流程启动时间
	private Date startTime;
	
	// 业务数据ID
	private String recordId;
	
	// 流程标题
	private String title;

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getPromoter() {
		return promoter;
	}

	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	
}

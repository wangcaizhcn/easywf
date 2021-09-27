package com.easywf.wf.process.bean;

import java.util.HashMap;
import java.util.Map;

public class WFProcessCreateBean {

	// 流程模板ID，流程模板升级后，给出baseTemplateId即可，可以保证同一个流程不同版本
	private String baseTemplateId;
	
	// 流程发起者
	private String promoter;
	
	// 业务ID
	private String recordId;
	
	// 流程标题
	private String title;
	
	// 流程原因
	private String reason;
	
	// 流程参数
	private Map<String, String> params = new HashMap<>();

	/**
	 * 添加参数
	 * @param property
	 * @param value
	 */
	public void addParam(String property, String value) {
		this.params.put(property, value);
	}
	
	public String getBaseTemplateId() {
		return baseTemplateId;
	}

	public String getPromoter() {
		return promoter;
	}

	public String getRecordId() {
		return recordId;
	}

	public String getTitle() {
		return title;
	}

	public String getReason() {
		return reason;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setBaseTemplateId(String baseTemplateId) {
		this.baseTemplateId = baseTemplateId;
	}

	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
}

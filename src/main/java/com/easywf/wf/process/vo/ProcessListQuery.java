package com.easywf.wf.process.vo;

import java.util.Date;

import com.easywf.wf.common.PageableQueryBean;
import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;

/**
 * 查询流程列表的查询对象<br>
 * 按照流程发起时间倒序排序
 * @author wangcai
 * @version 1.0
 */
public class ProcessListQuery extends PageableQueryBean {

	// 模板ID
	private String templateId;
	
	// 流程状态
	private WFProcessState processState;
	
	// 流程审批状态
	private WFProcessApproveState approveState;
	
	// 发起人姓名
	private String promoterName;
	
	// 发起人ID
	private String promoterId;
	
	// 提交开始时间
	private Date submitDateBegin;
	
	// 提交结束时间
	private Date submitDateEnd;
	
	// 流程结束开始时间
	private Date finishDateBegin;
	
	// 流程结束终止时间
	private Date finishDateEnd;
	
	// 审批结束起始时间
	private Date approveDateBegin;
	
	// 审批结束终止时间
	private Date approveDateEnd;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public WFProcessState getProcessState() {
		return processState;
	}

	public void setProcessState(WFProcessState processState) {
		this.processState = processState;
	}

	public WFProcessApproveState getApproveState() {
		return approveState;
	}

	public void setApproveState(WFProcessApproveState approveState) {
		this.approveState = approveState;
	}

	public String getPromoterName() {
		return promoterName;
	}

	public void setPromoterName(String promoterName) {
		this.promoterName = promoterName;
	}

	public String getPromoterId() {
		return promoterId;
	}

	public void setPromoterId(String promoterId) {
		this.promoterId = promoterId;
	}

	public Date getSubmitDateBegin() {
		return submitDateBegin;
	}

	public Date getSubmitDateEnd() {
		return submitDateEnd;
	}

	public Date getFinishDateBegin() {
		return finishDateBegin;
	}

	public Date getFinishDateEnd() {
		return finishDateEnd;
	}

	public Date getApproveDateBegin() {
		return approveDateBegin;
	}

	public Date getApproveDateEnd() {
		return approveDateEnd;
	}

	public void setSubmitDateBegin(Date submitDateBegin) {
		this.submitDateBegin = submitDateBegin;
	}

	public void setSubmitDateEnd(Date submitDateEnd) {
		this.submitDateEnd = submitDateEnd;
	}

	public void setFinishDateBegin(Date finishDateBegin) {
		this.finishDateBegin = finishDateBegin;
	}

	public void setFinishDateEnd(Date finishDateEnd) {
		this.finishDateEnd = finishDateEnd;
	}

	public void setApproveDateBegin(Date approveDateBegin) {
		this.approveDateBegin = approveDateBegin;
	}

	public void setApproveDateEnd(Date approveDateEnd) {
		this.approveDateEnd = approveDateEnd;
	}

}

package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 流程上下文对象
 * @author wangcai
 *
 */
public class ProcessContext extends ProcessBaseContext{

	// 流程结束时间 ->正常流程执行完成（通过、未通过）
	private Date finishTime;
	
	// 流程驳回时间
	private Date rejectTime;
	
	// 驳回人
	private String rejector;
	
	// 驳回人ID
	private String rejectorId;
	
	// 驳回意见
	private String opinions;
	
	// 流程结束时，总体审批结果, 通过，不通过，驳回等
	private String result;
	
	
	private String workitemId;
	
	// 审批详细内容
	private List<ApproveBaseContext> approveDetail = new ArrayList<>();

	// 添加一个审批明细
	public void addApproveDetail(ApproveBaseContext ac) {
		this.approveDetail.add(ac);
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<ApproveBaseContext> getApproveDetail() {
		return approveDetail;
	}

	public void setApproveDetail(List<ApproveBaseContext> approveDetail) {
		this.approveDetail = approveDetail;
	}

	public Date getRejectTime() {
		return rejectTime;
	}

	public void setRejectTime(Date rejectTime) {
		this.rejectTime = rejectTime;
	}

	public String getRejector() {
		return rejector;
	}

	public void setRejector(String rejector) {
		this.rejector = rejector;
	}

	public String getRejectorId() {
		return rejectorId;
	}

	public void setRejectorId(String rejectorId) {
		this.rejectorId = rejectorId;
	}

	public String getOpinions() {
		return opinions;
	}

	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}
	
}

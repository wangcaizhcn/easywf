package com.easywf.wf.process.bean;

public class ApproveBean {
	
	private String approver;
	
	private String workitemId;
	
	private String approveResult;
	
	private String opinions;

	public String getApprover() {
		return approver;
	}

	public String getWorkitemId() {
		return workitemId;
	}

	public String getApproveResult() {
		return approveResult;
	}

	public String getOpinions() {
		return opinions;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public void setApproveResult(String approveResult) {
		this.approveResult = approveResult;
	}

	public void setOpinions(String opinions) {
		this.opinions = opinions;
	}
}

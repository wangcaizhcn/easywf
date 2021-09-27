package com.easywf.wf.process.event;

import org.springframework.context.ApplicationEvent;

import com.easywf.wf.process.constant.WFProcessApproveState;

public class WorkitemPassEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1L;
	
	private WFProcessApproveState approveType;
	
	public WorkitemPassEvent(Object source) {
		super(source);
	}

	public WFProcessApproveState getApproveType() {
		return approveType;
	}

	public void setApproveType(WFProcessApproveState approveType) {
		this.approveType = approveType;
	}

}

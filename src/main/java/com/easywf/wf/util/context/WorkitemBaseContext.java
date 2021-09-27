package com.easywf.wf.util.context;

import com.easywf.wf.template.constant.WFNodeHandleMode;

/**
 * 工作项上下文基础对象
 * @author wangcai
 * @version 1.0
 */
public class WorkitemBaseContext {

	// 工作项ID
	private String workitemId;
	
	// 节点ID
	private String nodeId;
	
	// 节点名称
	private String name;
	
	// 审批类型
	private WFNodeHandleMode approveType;

	public String getWorkitemId() {
		return workitemId;
	}

	public void setWorkitemId(String workitemId) {
		this.workitemId = workitemId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public WFNodeHandleMode getApproveType() {
		return approveType;
	}

	public void setApproveType(WFNodeHandleMode approveType) {
		this.approveType = approveType;
	}

}

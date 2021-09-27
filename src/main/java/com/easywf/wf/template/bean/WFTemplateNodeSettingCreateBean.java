package com.easywf.wf.template.bean;

public class WFTemplateNodeSettingCreateBean {

	// 节点ID
	private String nodeId; 
	
	// 审批模式，竞争办理、多人办理
	private String approvalMode;
	
	// 审批人选择策略，角色找人、组织机构找人等
	private String strategy;
	
	// 自定义实现类找审批人的服务类
	private String approverService;
	
	// 角色列表
	private String roles;
	
	// 审批表单地址
	private String formURL;
	
	// 节点办理类型，审批、意见（会签）
	private String handleMode;
	
	// 节点通过类型，单人通过、半数通过等
	private String passType;
	
	// 节点功能，通过、不通过、驳回等
	private String functions;
	
	// 组织机构下找人模式，依据发起人、依据上一个审批人等
	private String orgRoleMode;
	
	// 组织机构下找人时，是否向上级找人
	private Boolean upSearch;
	
	// 参数指定部门ID
	private String deptParamId;
	
	// 参数指定审批人
	private String userParamId;
	
	// 自定义节点通过的实现类
	private String passService;

	public String getNodeId() {
		return nodeId;
	}

	public String getApprovalMode() {
		return approvalMode;
	}

	public String getStrategy() {
		return strategy;
	}

	public String getApproverService() {
		return approverService;
	}

	public String getRoles() {
		return roles;
	}

	public String getFormURL() {
		return formURL;
	}

	public String getHandleMode() {
		return handleMode;
	}

	public String getPassType() {
		return passType;
	}

	public String getFunctions() {
		return functions;
	}

	public String getOrgRoleMode() {
		return orgRoleMode;
	}

	public Boolean getUpSearch() {
		return upSearch == null ? false : upSearch;
	}

	public String getPassService() {
		return passService;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public void setApprovalMode(String approvalMode) {
		this.approvalMode = approvalMode;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public void setApproverService(String approverService) {
		this.approverService = approverService;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public void setFormURL(String formURL) {
		this.formURL = formURL;
	}

	public void setHandleMode(String handleMode) {
		this.handleMode = handleMode;
	}

	public void setPassType(String passType) {
		this.passType = passType;
	}

	public void setFunctions(String functions) {
		this.functions = functions;
	}

	public void setOrgRoleMode(String orgRoleMode) {
		this.orgRoleMode = orgRoleMode;
	}

	public void setUpSearch(Boolean upSearch) {
		this.upSearch = upSearch;
	}

	public void setPassService(String passService) {
		this.passService = passService;
	}

	public String getDeptParamId() {
		return deptParamId;
	}

	public String getUserParamId() {
		return userParamId;
	}

	public void setDeptParamId(String deptParamId) {
		this.deptParamId = deptParamId;
	}

	public void setUserParamId(String userParamId) {
		this.userParamId = userParamId;
	}
	
}

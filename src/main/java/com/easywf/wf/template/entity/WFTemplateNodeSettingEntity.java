package com.easywf.wf.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 节点审批设置实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template_node_setting",
	indexes = {@Index(name = "idx_node_id",  columnList="node_id", unique = true)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template_node_setting", comment = "审批类型节点配置表")
public class WFTemplateNodeSettingEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "node_id", columnDefinition = "varchar(36) COMMENT '节点ID'")
	private String nodeId;
	
	@Column(name = "strategy", columnDefinition = "varchar(20) COMMENT '审批人选择策略，角色找人、组织机构找人等'")
	private String strategy;
	
	@Column(name = "approver_service", columnDefinition = "varchar(255) COMMENT '自定义实现类找审批人的服务类'")
	private String approverService;
	
	@Column(name = "roles", columnDefinition = "varchar(2000) COMMENT '角色中找审批人，指定角色列表'")
	private String roles;
	
	@Column(name = "approval_mode", columnDefinition = "varchar(20) COMMENT '审批模式，竞争办理、多人办理'")
	private String approvalMode;

	@Column(name = "form_url", columnDefinition = "varchar(2000) COMMENT '审批页面表单地址'")
	private String formURL;
	
	@Column(name = "handle_mode", columnDefinition = "varchar(20) COMMENT '节点办理类型，审批、意见（会签）'")
	private String handleMode;
	
	@Column(name = "pass_type", columnDefinition = "varchar(20) COMMENT '节点通过类型，单人通过、半数通过等'")
	private String passType;
	
	@Column(name = "functions", columnDefinition = "varchar(500) COMMENT '节点功能，通过、不通过、驳回等'")
	private String functions;
	
	@Column(name = "org_role_mode", columnDefinition = "varchar(20) COMMENT '组织机构下找人模式，依据发起人、依据上一个审批人等'")
	private String orgRoleMode = "";
	
	@Column(name = "up_search", columnDefinition = "tinyint(1) default 0 COMMENT '组织机构下找人时，是否向上级找人， false不再向上级找人，true向上级找人，默认为false'")
	private boolean upSearch;
	
	@Column(name = "dept_param_id", columnDefinition = "varchar(2000) COMMENT '参数指定部门ID'")
	private String deptParamId;
	
	@Column(name = "user_param_id", columnDefinition = "varchar(2000) COMMENT '参数指定审批人ID'")
	private String userParamId;
	
	@Column(name = "pass_service", columnDefinition = "varchar(255) COMMENT '自定义节点通过的实现类'")
	private String passService;
	
	/**
	 * 获取审批设置实体ID
	 * @return 审批设置实体ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置审批设置项实体ID
	 * @param id 审批设置项实体ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取节点ID
	 * @return 节点ID
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * 设置节点ID
	 * @param nodeId 节点ID
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * 获取选人实现类
	 * @return 选人实现类
	 */
	public String getApproverService() {
		return approverService;
	}

	/**
	 * 设置选人实现类
	 * @param userService 选人实现类
	 */
	public void setApproverService(String approverService) {
		this.approverService = approverService;
	}

	/**
	 * 获取审批表单URL
	 * @return 审批表单URL
	 */
	public String getFormURL() {
		return formURL;
	}

	/**
	 * 设置审批表单URL
	 * @param formURL 审批表单URL
	 */
	public void setFormURL(String formURL) {
		this.formURL = formURL;
	}

	/**
	 * 获取审批类型
	 * @return 审批类型，参考{@link com.easywf.wf.template.constant.WFNodeHandleMode}
	 */
	public String getHandleMode() {
		return handleMode;
	}

	/**
	 * 设置审批类型
	 * @param approvalType 审批类型，参考{@link com.easywf.wf.template.constant.WFNodeHandleMode}
	 */
	public void setHandleMode(String handleMode) {
		this.handleMode = handleMode;
	}

	/**
	 * 获取工作项通过类型
	 * @return 工作项通过类型，参考{@link com.easywf.wf.template.constant.WFApprovalPassType}
	 */
	public String getPassType() {
		return passType;
	}

	/**
	 * 设置工作项通过类型
	 * @param passType 工作项通过类型，参考{@link com.easywf.wf.template.constant.WFApprovalPassType}
	 */
	public void setPassType(String passType) {
		this.passType = passType;
	}

	/**
	 * 获取节点办理功能<br>
	 * 多个办理功能采用英文逗号分隔
	 * @return 办理功能，参考{@link com.easywf.wf.template.constant.WFFunctions}
	 */
	public String getFunctions() {
		return functions;
	}

	/**
	 * 设置节点办理功能<br>
	 * 支持多个办理功能，采用英文逗号分隔
	 * @param functions 办理功能，参考{@link com.easywf.wf.template.constant.WFFunctions}
	 */
	public void setFunctions(String functions) {
		this.functions = functions;
	}

	/**
	 * 获取角色选人、组织机构角色选人设置的角色ID<br>
	 * 多个角色采用英文逗号分隔
	 * @return 角色ID
	 */
	public String getRoles() {
		return roles;
	}

	/**
	 * 设置角色选人、组织机构角色选人的角色ID<br>
	 * 多个角色采用英文逗号分隔
	 * @param roles 角色ID
	 */
	public void setRoles(String roles) {
		this.roles = roles;
	}

	/**
	 * 获取选择审批人策略设置
	 * @return 选择审批人策略，参考 {@link com.easywf.wf.template.constant.WFApproverStrategy}
	 */
	public String getStrategy() {
		return strategy;
	}

	/**
	 * 设置选择审批人策略
	 * @param strategy 选择审批人策略，参考 {@link com.easywf.wf.template.constant.WFApproverStrategy}
	 */
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	/**
	 * 获取审批模式
	 * @return 审批模式，参考 {@link com.easywf.wf.template.constant.WFApprovalMode}
	 */
	public String getApprovalMode() {
		return approvalMode;
	}

	/**
	 * 设置审批模式
	 * @param approvalMode 审批模式，参考 {@link com.easywf.wf.template.constant.WFApprovalMode}
	 */
	public void setApprovalMode(String approvalMode) {
		this.approvalMode = approvalMode;
	}

	/**
	 * 获取组织机构的获取方式
	 * @return 组织机构的获取方式，参考{@link com.easywf.wf.template.constant.WFOrgRoleMode}
	 */
	public String getOrgRoleMode() {
		return orgRoleMode;
	}

	/**
	 * 设置组织机构的获取方式
	 * @param orgRoleMode 组织机构的获取方式，参考{@link com.easywf.wf.template.constant.WFOrgRoleMode}
	 */
	public void setOrgRoleMode(String orgRoleMode) {
		this.orgRoleMode = orgRoleMode;
	}

	/**
	 * 根据参数指定部门，获取该参数ID
	 * @return 参数ID
	 */
	public String getDeptParamId() {
		return deptParamId;
	}

	/**
	 * 设置置顶部门的参数ID
	 * @param paramId 参数ID
	 */
	public void setDeptParamId(String deptParamId) {
		this.deptParamId = deptParamId;
	}

	/**
	 * 组织机构是否支持向上级查找
	 * @return true: 支持向上级组织继续查找符合规则的审批人<br>
	 * false:只在当前组织机构中查找符合规则的审批人
	 */
	public boolean isUpSearch() {
		return upSearch;
	}

	/**
	 * 设置是否支持向上级组织机构查找符合规则的审批人
	 * @param upSearch true: 支持向上级组织继续查找符合规则的审批人<br>
	 * false:只在当前组织机构中查找符合规则的审批人
	 */
	public void setUpSearch(boolean upSearch) {
		this.upSearch = upSearch;
	}

	/**
	 * 获取自定义通过方式的实现类
	 * @return 自定义通过方式的实现类
	 */
	public String getPassService() {
		return passService;
	}

	/**
	 * 设置自定义通过方式的实现类
	 * @param passService 自定义通过方式的实现类
	 */
	public void setPassService(String passService) {
		this.passService = passService;
	}

	/**
	 * 根据参数指定审批人，获取该参数ID
	 * @return
	 */
	public String getUserParamId() {
		return userParamId;
	}

	/**
	 * 根据参数指定审批人，设置审批人的参数
	 * @param userParamIds
	 */
	public void setUserParamId(String userParamId) {
		this.userParamId = userParamId;
	}
	
}

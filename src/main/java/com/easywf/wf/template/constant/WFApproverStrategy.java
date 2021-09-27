package com.easywf.wf.template.constant;

/**
 * 节点选择审批人策略
 * @author wangcai
 * @version 1.0
 */
public enum WFApproverStrategy {
	/**
	 * 手动选择审批人
	 */
	MANUALLY, 
	/**
	 * 在指定角色中选择审批人
	 */
	ROLE, 
	/**
	 * 在组织机构下选择审批人
	 */
	ORG, 
	/**
	 * 在组织机构下按照角色选择审批人
	 */
	ORG_ROLE, 
	/**
	 * 流程参数中指定审批人
	 */
	PARAMUSER,
	/**
	 * 自定义实现选择审批人，实现类继承接口 {@link com.easywf.wf.util.ApproveUserService}
	 */
	SERVICE;
	
	public static boolean include(String strategy){
        boolean include = false;
        for (WFApproverStrategy as : WFApproverStrategy.values()){
            if(as.toString().equals(strategy)){
                include = true;
                break;
            }
        }
        return include;
    }
}

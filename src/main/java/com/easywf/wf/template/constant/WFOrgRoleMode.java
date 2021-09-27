package com.easywf.wf.template.constant;

/**
 * 组织机构下角色选人方式
 * @author wangcai
 * @version 1.0
 */
public enum WFOrgRoleMode {
	/**
	 * 根据发起人的组织机构查找
	 */
	PROMOTER, 
	/**
	 * 根据上个审批者的组织机构查找
	 */
	APPROVER,
	/**
	 * 从流程参数中的组织机构查找
	 */
	PARAMDEPT;
	
	public static boolean include(String mode){
        boolean include = false;
        for (WFOrgRoleMode orm : WFOrgRoleMode.values()){
            if(orm.toString().equals(mode)){
                include = true;
                break;
            }
        }
        return include;
    }
}

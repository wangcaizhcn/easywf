package com.easywf.wf.template.constant;

/**
 * 审批模式
 * @author wangcai
 * @version 1.0
 */
public enum WFApprovalMode {
	/**
	 * 竞争办理
	 */
	COMPETE, 
	/**
	 * 多人办理
	 */
	MULTI;
	
	public static boolean include(String mode){
        boolean include = false;
        for (WFApprovalMode am : WFApprovalMode.values()){
            if(am.toString().equals(mode)){
                include = true;
                break;
            }
        }
        return include;
    }
}

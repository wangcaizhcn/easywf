package com.easywf.wf.template.constant;

/**
 * 节点审批类型
 * @author wangcai
 * @version 1.0
 */
public enum WFNodeHandleMode {
	/**
	 * 审批
	 */
	APPROVAL, 
	/**
	 * 意见（会签）
	 */
	OPINION;
	
	public static boolean include(String mode){
        boolean include = false;
        for (WFNodeHandleMode nhm : WFNodeHandleMode.values()){
            if(nhm.toString().equals(mode)){
                include = true;
                break;
            }
        }
        return include;
    }
}

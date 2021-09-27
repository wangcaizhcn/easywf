package com.easywf.wf.template.constant;

/**
 * 多人审批，工作项通过方式
 * @author wangcai
 * @version 1.0
 */
public enum WFApprovalPassType {
	/**
	 * 单人通过，工作项通过
	 */
	SINGLE, 
	/**
	 * 半数通过，工作项通过
	 */
	HALF, 
	/**
	 * 全体通过，工作项通过
	 */
	ALL, 
	/**
	 * 自定义通过方式，需实现WorkitemPassService接口
	 */
	SERVICE;
	
	public static boolean include(String type){
        boolean include = false;
        for (WFApprovalPassType apt : WFApprovalPassType.values()){
            if(apt.toString().equals(type)){
                include = true;
                break;
            }
        }
        return include;
    }
	
}

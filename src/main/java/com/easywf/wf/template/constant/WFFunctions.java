package com.easywf.wf.template.constant;

/**
 * 审批的功能项
 * @author wangcai
 * @version 1.0
 */
public enum WFFunctions {
	/**
	 * 通过按钮
	 */
	PASS, 
	/**
	 * 不通过按钮
	 */
	FAILED, 
	/**
	 * 意见输入框
	 */
	OPINION, 
	/**
	 * 驳回按钮
	 */
	REJECT, 
	/**
	 * 转办按钮
	 */
	TRANSFER, 
	/**
	 * 协办按钮
	 */
	COOPERATE;
	
	public static boolean include(String function){
        boolean include = false;
        for (WFFunctions func : WFFunctions.values()){
            if(func.toString().equals(function)){
                include = true;
                break;
            }
        }
        return include;
    }
}

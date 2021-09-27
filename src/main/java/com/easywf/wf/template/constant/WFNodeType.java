package com.easywf.wf.template.constant;

/**
 * 节点类型
 * @author wangcai
 * @version 1.0
 */
public enum WFNodeType {
	/**
	 * 开始节点
	 */
	BEGIN, 
	/**
	 * 结束节点
	 */
	END, 
	/**
	 * 自动节点
	 */
	AUTO, 
	/**
	 * 手动审批节点
	 */
	WORK, 
	/**
	 * 条件节点
	 */
	CONDITION, 
	/**
	 * 并发开始节点
	 */
	CONCURRENTBEGIN, 
	/**
	 * 并发结束节点
	 */
	CONCURRENTEND,
	/**
	 * 子流程
	 */
	CHILD;
	
	public static boolean include(String type){
        boolean include = false;
        for (WFNodeType np : WFNodeType.values()){
            if(np.toString().equals(type)){
                include = true;
                break;
            }
        }
        return include;
    }
	
	/*
	 * public static boolean include(String code){
        boolean include = false;
        for (BusinessTypeEnum pte : BusinessTypeEnum.values()){
            if(pte.getCode().equals(code)){
                include = true;
                break;
            }
        }
        return include;
    }
	 */
}

package com.easywf.wf.template.constant;

/**
 * 参数值类型
 * @author wangcai
 * @version 1.0
 */
public enum WFParamValueType {
	/**
	 * 字符串
	 */
	STRING, 
	/**
	 * 整数
	 */
	LONG, 
	/**
	 * 浮点数
	 */
	DOUBLE, 
	/**
	 * 日期，支持YYYY-MM-DD格式
	 */
	DATE, 
	/**
	 * 布尔类型
	 */
	BOOLEAN;
	
	public static boolean include(String type){
        boolean include = false;
        for (WFParamValueType pvt : WFParamValueType.values()){
            if(pvt.toString().equals(type)){
                include = true;
                break;
            }
        }
        return include;
    }
}

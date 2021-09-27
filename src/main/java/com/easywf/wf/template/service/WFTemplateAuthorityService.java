package com.easywf.wf.template.service;

/**
 * 流程模板相关操作，校验用户权限的接口<br>
 * 三方系统集成后，需要实现该接口，并声明成Spring的Bean
 * @author wangcai
 * @version 1.0
 */
public interface WFTemplateAuthorityService {

	/**
	 * 查询操作类型
	 */
	public static final String OPERATETYPE_SELECT = "OPERATETYPE_SELECT";
	
	/**
	 * 新增操作类型
	 */
	public static final String OPERATETYPE_ADD = "OPERATETYPE_ADD";
	
	/**
	 * 修改操作类型
	 */
	public static final String OPERATETYPE_MODIFY = "OPERATETYPE_MODIFY";
	
	/**
	 * 删除操作类型
	 */
	public static final String OPERATETYPE_DELETE = "OPERATETYPE_DELETE";
	
	/**
	 * 操作功能 模板
	 */
	public static final String OPERATEFUNCTION_TEMPLATE = "OPERATEFUNCTION_TEMPLATE";
	
	/**
	 * 操作功能 参数
	 */
	public static final String OPERATEFUNCTION_PARAM = "OPERATEFUNCTION_PARAM";
	
	/**
	 * 操作功能 节点
	 */
	public static final String OPERATEFUNCTION_NODE = "OPERATEFUNCTION_NODE";
	
	/**
	 * 操作功能 关系
	 */
	public static final String OPERATEFUNCTION_LINE = "OPERATEFUNCTION_LINE";
	
	/**
	 * 校验权限方法
	 * @param operateType  操作类型， 参考本接口常量
	 * @param operateFunction  操作功能， 参考本接口常量
	 * @return true用户有权限， false用户没有权限
	 */
	boolean check(String operateType, String operateFunction);
}

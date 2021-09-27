package com.easywf.wf.template.constant;

/**
 * 条件和逻辑操作符
 * @author wangcai
 * @version 1.0
 */
public interface WFOperator {

	/**
	 * 等于
	 */
	public static final String EQUAL = "=";
	
	/**
	 * 不等于
	 */
	public static final String NOT_EQUAL = "!=";
	
	/**
	 * 大于
	 */
	public static final String LARGE_THAN = ">";
	
	/**
	 * 大于等于
	 */
	public static final String LARGE_EQUAL_THAN = ">=";
	
	/**
	 * 小于
	 */
	public static final String LESS_THAN = "<";
	
	/**
	 * 小于等于
	 */
	public static final String LESS_EQUAL_THAN = "<=";
	
	/**
	 * 逻辑与
	 */
	public static final String OP_AND = "&";
	
	/**
	 * 逻辑或
	 */
	public static final String OP_OR = "|";
	
	/**
	 * 表达式总体取反
	 */
	public static final String OP_INVERT = "@!";
	
	/**
	 * 表示变量的前缀标识
	 */
	public static final String VARIABLE_PREFIX = "{{";
	
	/**
	 * 表示变量的后缀标识
	 */
	public static final String VARIABLE_SUFFIX = "}}";
}

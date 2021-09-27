package com.easywf.wf.common;

/**
 * 系统接口返回的代码表
 * @author wangcai
 * @version 1.0
 */
public class ResultCodeConstant {

	/**
	 * 系统异常
	 */
	public static final String SYSTEM_ERROR = "-1";
	
	/**
	 * 请求成功
	 */
	public static final String REQUEST_SUCCESS = "0";
	
	/**
	 * 校验异常
	 */
	public static final String ARGUMENT_INVALID_ERROR = "invalid_error";
	
	/**
	 * 请求不合法
	 */
	public static final String REQUEST_ERROR = "request_error";
	
	/**
	 *  用户权限不足
	 */
	public static final String AUTHORITY_ERROR = "authority_error";
	
	public static final String PROCESS_IS_NOT_EXISTS = "process_is_not_exists";
	
	
	public static final String PROCESS_STATE_ERROR = "process_state_error";
}

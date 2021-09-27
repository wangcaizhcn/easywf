package com.easywf.wf.util.service;

/**
 * 获取当前登录用户ID接口
 * @author wangcai
 * @version 1.0
 */
public interface WFLoginUserService {

	/**
	 * 获取当前登录的用户ID
	 * @return 登录用户ID
	 */
	String getLoginUserId();
}

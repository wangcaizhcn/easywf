package com.easywf.wf.process.service;

import java.util.List;

/**
 * 组织机构相关方法接口类
 * @author wangcai
 * @version 1.0
 */
public interface OrgRuleService {

	/**
	 * 获取给定组织机构的全部上级组织机构ID包含自己
	 * @param orgId 组织机构ID
	 * @return 所有上级组织机构ID
	 */
	List<String> allParentId(String orgId);
}

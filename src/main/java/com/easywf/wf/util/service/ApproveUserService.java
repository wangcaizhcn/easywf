package com.easywf.wf.util.service;

import java.util.List;

import com.easywf.wf.util.context.ApproveUserContext;

/**
 * 自定义选择办理人的接口，实现类需实现该接口
 * @author wangcai
 * @version 1.0
 */
public interface ApproveUserService {

	List<String> findUsers(ApproveUserContext auc);
}

package com.easywf.wf.util.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.easywf.wf.resources.entity.UserRoleEntity;
import com.easywf.wf.resources.repository.UserRoleJpaRepo;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.repository.WFTemplateNodeSettingJpaRepo;
import com.easywf.wf.util.context.ApproveUserContext;

@Service("role_user_approve")
public class RoleApproverServiceImpl implements ApproveUserService {

	@Autowired
	private WFTemplateNodeSettingJpaRepo wfTemplateNodeSettingJpaRepo;
	
	@Autowired
	@Qualifier("wfUserRoleJpaRepo")
	private UserRoleJpaRepo userRoleJpaRepo;
	
	@Override
	public List<String> findUsers(ApproveUserContext auc) {
		
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(auc.getWorkitem().getNodeId());
		// 角色选人，获取该角色对应的用户
		List<UserRoleEntity> userRoles = userRoleJpaRepo.findByRoleIdIn(Arrays.asList(nodeSetting.getRoles().split(",")));
		
		// 将用户去重
		List<String> approvers = new ArrayList<>();
		for(UserRoleEntity userRole : userRoles) {
			approvers.add(userRole.getUserId());
		}
		return approvers;
	}

}

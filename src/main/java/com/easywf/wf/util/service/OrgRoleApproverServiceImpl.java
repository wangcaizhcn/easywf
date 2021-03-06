package com.easywf.wf.util.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.easywf.wf.process.entity.ProcessParamEntity;
import com.easywf.wf.process.entity.WorkitemEntity;
import com.easywf.wf.process.repository.ProcessJpaRepo;
import com.easywf.wf.process.repository.ProcessParamJpaRepo;
import com.easywf.wf.process.repository.WorkitemApproverJpaRepo;
import com.easywf.wf.process.repository.WorkitemJpaRepo;
import com.easywf.wf.process.service.OrgRuleService;
import com.easywf.wf.resources.entity.UserOrgEntity;
import com.easywf.wf.resources.entity.UserRoleEntity;
import com.easywf.wf.resources.repository.UserOrgJpaRepo;
import com.easywf.wf.resources.repository.UserRoleJpaRepo;
import com.easywf.wf.template.constant.WFOrgRoleMode;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.entity.WFTemplateParamEntity;
import com.easywf.wf.template.repository.WFTemplateNodeSettingJpaRepo;
import com.easywf.wf.template.repository.WFTemplateParamJpaRepo;
import com.easywf.wf.util.context.ApproveUserContext;

@Service("org_role_user_approve")
public class OrgRoleApproverServiceImpl implements ApproveUserService {

	@Autowired
	@Qualifier("wfUserRoleJpaRepo")
	private UserRoleJpaRepo userRoleJpaRepo;
	
	@Autowired
	private ProcessJpaRepo processJpaRepo;
	
	@Autowired
	private WorkitemJpaRepo workitemJpaRepo;
	
	@Autowired
	private WorkitemApproverJpaRepo workitemApproverJpaRepo;
	
	@Autowired
	private ProcessParamJpaRepo processParamJpaRepo;
	
	@Autowired
	private WFTemplateParamJpaRepo wfTemplateParamJpaRepo;
	
	@Autowired
	@Qualifier("wfUserOrgJpaRepo")
	private UserOrgJpaRepo userOrgJpaRepo;
	
	@Autowired
	private OrgRuleService orgRuleService;
	
	@Autowired
	private WFTemplateNodeSettingJpaRepo wfTemplateNodeSettingJpaRepo;
	
	@Override
	public List<String> findUsers(ApproveUserContext auc) {
		
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(auc.getWorkitem().getNodeId());
		
		String userId = "";
		if(WFOrgRoleMode.PROMOTER.toString().equals(nodeSetting.getOrgRoleMode())) {
			userId = processJpaRepo.findById(auc.getProcessId()).get().getPromoter();
		}else if(WFOrgRoleMode.APPROVER.toString().equals(nodeSetting.getOrgRoleMode())) {
			
			// TODO ???????????????????????????????????????????????????????????????????????????
			List<WorkitemEntity> workitems = workitemJpaRepo.findByProcessId(auc.getProcessId());
			List<String> workitemIds = new ArrayList<>();
			for(WorkitemEntity entry : workitems) {
				workitemIds.add(entry.getId());
			}
			userId = workitemApproverJpaRepo.findLastApprover(workitemIds);
		}
		
		List<String> orgId = new ArrayList<>();
		if(WFOrgRoleMode.PARAMDEPT.toString().equals(nodeSetting.getOrgRoleMode())) {
			WFTemplateParamEntity param = wfTemplateParamJpaRepo.findById(nodeSetting.getDeptParamId()).get();
			ProcessParamEntity pp =  processParamJpaRepo.findByProcessIdAndProperty(auc.getProcessId(), param.getProperty());
			if(pp != null) {
				// TODO ???????????????????????????
				orgId.add(pp.getValue());
			}else {
				// TODO ??????????????????????????????
			}
		}else {
			List<UserOrgEntity> userOrgs = userOrgJpaRepo.findByUserId(userId);
			for(UserOrgEntity userOrg :userOrgs) {
				orgId.add(userOrg.getOrgId());
			}
		}
		
		Set<String> orgList = new HashSet<>();
		// ????????????????????????
		if(nodeSetting.isUpSearch() == true) {
			
			// TODO ????????????????????????????????????
			orgList.addAll(orgRuleService.allParentId(orgId.get(0)));
			/*
			List<OrgEntity> orgs = orgJpaRepo.findByIdIn(orgId);
			for(OrgEntity org : orgs) {
				String[] orgIds = org.getPath().split("/");
				for(String id : orgIds) {
					if(StringUtils.isNotBlank(id)) {
						orgList.add(id);
					}
				}
			}*/
		}
		
//		orgList.addAll(orgId);
		
		// ?????????????????????????????????????????????????????????????????????
		List<UserRoleEntity> userRoles = userRoleJpaRepo.findOrgRole(orgId, Arrays.asList(nodeSetting.getRoles().split(",")));
		
		List<String> approvers = new ArrayList<>();
		for(UserRoleEntity userRole : userRoles) {
			approvers.add(userRole.getUserId());
		}
		
		// ???????????????????????????????????????????????????????????????????????????
		
		if(CollectionUtils.isEmpty(approvers)) {
			userRoles = userRoleJpaRepo.findOrgRole(new ArrayList<String>(orgList), Arrays.asList(nodeSetting.getRoles().split(",")));
			// ???????????????
			for(UserRoleEntity userRole : userRoles) {
				approvers.add(userRole.getUserId());
			}
		}
		
		return approvers;
	}
}

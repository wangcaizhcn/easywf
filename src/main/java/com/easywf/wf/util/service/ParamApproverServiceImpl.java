package com.easywf.wf.util.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easywf.wf.process.entity.ProcessParamEntity;
import com.easywf.wf.process.repository.ProcessParamJpaRepo;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.entity.WFTemplateParamEntity;
import com.easywf.wf.template.repository.WFTemplateNodeSettingJpaRepo;
import com.easywf.wf.template.repository.WFTemplateParamJpaRepo;
import com.easywf.wf.util.context.ApproveUserContext;

@Service("param_user_approve")
public class ParamApproverServiceImpl implements ApproveUserService {

	@Autowired
	private WFTemplateParamJpaRepo wfTemplateParamJpaRepo;
	
	@Autowired
	private ProcessParamJpaRepo processParamJpaRepo;
	
	@Autowired
	private WFTemplateNodeSettingJpaRepo wfTemplateNodeSettingJpaRepo;
	
	@Override
	public List<String> findUsers(ApproveUserContext auc) {
		
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(auc.getWorkitem().getNodeId());
		
		WFTemplateParamEntity param = wfTemplateParamJpaRepo.findById(nodeSetting.getUserParamId()).get();
		ProcessParamEntity pp =  processParamJpaRepo.findByProcessIdAndProperty(auc.getProcessId(), param.getProperty());
		
		List<String> approvers = new ArrayList<>();
		for(String str : pp.getValue().split(",")) {
			approvers.add(str);
		}
		return approvers;
	}

}

package com.easywf.wf.resources.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.ApiResult;
import com.easywf.wf.common.ApiResultTemplate;
import com.easywf.wf.resources.entity.RoleEntity;
import com.easywf.wf.resources.entity.UserEntity;
import com.easywf.wf.resources.service.WFResourcesService;

@RestController
public class ResourcesController {

	@Autowired
	private WFResourcesService wfResourcesService;
	
	@GetMapping("/wf/resources/role/list")
	public ApiResult<List<RoleEntity>> getRoleList(){
		List<RoleEntity> list = wfResourcesService.getRoleList();
		return ApiResultTemplate.success(list);
	}
	
	@GetMapping("/wf/resources/user/list")
	public ApiResult<List<UserEntity>> getUserList(){
		List<UserEntity> users = wfResourcesService.getUserList();
		return ApiResultTemplate.success(users);
	}
}

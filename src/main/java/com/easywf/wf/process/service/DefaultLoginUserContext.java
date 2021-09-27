package com.easywf.wf.process.service;

import org.springframework.stereotype.Service;

import com.easywf.wf.common.LoginUserContext;

@Service
public class DefaultLoginUserContext implements LoginUserContext {

	@Override
	public String getLoginUserId() {
		return null;
	}

}

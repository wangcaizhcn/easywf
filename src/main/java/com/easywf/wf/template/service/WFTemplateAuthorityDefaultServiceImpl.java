package com.easywf.wf.template.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WFTemplateAuthorityDefaultServiceImpl implements WFTemplateAuthorityService {

	private final static Logger log = LoggerFactory.getLogger(WFTemplateAuthorityDefaultServiceImpl.class);
	
	@Override
	public boolean check(String operateType, String operateFunction) {
		log.debug("operateType = " + operateType + ", operateFunction = " + operateFunction);
		return true;
	}

}

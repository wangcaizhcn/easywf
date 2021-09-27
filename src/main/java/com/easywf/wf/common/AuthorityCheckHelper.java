package com.easywf.wf.common;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.easywf.wf.template.service.WFTemplateAuthorityService;

@Aspect
@Component
public class AuthorityCheckHelper {

	private final static Logger log = LoggerFactory.getLogger(AuthorityCheckHelper.class);
	
	@Autowired(required = false)
	private WFTemplateAuthorityService wfTemplateAuthorityService;
	
	@Pointcut("@annotation(com.easywf.wf.common.AuthorityCheck)")
	public void pointcut() {};
	
	@Before("pointcut()")
	public void check(JoinPoint joinPoint) throws Throwable {
		
		log.info("=======AuthorityCheckHelper=======");
		
		if(wfTemplateAuthorityService != null) {
			Class<?> className = joinPoint.getTarget().getClass(); 
	        String methodName = joinPoint.getSignature().getName();  
	        
	        Class<?>[] argClass = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
			Method method = className.getDeclaredMethod(methodName, argClass);
			
			AuthorityCheck annotation = method.getAnnotation(AuthorityCheck.class);
			String operateType = annotation.operateType();
			String operateFunction = annotation.operateFunction();
			
			if(!StringUtils.isAnyBlank(operateType, operateFunction)) {
				boolean flag = wfTemplateAuthorityService.check(operateType, operateFunction);
				if(flag == false) {
					throw new EasyWFException(ResultCodeConstant.AUTHORITY_ERROR);
				}
			}
		}
	}
	
}

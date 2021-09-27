package com.easywf.wf.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class WFSpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		WFSpringContextUtil.applicationContext = applicationContext;
	}

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    public static Object getBean(String name) throws BeansException {  
        return applicationContext.getBean(name);
    }
    
}

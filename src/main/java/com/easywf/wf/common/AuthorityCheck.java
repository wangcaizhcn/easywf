package com.easywf.wf.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解
 * @author 王财
 * @date 2021年7月23日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthorityCheck {

	/**
	 *  操作类型  参考{@link com.easywf.wf.template.service.WFTemplateAuthorityService}
	 * @return
	 */
	String operateType()  default "";
	
	/**
	 *  操作方法 参考{@link com.easywf.wf.template.service.WFTemplateAuthorityService}
	 * @return
	 */
	String operateFunction()  default "";
}

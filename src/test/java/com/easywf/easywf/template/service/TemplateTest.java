package com.easywf.easywf.template.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.easywf.wf.EasywfApplication;
import com.easywf.wf.template.bean.WFTemplateCreateBean;
import com.easywf.wf.template.service.WFTemplateService;

// 高版本使用 junit-jupiter(貌似springboot 2.2版本开始)， 不在需要@RunWith注解
//@RunWith(SpringRunner.class)
//@ExtendWith(SpringExtension.class)  等价@RunWith， 但可以不写， Junit5开始
@SpringBootTest(classes = EasywfApplication.class)
public class TemplateTest {
	
	@Autowired
	private WFTemplateService wfTemplateService;
	
	// Junit5 以后采用assertThrows
//	@Rule
//	public ExpectedException expected = ExpectedException.none();
	
	@Test
	public void createTemplateWithException() {
		
		// template.name is null
		WFTemplateCreateBean templateBean = new WFTemplateCreateBean();
		templateBean.setDescription("description");
		
		Exception exception = assertThrows(IllegalArgumentException.class, 
				() -> wfTemplateService.createTemplate(templateBean));
		assertEquals("模板名称不能为空", exception.getMessage());
		
		// template.name is ""
		WFTemplateCreateBean templateBean1 = new WFTemplateCreateBean();
		templateBean1.setDescription("description");
		templateBean1.setName("");
		
		exception = assertThrows(IllegalArgumentException.class,
				() -> wfTemplateService.createTemplate(templateBean1)/* , "模板名称不能为空" */);
		assertEquals("模板名称不能为空", exception.getMessage());
		
		// template.name is " "
		WFTemplateCreateBean templateBean2 = new WFTemplateCreateBean();
		templateBean2.setDescription("description");
		templateBean2.setName(" ");
		
		exception = assertThrows(IllegalArgumentException.class, 
				() -> wfTemplateService.createTemplate(templateBean2)/* , "模板名称不能为空123" */);
		assertEquals("模板名称不能为空", exception.getMessage());
		
//		assertThrows三个参数也并不校验message，所以使用相应的Exception或者使用Exception接受相应的异常，然后再采用assertEquals比较异常信息
		// 综上，null，""，空白字符都算作空，代码采用Assert.hasText，所有后续不再做多种判断，只需null的情况进行测试
	}
}

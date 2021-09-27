package com.wangcai.test;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.EasyWFException;

@RestController
public class ControllerAdviceTestController {

	@GetMapping("advice/test")
	public String test(String param) {
		if(StringUtils.isBlank(param)) {
			throw new EasyWFException("-1", "测试异常");
		}
		
		return param;
	}
}

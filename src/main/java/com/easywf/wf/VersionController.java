package com.easywf.wf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.ApiResult;
import com.easywf.wf.common.ApiResultTemplate;
import com.easywf.wf.common.Version;

@RestController
public class VersionController {

	/**
	 * 查看工作流引擎版本
	 * @return
	 */
	@GetMapping(value = "/wf/version")
	public ApiResult<String> version() {
		return ApiResultTemplate.success("Ver " + Version.VERSION + ", Create by WangCai.");
	}
}

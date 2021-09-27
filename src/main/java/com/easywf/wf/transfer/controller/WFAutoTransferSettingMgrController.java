package com.easywf.wf.transfer.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.ApiResult;
import com.easywf.wf.common.ApiResultTemplate;
import com.easywf.wf.transfer.entity.WFAutoTransferSettingEntity;
import com.easywf.wf.transfer.service.WFAutoTransferSettingService;
import com.easywf.wf.transfer.vo.WFAutoTransferSettingVO;

@RestController
@RequestMapping("/auto/transfer")
public class WFAutoTransferSettingMgrController {
	
	private static final Log log = LogFactory.getLog(WFAutoTransferSettingMgrController.class);
	
	@Autowired
	private WFAutoTransferSettingService wfAutoTransferSettingService;
	
	/**
	 * 列表
	 * @return
	 */
	@GetMapping("list")
	public ApiResult<List<WFAutoTransferSettingVO>> list() {
		
//			String userId = SecurityUtils.obtainLoginedUserId();
		String userId = "wangcai";
		List<WFAutoTransferSettingVO> list = wfAutoTransferSettingService.obtainList(userId);
		return ApiResultTemplate.success(list);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	@GetMapping("obtain")
	public ApiResult<WFAutoTransferSettingVO> obtain(@RequestParam(required = true) String id) {
		WFAutoTransferSettingVO vo = wfAutoTransferSettingService.obtainVO(id);
		return ApiResultTemplate.success(vo);
	}
	
	/**
	 * @param id
	 * @param beginTime
	 * @param endTime
	 * @param transferUserId
	 * @return
	 */
	@PostMapping("save")
	public ApiResult update(String id, @RequestParam(required = true) long beginTime, 
			@RequestParam(required = true) long endTime, @RequestParam(required = true) String transferUserId) {
		WFAutoTransferSettingEntity entity = null;
		if (StringUtils.isEmpty(id)) {
			id = UUID.randomUUID().toString();
		} else {
			entity = wfAutoTransferSettingService.obtain(id);
		}
//			String userId = SecurityUtils.obtainLoginedUserId();
		String userId = "wangcai";
		if (entity == null) {
			entity = new WFAutoTransferSettingEntity();
			entity.setId(id);
		} else if (!userId.equals(entity.getApproverId())) {
			ApiResultTemplate.failure("-1", "您无权更新此记录");
		}
		
		entity.setApproverId(userId);
		entity.setBeginTime(new Date(beginTime));
		entity.setEndTime(new Date(endTime));
		entity.setTransferUserId(transferUserId);
		entity.setUpdateTime(new Date());
		
		wfAutoTransferSettingService.save(entity);
		
		return ApiResultTemplate.success();
	}
	
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	@PostMapping("delete")
	public ApiResult delete(@RequestParam(required = true)String[] ids) {
		
		if (ids != null && ids.length > 0) {
			List<String> idList = Arrays.asList(ids);
			wfAutoTransferSettingService.delete(idList);
		}
		
		return ApiResultTemplate.success();
	}
}

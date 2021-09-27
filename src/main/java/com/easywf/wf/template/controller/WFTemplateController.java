package com.easywf.wf.template.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.ApiResult;
import com.easywf.wf.common.ApiResultTemplate;
import com.easywf.wf.common.AuthorityCheck;
import com.easywf.wf.common.ResultCodeConstant;
import com.easywf.wf.template.bean.WFTemplateCreateBean;
import com.easywf.wf.template.bean.WFTemplateDetailVO;
import com.easywf.wf.template.bean.WFTemplateLineCreateBean;
import com.easywf.wf.template.bean.WFTemplateLineUpdateBean;
import com.easywf.wf.template.bean.WFTemplateNodeCreateBean;
import com.easywf.wf.template.bean.WFTemplateNodeSettingCreateBean;
import com.easywf.wf.template.bean.WFTemplateNodeSettingUpdateBean;
import com.easywf.wf.template.bean.WFTemplateNodeUpdateBean;
import com.easywf.wf.template.bean.WFTemplateParamCreateBean;
import com.easywf.wf.template.bean.WFTemplateParamUpdateBean;
import com.easywf.wf.template.bean.WFTemplateUpdateBean;
import com.easywf.wf.template.entity.WFTemplateEntity;
import com.easywf.wf.template.entity.WFTemplateNodeParamEntity;
import com.easywf.wf.template.service.WFTemplateAuthorityService;
import com.easywf.wf.template.service.WFTemplateService;

/**
 * 流程模板相关操作Controller
 * @author wangcai
 * @version 1.0
 */
@RestController
@RequestMapping("/wf/template")
public class WFTemplateController {
	
	@Autowired
	private WFTemplateService wfTemplateService;
	
	// 操作相关的需要考虑权限
	/*
	 * 创建流程模板 √
	 * 修改流程模板  √
	 * 删除流程模板   √
	 * 发布模板  √
	 * 失效模板 √
	 * 升级版本
	 * 导入模板
	 * 导出模板
	 * 校验模板正否正确 
	 * 获取流程模板列表   √
	 * 获取流程模板发布列表 √
	 * 获取流程详情
	 * 
	 * 添加流程参数
	 * 修改流程参数 草稿状态
	 * 删除流程参数 草稿状态
	 * 
	 * 添加流程节点
	 * 删除流程节点
	 * 修改流程节点
	 * 
	 * 添加流程节点拓展信息
	 * 修改流程节点拓展信息
	 * 删除流程节点拓展信息
	 * 
	 * 添加节点关系
	 * 修改节点关系
	 * 删除节点关系
	 * 
	 */
	
	/**
	 * 创建流程模板，并生成开始和结束节点
	 * @param name 模板名称
	 * @param description 描述
	 * @return JSON格式数据
	 */
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_ADD, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_TEMPLATE)
	@PostMapping("/create")
	public ApiResult<String> createTemplate(@RequestBody WFTemplateCreateBean templateBean) {
		return ApiResultTemplate.success(wfTemplateService.createTemplate(templateBean));
	}
	
	/**
	 * 编辑流程模板
	 * @param templateId 模板ID
	 * @param name 模板名称
	 * @param description 描述
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_TEMPLATE)
	@PostMapping("/edit")
	public ApiResult editTemplate(@RequestBody  WFTemplateUpdateBean templateBean) {
		wfTemplateService.updateTemplate(templateBean);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 删除流程模板
	 * @param templateId 模板ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_DELETE, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_TEMPLATE)
	@PostMapping("/delete")
	public ApiResult deleteTemplate(String templateId) {
		wfTemplateService.deleteTemplate(templateId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 升级一个流程模板
	 * @since 1.6
	 * @param templateId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/upgrade")
	public ApiResult upgradeTemplate(String templateId) {
		wfTemplateService.upgradeTemplate(templateId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 拷贝一个流程模板
	 * @since 1.6
	 * @param templateId
	 * @return
	 */
	@PostMapping("/copy")
	public ApiResult<String> copyTemplate(String templateId){
		return ApiResultTemplate.success(wfTemplateService.copyTemplate(templateId));
	}
	
	/**
	 * 发布流程模板
	 * @param templateId 流程模板ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_TEMPLATE)
	@PostMapping("/publish")
	public ApiResult publishTemplate(String templateId){
		wfTemplateService.publishTemplate(templateId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 失效流程模板
	 * @param templateId  流程模板ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_TEMPLATE)
	@PostMapping("/invalid")
	public ApiResult invalidTemplate(String templateId){
		wfTemplateService.invalidTemplate(templateId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 获取全部的流程模板对象<br>
	 * 不区分流程模板状态<br>
	 * 不包含节点、参数等其他信息
	 * @return JSON格式数据
	 */
	@GetMapping("/all/list")
	public ApiResult<List<WFTemplateEntity>> listAllTemplate(){
		List<WFTemplateEntity> list = wfTemplateService.listTemplate();
		return ApiResultTemplate.success(list);
	}
	
	@GetMapping("/publish/list")
	public ApiResult<List<WFTemplateEntity>> listPublishTemplate(){
		List<WFTemplateEntity> list = wfTemplateService.listPublishTemplate();
		return ApiResultTemplate.success(list);
	}
	
	
	/**
	 * 检查模板正确性
	 * @param templateId 流程模板ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/check")
	public ApiResult checkTemplate(String templateId){
		wfTemplateService.checkTemplate(templateId);
		return ApiResultTemplate.success();
	}
	
	@GetMapping("/detail")
	public ApiResult<WFTemplateDetailVO> detailTemplate(String templateId){
		return ApiResultTemplate.success(wfTemplateService.detailTemplate(templateId));
	}
	
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_ADD, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_PARAM)
	@PostMapping("/param/create")
	public ApiResult<String> addParam(@RequestBody WFTemplateParamCreateBean paramCreateBean){
		return ApiResultTemplate.success(wfTemplateService.addParam(paramCreateBean));
	}
	
	/**
	 * 更新流程模板参数
	 * @param paramUpdateBean
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_PARAM)
	@PostMapping("/param/edit")
	public ApiResult editParam(@RequestBody WFTemplateParamUpdateBean paramUpdateBean){
		wfTemplateService.updateParam(paramUpdateBean);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 删除流程模板参数
	 * @param paramId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_DELETE, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_PARAM)
	@PostMapping("/param/delete")
	public ApiResult deleteParam(String paramId){
		wfTemplateService.deleteParam(paramId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 新增节点
	 * @param templateId 流程模板ID
	 * @param name 节点名称
	 * @param description 节点描述
	 * @param type 节点类型
	 * @param autoService 自动节点实现类
	 * @param childTemplateId 子流程模板ID
	 * @param syncVariable 是否同步变量到主流程
	 * @return JSON格式数据
	 */
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_ADD, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_NODE)
	@PostMapping("/node/create")
	public ApiResult<String> addNode(@RequestBody WFTemplateNodeCreateBean nodeBean){
		return ApiResultTemplate.success(wfTemplateService.addTemplateNode(nodeBean));
	}
	
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_NODE)
	@PostMapping("/node/edit")
	public ApiResult editNode(@RequestBody WFTemplateNodeUpdateBean nodeUpdateBean){
		wfTemplateService.updateTemplateNode(nodeUpdateBean);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 删除节点参数
	 * @param nodeId 节点ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_DELETE, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_NODE)
	@PostMapping("/node/delete")
	public ApiResult deleteNode(String nodeId){
		wfTemplateService.deleteTemplateNode(nodeId);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 更新节点坐标
	 * @param nodeId
	 * @param x
	 * @param y
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_NODE)
	@PostMapping("/node/position")
	public ApiResult updateNodePosition(String nodeId, double x, double y){
		wfTemplateService.updateNodePosition(nodeId, x, y);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 获取节点参数列表
	 * @param nodeId 节点ID
	 * @return JSON格式数据
	 */
	@GetMapping("/node/param/list")
	public ApiResult<List<WFTemplateNodeParamEntity>> listNodeParam(String nodeId){
		List<WFTemplateNodeParamEntity> list = wfTemplateService.listNodeParam(nodeId);
		return ApiResultTemplate.success(list);
	}
	
	/**
	 * 保存节点参数
	 * @param nodeId 节点ID
	 * @param paramIds 参数ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_NODE)
	@PostMapping("/node/param/save")
	public ApiResult saveNodeParam(String nodeId, String paramIds){
		wfTemplateService.saveNodeParam(nodeId, paramIds);
		return ApiResultTemplate.success();
	}
	
	
	/**
	 * 新增节点
	 * @param templateId 流程模板ID
	 * @param beginNodeId 起始节点ID
	 * @param endNodeId 终止节点ID
	 * @param priority 优先级
	 * @param routeExpression 路由表达式
	 * @return JSON格式数据
	 */
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_ADD, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_LINE)
	@PostMapping("/line/create")
	public ApiResult<String> addLine(@RequestBody WFTemplateLineCreateBean lineCreateBean){
		if(lineCreateBean.getPriority() == null || lineCreateBean.getPriority() < 0) {
			lineCreateBean.setPriority(999);
		}
		return ApiResultTemplate.success(wfTemplateService.addLine(lineCreateBean));
	}
	
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_MODIFY, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_LINE)
	@PostMapping("/line/edit")
	public ApiResult editLine(@RequestBody WFTemplateLineUpdateBean lineUpdateBean){
		if(lineUpdateBean.getPriority() == null || lineUpdateBean.getPriority() < 0) {
			lineUpdateBean.setPriority(999);
		}
		wfTemplateService.updateLine(lineUpdateBean);
		return ApiResultTemplate.success();
	}
	
	@SuppressWarnings("rawtypes")
	@AuthorityCheck(operateType = WFTemplateAuthorityService.OPERATETYPE_DELETE, operateFunction = WFTemplateAuthorityService.OPERATEFUNCTION_LINE)
	@PostMapping("/line/delete")
	public ApiResult deleteLine(String lineId){
		wfTemplateService.deleteLine(lineId);
		return ApiResultTemplate.success();
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/node/setting/create")
	public ApiResult createNodeSetting(@RequestBody WFTemplateNodeSettingCreateBean nodeSettingCreateBean){
		wfTemplateService.addNodeSetting(nodeSettingCreateBean);
		return ApiResultTemplate.success();
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/node/setting/edit")
	public ApiResult editNodeSetting(@RequestBody WFTemplateNodeSettingUpdateBean nodeSettingUpdateBean){
		wfTemplateService.updateNodeSetting(nodeSettingUpdateBean);
		return ApiResultTemplate.success();
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/node/setting/delete")
	public ApiResult deleteNodeSetting(String settingId){
		wfTemplateService.deleteNodeSetting(settingId);
		return ApiResultTemplate.success();
	}
	
}

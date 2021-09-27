package com.easywf.wf.template.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.easywf.wf.common.EasyWFException;
import com.easywf.wf.common.ResultCodeConstant;
import com.easywf.wf.process.service.ExpressionEngineService;
import com.easywf.wf.process.vo.ProcessParamVO;
import com.easywf.wf.resources.service.WFResourcesService;
import com.easywf.wf.template.bean.WFTemplateCreateBean;
import com.easywf.wf.template.bean.WFTemplateDetailVO;
import com.easywf.wf.template.bean.WFTemplateLineCreateBean;
import com.easywf.wf.template.bean.WFTemplateLineDetailVO;
import com.easywf.wf.template.bean.WFTemplateLineUpdateBean;
import com.easywf.wf.template.bean.WFTemplateNodeCreateBean;
import com.easywf.wf.template.bean.WFTemplateNodeSettingCreateBean;
import com.easywf.wf.template.bean.WFTemplateNodeSettingUpdateBean;
import com.easywf.wf.template.bean.WFTemplateNodeUpdateBean;
import com.easywf.wf.template.bean.WFTemplateParamCreateBean;
import com.easywf.wf.template.bean.WFTemplateParamUpdateBean;
import com.easywf.wf.template.bean.WFTemplateUpdateBean;
import com.easywf.wf.template.constant.WFApprovalMode;
import com.easywf.wf.template.constant.WFApprovalPassType;
import com.easywf.wf.template.constant.WFApproverStrategy;
import com.easywf.wf.template.constant.WFFunctions;
import com.easywf.wf.template.constant.WFNodeHandleMode;
import com.easywf.wf.template.constant.WFNodeType;
import com.easywf.wf.template.constant.WFOrgRoleMode;
import com.easywf.wf.template.constant.WFParamValueType;
import com.easywf.wf.template.constant.WFTemplateState;
import com.easywf.wf.template.entity.WFTemplateEntity;
import com.easywf.wf.template.entity.WFTemplateLineEntity;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.entity.WFTemplateNodeParamEntity;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.entity.WFTemplateParamEntity;
import com.easywf.wf.template.repository.WFTemplateJpaRepo;
import com.easywf.wf.template.repository.WFTemplateLineJpaRepo;
import com.easywf.wf.template.repository.WFTemplateNodeJapRepo;
import com.easywf.wf.template.repository.WFTemplateNodeParamJpaRepo;
import com.easywf.wf.template.repository.WFTemplateNodeSettingJpaRepo;
import com.easywf.wf.template.repository.WFTemplateParamJpaRepo;
import com.easywf.wf.util.service.ParamValidityService;

@Service
public class WFTemplateService {
	
	@Autowired
	private WFTemplateJpaRepo wfTemplateJpaRepo;
	
	@Autowired
	private WFTemplateNodeJapRepo wfTemplateNodeJapRepo;
	
	@Autowired
	private WFTemplateLineJpaRepo wfTemplateLineJpaRepo;
	
	@Autowired
	private WFTemplateNodeSettingJpaRepo wfTemplateNodeSettingJpaRepo;
	
	@Autowired
	private ExpressionEngineService expressionEngineService;
	
	@Autowired
	private WFTemplateParamJpaRepo wfTemplateParamJpaRepo;
	
	@Autowired
	private WFTemplateNodeParamJpaRepo wfTemplateNodeParamJpaRepo;
	
	@Autowired
	private WFResourcesService wfResourcesService;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	///////////////////// 流程模板 ///////////////////////////////////
	
	/**
	 * 创建流程模板，并生成开始和结束节点
	 * @param name 模板名称
	 * @param description 描述
	 * @return 流程模板ID
	 */
	@Transactional
	public String createTemplate(WFTemplateCreateBean templateBean) {
		
		Assert.hasText(templateBean.getName(), "模板名称不能为空");
		
		// 创建工作流模板对象
		WFTemplateEntity template = new WFTemplateEntity(templateBean.getName(), templateBean.getDescription());
		template.setPaap(templateBean.isPaap());
		template.setSaap(templateBean.isSaap());
		// 创建新版本，第一版的templateId与ID一致
		template.setBaseTemplateId(template.getId());
		wfTemplateJpaRepo.save(template);
		
		// 创建开始和结束节点
		String beginNodeId = createBeginNode(template.getId());
		String endNodeId = createEndNode(template.getId());
		
		// 生成开始到结束节点的关系
		WFTemplateLineCreateBean createLineBean = new WFTemplateLineCreateBean();
		createLineBean.setTemplateId(template.getId());
		createLineBean.setBeginNodeId(beginNodeId);
		createLineBean.setEndNodeId(endNodeId);
		createLineBean.setPriority(999);
		createLineBean.setRouteExpression("");
		createLine(createLineBean);
		
		return template.getId();
	}
	
	/**
	 * 更新模板基础属性
	 * @param templateId 模板ID
	 * @param name 模板名称
	 * @param description 模板描述
	 */
	@Transactional
	public void updateTemplate(WFTemplateUpdateBean templateBean) {
		Assert.hasText(templateBean.getName(), "模板名称不能为空");
		
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateBean.getId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		Assert.isTrue(isDraft(template), "流程模板不是草稿状态");
		
		template.setName(templateBean.getName());
		template.setDescription(templateBean.getDescription());
		template.setPaap(templateBean.isPaap());
		template.setSaap(templateBean.isSaap());
		
		wfTemplateJpaRepo.save(template);
	}
	
	/**
	 * 删除一个模板
	 * @param templateId 模板ID
	 */
	@Transactional
	public void deleteTemplate(String templateId) {
		
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		Assert.isTrue(isDraft(template), "流程模板不是草稿状态");
		
		// 删除流程模板中的变量
		List<WFTemplateParamEntity> params = wfTemplateParamJpaRepo.findByTemplateId(templateId);
		wfTemplateParamJpaRepo.deleteInBatch(params);
		
		// 删除节点（该方法中删除节点依赖的路由关系和节点拓展属性）
		List<WFTemplateNodeEntity> nodes = wfTemplateNodeJapRepo.findByTemplateIdOrderByLevelAsc(templateId);
		
		nodes.forEach((node) -> {
			deleteNode(node.getId());
		});
		
		// 删除模板
		wfTemplateJpaRepo.delete(template);
	}
	
	/**
	 * 发布一个模板
	 * @param templateId 模板ID
	 */
	@Transactional
	public void publishTemplate(String templateId) {
		
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		Assert.isTrue(isDraft(template), "流程模板不是草稿状态");
		
		// 需要校验子流程是否发布，先发布子流程，然后才能发布主流程
		checkChildDraftTemplate(templateId);
		
		// 校验模板的正确性
		checkTemplate(templateId);
		
		// 更新流程状态和发布时间
		template.setState(WFTemplateState.PUBLISH.toString());
		template.setPublishTime(new Date());
		wfTemplateJpaRepo.save(template);
	}
	
	
	private void checkChildDraftTemplate(String templateId) {
		// TODO
	}
	
	/**
	 * 失效一个模板
	 * @param templateId 模板ID
	 */
	@Transactional
	public void invalidTemplate(String templateId) {
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		// 模板不是发布状态，不能失效
		if(!WFTemplateState.PUBLISH.toString().equals(template.getState())) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "模板不是发布状态，不能设置失效");
		}
		
		template.setState(WFTemplateState.INVALID.toString());
		template.setInvalidTime(new Date());
		wfTemplateJpaRepo.save(template);
	}
	
	/**
	 * 升级流程模板
	 * @since 1.6
	 * @param originalTemplateId 原始模板ID
	 */
	@Transactional
	public void upgradeTemplate(String originalTemplateId) {
		
		// TODO 升级模板需要做成分布式锁，一个模板同时只能由一个升级请求，主要是版本控制问题
		
		// 拷贝模板表
		WFTemplateEntity originalTemplate = wfTemplateJpaRepo.findById(originalTemplateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		// 判断当前的流程模板是否是最高版本，只有最高版本才能继续升级
		Integer maxVersion = wfTemplateJpaRepo.findMaxVersion(originalTemplate.getBaseTemplateId());
		
		if(maxVersion != originalTemplate.getVersion()) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不是最高版本，不能进行升级版本。最高版本为：" + maxVersion);
		}
		
		WFTemplateEntity copyTemplate = new WFTemplateEntity(originalTemplate.getName(), originalTemplate.getDescription());
		copyTemplate.setBaseTemplateId(originalTemplate.getBaseTemplateId());
		copyTemplate.setPaap(originalTemplate.isPaap());
		copyTemplate.setSaap(originalTemplate.isSaap());
		copyTemplate.setVersion(maxVersion + 1);
		
		wfTemplateJpaRepo.save(copyTemplate);
		
		// 拷贝其余细项
		copySettings(originalTemplateId, copyTemplate.getId());
		
		// 将上个版本的流程模板设置成失效
		originalTemplate.setState(WFTemplateState.INVALID.toString());
		originalTemplate.setInvalidTime(new Date());
		wfTemplateJpaRepo.save(originalTemplate);
	}
	
	/**
	 * 拷贝一个模板
	 * @since 1.6
	 * @param originalTemplateId 原始模板ID
	 */
	@Transactional
	public String copyTemplate(String originalTemplateId) {
		// 拷贝模板表
		WFTemplateEntity originalTemplate = wfTemplateJpaRepo.findById(originalTemplateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		
		WFTemplateEntity copyTemplate = new WFTemplateEntity(originalTemplate.getName() + " (拷贝)", originalTemplate.getDescription());
		copyTemplate.setBaseTemplateId(copyTemplate.getId());
		copyTemplate.setPaap(originalTemplate.isPaap());
		copyTemplate.setSaap(originalTemplate.isSaap());
		
		wfTemplateJpaRepo.save(copyTemplate);
		
		// 拷贝其余细项
		copySettings(originalTemplateId, copyTemplate.getId());
		return copyTemplate.getId();
	}
	
	private void copySettings(String originalTemplateId, String copyTemplateId) {
		Map<String, String> paramsMap = new HashMap<>();
		// 拷贝流程参数
		List<WFTemplateParamEntity> params = wfTemplateParamJpaRepo.findByTemplateId(originalTemplateId);
		for(WFTemplateParamEntity param : params) {
			WFTemplateParamEntity p = new WFTemplateParamEntity();
			p.setId(UUID.randomUUID().toString());
			p.setInitValue(param.getInitValue());
			p.setName(param.getName());
			p.setProperty(param.getProperty());
			p.setTemplateId(copyTemplateId);
			p.setValueType(param.getValueType());
			wfTemplateParamJpaRepo.save(p);
			paramsMap.put(param.getId(), p.getId());
		}
		
		Map<String, String> nodeMap = new HashMap<>();
		
		// 拷贝节点
		List<WFTemplateNodeEntity> nodes = wfTemplateNodeJapRepo.findByTemplateIdOrderByLevelAsc(originalTemplateId);
		for(WFTemplateNodeEntity node : nodes) {
			// 拷贝节点
			WFTemplateNodeEntity n = new WFTemplateNodeEntity();
			n.setAutoService(node.getAutoService());
			n.setChildTemplateId(node.getChildTemplateId());
			n.setDescription(node.getDescription());
			n.setDisplay(node.getDisplay());
			n.setId(UUID.randomUUID().toString());
			n.setLevel(node.getLevel());
			n.setName(node.getName());
			n.setSyncVariable(node.isSyncVariable());
			n.setTemplateId(copyTemplateId);
			n.setType(node.getType());
			n.setX(node.getX());
			n.setY(node.getY());
			wfTemplateNodeJapRepo.save(n);
			
			nodeMap.put(node.getId(), n.getId());
			
			// 拷贝节点设置
			WFTemplateNodeSettingEntity setting = wfTemplateNodeSettingJpaRepo.findByNodeId(node.getId());
			if(setting != null) {
				WFTemplateNodeSettingEntity s = new WFTemplateNodeSettingEntity();
				s.setApprovalMode(setting.getApprovalMode());
				s.setHandleMode(setting.getHandleMode());
				s.setFormURL(setting.getFormURL());
				s.setFunctions(setting.getFormURL());
				s.setId(UUID.randomUUID().toString());
				s.setNodeId(n.getId());
				s.setOrgRoleMode(setting.getOrgRoleMode());
				if(StringUtils.isNotBlank(setting.getDeptParamId())) {
					s.setDeptParamId(paramsMap.get(setting.getDeptParamId()));
				}
				
				if(StringUtils.isNotBlank(setting.getUserParamId())) {
					s.setDeptParamId(paramsMap.get(setting.getDeptParamId()));
				}
				
				s.setPassService(setting.getPassService());
				s.setPassType(setting.getPassType());
				s.setRoles(setting.getRoles());
				s.setStrategy(setting.getStrategy());
				s.setUpSearch(setting.isUpSearch());
				s.setApproverService(setting.getApproverService());
				
				wfTemplateNodeSettingJpaRepo.save(s);
			}
			
			// 拷贝流程节点变量
			List<WFTemplateNodeParamEntity> nps = wfTemplateNodeParamJpaRepo.findByNodeId(node.getId());
			for(WFTemplateNodeParamEntity nodeParam : nps) {
				WFTemplateNodeParamEntity np = new WFTemplateNodeParamEntity();
				np.setId(UUID.randomUUID().toString());
				np.setNodeId(n.getId());
				np.setParamId(paramsMap.get(nodeParam.getParamId()));
				
				wfTemplateNodeParamJpaRepo.save(np);
			}
		}
		
		// 拷贝节点之间关系
		List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByTemplateId(originalTemplateId);
		for(WFTemplateLineEntity line : lines) {
			WFTemplateLineEntity l = new WFTemplateLineEntity();
			l.setBeginNodeId(nodeMap.get(line.getBeginNodeId()));
			l.setEndNodeId(nodeMap.get(line.getEndNodeId()));
			l.setId(UUID.randomUUID().toString());
			l.setPriority(line.getPriority());
			l.setRouteExpression(line.getRouteExpression());
			l.setTemplateId(copyTemplateId);
			
			wfTemplateLineJpaRepo.save(l);
		}
	}
	
	///////////////////// 流程模板 ////////////////////////////////////////////
	
	/**
	 * 添加一个参数
	 * @param templateId 流程模板ID
	 * @param name 参数名 
	 * @param property 属性名
	 * @param initValue 初始值
	 * @param valueType 值类型
	 * @return 参数ID
	 */
	@Transactional
	public String addParam(WFTemplateParamCreateBean paramCreateBean) {
		
		Assert.hasText(paramCreateBean.getName(), "参数名称不能为空");
		Assert.hasText(paramCreateBean.getProperty(), "参数属性不能为空");
		Assert.hasText(paramCreateBean.getValueType(), "参数值类型不能为空");
		
		if(!WFParamValueType.include(paramCreateBean.getValueType())) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "值类型设置不正确");
		}
		
		if(StringUtils.isNotBlank(paramCreateBean.getInitValue())) {
			ParamValidityService.check(paramCreateBean.getInitValue(), paramCreateBean.getValueType());
		}
		
		checkDraftTemplate(paramCreateBean.getTemplateId());
		
		// 校验是否存在该属性
		WFTemplateParamEntity param = wfTemplateParamJpaRepo.findByTemplateIdAndProperty(paramCreateBean.getTemplateId(), paramCreateBean.getProperty());
		Assert.isNull(param, "流程模板下已存在属性");
		
		param = new WFTemplateParamEntity();
		param.setId(UUID.randomUUID().toString());
		param.setName(paramCreateBean.getName());
		param.setProperty(paramCreateBean.getProperty());
		param.setInitValue(paramCreateBean.getInitValue());
		param.setTemplateId(paramCreateBean.getTemplateId());
		param.setValueType(paramCreateBean.getValueType());
			
		wfTemplateParamJpaRepo.save(param);
		
		return param.getId();
	}
	
	/**
	 *  更新一个参数
	 * @param paramId 参数ID
	 * @param name 参数名称
	 * @param property 属性名称
	 * @param initValue 初始值
	 * @param valueType 值类型
	 */
	@Transactional
	public void updateParam(WFTemplateParamUpdateBean paramUpdateBean) {
		// 校验必填项
		Assert.hasText(paramUpdateBean.getName(), "参数名称不能为空");
		Assert.hasText(paramUpdateBean.getProperty(), "参数属性不能为空");
		Assert.hasText(paramUpdateBean.getValueType(), "参数值类型不能为空");
		
		if(!WFParamValueType.include(paramUpdateBean.getValueType())) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "值类型设置不正确");
		}
		
		// 校验初始值设置是否合法
		if(StringUtils.isNotBlank(paramUpdateBean.getInitValue())) {
			ParamValidityService.check(paramUpdateBean.getInitValue(), paramUpdateBean.getValueType());
		}
		
		// 校验参数是否存在
		WFTemplateParamEntity param = wfTemplateParamJpaRepo.findById(paramUpdateBean.getParamId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程参数不存在"));
		checkDraftTemplate(param.getTemplateId());
		
		// 校验是否已经被使用， 即修改后的属性不能与流程中其他的属性相同
		WFTemplateParamEntity existsParam = wfTemplateParamJpaRepo.findByTemplateIdAndProperty(param.getTemplateId(), paramUpdateBean.getProperty());
		if(existsParam != null && !existsParam.getId().equals(paramUpdateBean.getParamId())) {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板下已存在属性");
		}
		
		// 保存参数
		param.setName(paramUpdateBean.getName());
		param.setProperty(paramUpdateBean.getProperty());
		param.setInitValue(paramUpdateBean.getInitValue());
		param.setValueType(paramUpdateBean.getValueType());
		
		wfTemplateParamJpaRepo.save(param);
	}
	
	/**
	 * 删除一个参数
	 * @param paramId 参数ID
	 */
	@Transactional
	public void deleteParam(String paramId) {
		WFTemplateParamEntity param = wfTemplateParamJpaRepo.findById(paramId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程参数不存在"));
		
		checkDraftTemplate(param.getTemplateId());
		wfTemplateParamJpaRepo.delete(param);
	}
	
	
	public List<WFTemplateParamEntity> listTemplateParams(String templateId){
		return wfTemplateParamJpaRepo.findByTemplateId(templateId);
	}
	
	/**
	 * 校验一个模板有效性
	 * @param templateId 流程模板ID
	 * @return true:校验通过， false:校验失败
	 */
	public void checkTemplate(String templateId) {
		
	}
	
	
	/**
	 * 获取流程模板详细信息
	 * @param templateId 模板ID
	 * @return 获取流程模板详细信息
	 */
	public WFTemplateDetailVO detailTemplate(String templateId) {
		
		WFTemplateDetailVO vo = new WFTemplateDetailVO();
		
		// 获取模板对象
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateId).get();
		if(template == null) {
			throw new RuntimeException("流程模板不存在！");
		}
		vo.setTemplate(template);
		
		// 获取流程参数
		List<WFTemplateParamEntity> params = listTemplateParams(templateId);
				
		vo.setParams(params);
		
		// 获取流程节点和节点拓展属性
		List<WFTemplateNodeEntity> nodes = wfTemplateNodeJapRepo.findByTemplateIdOrderByLevelAsc(templateId);
		Map<String, String> nodeNameMap = new HashMap<>();
		for(WFTemplateNodeEntity node : nodes) {
			WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(node.getId());
			vo.addNode(node, nodeSetting);
			nodeNameMap.put(node.getId(), node.getName());
		}
		
		// 获取节点之间关系
		List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByTemplateId(templateId);
		for(WFTemplateLineEntity line : lines) {
			WFTemplateLineDetailVO lineVO = new WFTemplateLineDetailVO();
			lineVO.setId(line.getId());
			lineVO.setPriority(line.getPriority());
			lineVO.setRouteExpression(line.getRouteExpression());
			lineVO.setBeginNode(nodeNameMap.get(line.getBeginNodeId()));
			lineVO.setEndNode(nodeNameMap.get(line.getEndNodeId()));
			lineVO.setBeginNodeId(line.getBeginNodeId());
			lineVO.setEndNodeId(line.getEndNodeId());
			vo.getLines().add(lineVO);
		}
		
		return vo;
	}
	
	/**
	 * 导出模板
	 */
	public void exportTemplate() {
		// TODO 调用获取详细，然后生成文件写入JSON
	}
	
	/**
	 * 导入模板
	 */
	public void importTemplate() {
		//TODO 读取文件，然后生成对应的模板
	}
	
	/**
	 * 获取流程模板列表
	 * @return 流程模板列表
	 */
	public List<WFTemplateEntity> listTemplate() {
		return wfTemplateJpaRepo.findAllOrderByName();
	}
	
	/**
	 * 获取发布的流程模板列表
	 * @return 发布状态的流程模板列表
	 */
	public List<WFTemplateEntity> listPublishTemplate(){
		return wfTemplateJpaRepo.findByState(WFTemplateState.PUBLISH.toString());
	}
	
	
	/**
	 * 创建一个模板的开始节点
	 * @param templateId 模板ID
	 * @return 开始节点ID
	 */
	private String createBeginNode(String templateId) {
		WFTemplateNodeCreateBean nodeBean = new WFTemplateNodeCreateBean();
		nodeBean.setName("开始");
		nodeBean.setDisplay("");
		nodeBean.setDescription("");
		nodeBean.setTemplateId(templateId);
		nodeBean.setType(WFNodeType.BEGIN.toString());
		WFTemplateNodeEntity beginNode = createNode(nodeBean);
		return beginNode.getId();
	}
	
	/**
	 * 创建一个模板的结束节点
	 * @param templateId 模板ID
	 * @return 结束节点ID
	 */
	private String createEndNode(String templateId) {
		WFTemplateNodeCreateBean nodeBean = new WFTemplateNodeCreateBean();
		nodeBean.setName("结束");
		nodeBean.setDisplay("");
		nodeBean.setDescription("");
		nodeBean.setTemplateId(templateId);
		nodeBean.setType(WFNodeType.END.toString());
		WFTemplateNodeEntity endNode = createNode(nodeBean);
		return endNode.getId();
	}
	
	private WFTemplateNodeEntity createNode(WFTemplateNodeCreateBean nodeBean) {
		
		if(!WFNodeType.AUTO.toString().equals(nodeBean.getType())) {
			nodeBean.setAutoService("");
		}else {
			Assert.hasText(nodeBean.getAutoService(), "自动节点需要设置实现类");
		}
		
		if(!WFNodeType.CHILD.toString().equals(nodeBean.getType())) {
			nodeBean.setChildTemplateId("");
			nodeBean.setSyncVariable(false);
		}else {
			Assert.hasText(nodeBean.getChildTemplateId(), "子流程需要设置流程模板ID");
			WFTemplateEntity childTemplate = wfTemplateJpaRepo.findById(nodeBean.getChildTemplateId())
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "子流程不存在"));
			
			if(WFTemplateState.INVALID.toString().equals(childTemplate.getState())) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "子流程已失效");
			}
		}
		
		// 创建节点对象
		WFTemplateNodeEntity node = new WFTemplateNodeEntity(); 
		node.setId(UUID.randomUUID().toString());
		node.setName(nodeBean.getName());
		node.setDisplay(nodeBean.getDisplay());
		node.setDescription(nodeBean.getDescription());
		node.setTemplateId(nodeBean.getTemplateId());
		node.setType(nodeBean.getType());
		node.setAutoService(nodeBean.getAutoService());
		node.setChildTemplateId(nodeBean.getChildTemplateId());
		node.setSyncVariable(nodeBean.isSyncVariable());
		
		wfTemplateNodeJapRepo.save(node);
		
		return node;
	}
	
	private String createLine(WFTemplateLineCreateBean lineCreateBean) {
		WFTemplateLineEntity line = new WFTemplateLineEntity();
		line.setId(UUID.randomUUID().toString());
		line.setTemplateId(lineCreateBean.getTemplateId());
		line.setBeginNodeId(lineCreateBean.getBeginNodeId());
		line.setEndNodeId(lineCreateBean.getEndNodeId());
		line.setPriority(lineCreateBean.getPriority());
		line.setRouteExpression(lineCreateBean.getRouteExpression());
		
		wfTemplateLineJpaRepo.save(line);
		return line.getId();
	}
	
	@Transactional
	public void deleteTemplateNode(String nodeId) {
		WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(nodeId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程节点不存在"));
		checkDraftTemplate(node.getTemplateId());
		deleteNode(nodeId);
	}
	
	@Transactional
	public void updateNodePosition(String nodeId, double x, double y) {
		WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(nodeId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程节点不存在"));
		
		node.position(x, y);
		wfTemplateNodeJapRepo.save(node);
	}
	
	/**
	 * 查询节点参数
	 * @param nodeId 节点ID
	 * @return 节点参数集合
	 */
	public List<WFTemplateNodeParamEntity> listNodeParam(String nodeId) {
		return wfTemplateNodeParamJpaRepo.findByNodeId(nodeId);
	}
	
	/**
	 * 保存节点参数
	 * @param nodeId 节点ID
	 * @param paramIds 参数ID
	 */
	@Transactional
	public void saveNodeParam(String nodeId, String paramIds) {
		WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(nodeId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程节点不存在"));
		
		checkDraftTemplate(node.getTemplateId());
		
		// 先删除，后新增
		// TODO 后续调整成判断， 减少数据库交互
		List<WFTemplateNodeParamEntity> list = wfTemplateNodeParamJpaRepo.findByNodeId(nodeId);
		for(WFTemplateNodeParamEntity entry : list) {
			wfTemplateNodeParamJpaRepo.delete(entry);
		}
		
		if(StringUtils.isNotBlank(paramIds)) {
			for(String paramId : paramIds.split(",")) {
				if(StringUtils.isNotBlank(paramId)) {
					WFTemplateNodeParamEntity wnp = new WFTemplateNodeParamEntity();
					wnp.setId(UUID.randomUUID().toString());
					wnp.setNodeId(nodeId);
					wnp.setParamId(paramId);
					wfTemplateNodeParamJpaRepo.save(wnp);
				}
			}
		}
	}
	
	
	/**
	 * 删除一个节点
	 * @param nodeId 节点ID
	 */
	
	private void deleteNode(String nodeId) {
		
		// 删除节点的路由
		List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByBeginNodeIdOrderByPriority(nodeId);
		wfTemplateLineJpaRepo.deleteInBatch(lines);
		
		lines = wfTemplateLineJpaRepo.findByEndNodeId(nodeId);
		wfTemplateLineJpaRepo.deleteInBatch(lines);
		
		// 删除节点扩展属性
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(nodeId);
		if(nodeSetting != null) {
			wfTemplateNodeSettingJpaRepo.delete(nodeSetting);
		}
		
		wfTemplateNodeJapRepo.deleteById(nodeId);
	}
	
	public List<WFTemplateNodeEntity> listTemplateNodes(String templateId){
		return wfTemplateNodeJapRepo.findByTemplateIdOrderByLevelAsc(templateId);
	}
	
	
	/**
	 * 添加一个节点
	 * @param name 节点名称
	 * @param description 节点描述
	 * @param templateId 流程模板ID
	 * @param type 节点类型
	 * @param autoService 自动节点实现类
	 * @param childTemplateId 子流程模板ID
	 * @param syncVariable 是否同步变量到主流程
	 * @return 节点ID
	 */
	@Transactional
	public String addTemplateNode(WFTemplateNodeCreateBean nodeBean) {
		
		Assert.hasText(nodeBean.getName(), "节点名称不能为空");
		Assert.hasText(nodeBean.getType(), "节点类型不能为空");
		Assert.isTrue(WFNodeType.include(nodeBean.getType()), "节点类型不正确");
		
		checkDraftTemplate(nodeBean.getTemplateId());
		
		// 存储节点信息
		WFTemplateNodeEntity node = createNode(nodeBean);
		return node.getId();
	}
	
	/**
	 * 更新一个节点
	 * @param nodeId 节点ID
	 * @param name 节点名称
	 * @param description 节点描述
	 * @param type 节点类型
	 * @param autoService 自动节点实现类
	 * @param childTemplateId 子流程模板ID
	 * @param syncVariable 是否同步变量到主流程
	 */
	@Transactional
	public void updateTemplateNode(WFTemplateNodeUpdateBean nodeUpdateBean) {
		
		// TODO 从办理节点  设置成其他类型， 需要给提示，已经设置了属性，将会被清空。
		
		Assert.hasText(nodeUpdateBean.getName(), "节点名称不能为空");
		Assert.hasText(nodeUpdateBean.getType(), "节点类型不能为空");
		
		Assert.isTrue(WFNodeType.include(nodeUpdateBean.getType()), "节点类型不正确");
		
		if(!WFNodeType.AUTO.toString().equals(nodeUpdateBean.getType())) {
			nodeUpdateBean.setAutoService("");
		}else {
			Assert.hasText(nodeUpdateBean.getAutoService(), "自动节点需要设置实现类");
		}
		
		if(!WFNodeType.CHILD.toString().equals(nodeUpdateBean.getType())) {
			nodeUpdateBean.setChildTemplateId("");
			nodeUpdateBean.setSyncVariable(false);
		}else {
			Assert.hasText(nodeUpdateBean.getChildTemplateId(), "子流程需要设置流程模板ID");
			
			WFTemplateEntity childTemplate = wfTemplateJpaRepo.findById(nodeUpdateBean.getChildTemplateId())
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "子流程不存在"));
			
			if(WFTemplateState.INVALID.toString().equals(childTemplate.getState())) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "子流程已失效");
			}
		}
		
		WFTemplateNodeEntity node = wfTemplateNodeJapRepo.findById(nodeUpdateBean.getNodeId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程节点不存在"));
		
		checkDraftTemplate(node.getTemplateId());
		
		node.setDescription(nodeUpdateBean.getDescription());
		node.setName(nodeUpdateBean.getName());
		node.setType(nodeUpdateBean.getType());
		node.setAutoService(nodeUpdateBean.getAutoService());
		node.setChildTemplateId(nodeUpdateBean.getChildTemplateId());
		node.setSyncVariable(nodeUpdateBean.isSyncVariable());
		node.setDisplay(nodeUpdateBean.getDisplay());
		
		wfTemplateNodeJapRepo.save(node);
		
		// 从办理类型调整成其他类型，需要删除掉设置
		if(!WFNodeType.WORK.toString().equals(nodeUpdateBean.getType())) {
			WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(nodeUpdateBean.getNodeId());
			if(nodeSetting != null) {
				wfTemplateNodeSettingJpaRepo.delete(nodeSetting);
			}
		}
	}
	
	/**
	 * 校验模板是否存在以及是否为草稿状态
	 * @param template 流程模板对象
	 */
	private boolean isDraft(WFTemplateEntity template) {
		return WFTemplateState.DRAFT.toString().equals(template.getState());
	}
	
	/**
	 * 校验模板是否存在以及是否为草稿状态
	 * @param templateId 流程模板ID
	 */
	private void checkDraftTemplate(String templateId) {
		WFTemplateEntity template = wfTemplateJpaRepo.findById(templateId)
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程模板不存在"));
		Assert.isTrue(isDraft(template), "流程模板不是草稿状态");
	}
	
	/**
	 * 创建两个节点的路由关系
	 * @param templateId 流程模板ID
	 * @param beginNodeId 起始节点ID
	 * @param endNodeId 终止节点ID
	 * @param priority 优先级
	 * @param routeExpression 路由表达式
	 * @return 新创建的关系ID
	 */
	@Transactional
	public String addLine(WFTemplateLineCreateBean lineCreateBean) {
		
		checkDraftTemplate(lineCreateBean.getTemplateId());
		
		Assert.hasText(lineCreateBean.getBeginNodeId(), "起始节点ID不能为空");
		Assert.hasText(lineCreateBean.getEndNodeId(), "终止节点ID不能为空");
		
		WFTemplateNodeEntity beginNode = wfTemplateNodeJapRepo.findById(lineCreateBean.getBeginNodeId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "起始节点不存在"));
		
		WFTemplateNodeEntity endNode = wfTemplateNodeJapRepo.findById(lineCreateBean.getEndNodeId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "终止节点不存在"));
		
		Assert.isTrue(beginNode.getTemplateId().equals(lineCreateBean.getTemplateId()), "起始节点与流程模板不匹配");
		Assert.isTrue(endNode.getTemplateId().equals(lineCreateBean.getTemplateId()), "终止节点与流程模板不匹配");
		
		String lineId = createLine(lineCreateBean);
		
		return lineId;
	}
	
	/**
	 * 更新两个节点之间的路由关系
	 * @param lineId 关系ID
	 * @param beginNodeId 起始节点ID
	 * @param endNodeId 终止节点ID
	 * @param priority 优先级
	 * @param routeExpression 路由表达式
	 */
	@Transactional
	public void updateLine(WFTemplateLineUpdateBean lineUpdateBean) {
		
		Assert.hasText(lineUpdateBean.getBeginNodeId(), "起始节点ID不能为空");
		Assert.hasText(lineUpdateBean.getEndNodeId(), "终止节点ID不能为空");
		
		WFTemplateLineEntity line = wfTemplateLineJpaRepo.findById(lineUpdateBean.getLineId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程关系不存在"));
		
		checkDraftTemplate(lineUpdateBean.getTemplateId());
		
		WFTemplateNodeEntity beginNode = wfTemplateNodeJapRepo.findById(lineUpdateBean.getBeginNodeId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "起始节点不存在"));
		
		WFTemplateNodeEntity endNode = wfTemplateNodeJapRepo.findById(lineUpdateBean.getEndNodeId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "终止节点不存在"));
		
		Assert.isTrue(beginNode.getTemplateId().equals(lineUpdateBean.getTemplateId()), "起始节点与流程模板不匹配");
		Assert.isTrue(endNode.getTemplateId().equals(lineUpdateBean.getTemplateId()), "终止节点与流程模板不匹配");
		
		line.setBeginNodeId(lineUpdateBean.getBeginNodeId());
		line.setEndNodeId(lineUpdateBean.getEndNodeId());
		line.setPriority(lineUpdateBean.getPriority());
		line.setRouteExpression(lineUpdateBean.getRouteExpression());
		
		wfTemplateLineJpaRepo.save(line);
	}
	
	/**
	 * 删除两个节点之间的路由关系
	 * @param lineId 关系ID
	 */
	@Transactional
	public void deleteLine(String lineId) {
		wfTemplateLineJpaRepo.deleteById(lineId);
	}
	
	/**
	 * 添加节点属性
	 * @param nodeId 工作项ID
	 * @param handleType 审批类型
	 * @param strategy 选人策略
	 * @param userService 实现类
	 * @param roles 角色
	 * @param formURL 表单URL
	 * @param approvalType 审批方式
	 * @param passType 通过方式
	 * @param functions 功能
	 * @param orgRoleMode 组织机构选择方式
	 * @param upSearch 是否向上级查找审批人
	 * @param paramId 参数指定部门时，指定部门ID
	 * @param passService 自定义节点通过的实现类名称
	 * @return 成功标志
	 */
	@Transactional
	public void addNodeSetting(WFTemplateNodeSettingCreateBean nodeSettingCreateBean) {
		
		Assert.hasText(nodeSettingCreateBean.getNodeId(), "流程节点不能为空");
		Assert.hasText(nodeSettingCreateBean.getStrategy(), "流程节点选人策略不能为空");
		// 一个节点只能有一个配置项
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findByNodeId(nodeSettingCreateBean.getNodeId());
		Assert.isNull(nodeSetting, "流程节点已存在配置信息");
		
		Assert.isTrue(WFNodeHandleMode.include(nodeSettingCreateBean.getHandleMode()), "节点审批类型设置不正确");
		Assert.isTrue(WFApprovalPassType.include(nodeSettingCreateBean.getPassType()), "工作项通过类型设置不正确");
		
		if(StringUtils.isNotBlank(nodeSettingCreateBean.getFunctions())) {
			for(String function : nodeSettingCreateBean.getFunctions().split(",")) {
				Assert.isTrue(WFFunctions.include(function), "节点功能设置不正确");
			}
		}
		
		Assert.isTrue(WFApproverStrategy.include(nodeSettingCreateBean.getStrategy()), "节点选人策略设置不正确");
		if(WFApproverStrategy.SERVICE.toString().equals(nodeSettingCreateBean.getStrategy())) {
			Assert.hasText(nodeSettingCreateBean.getApproverService(), "自定义实现类找审批人时，实现类设置不能为空");
		} else if(WFApproverStrategy.ROLE.toString().equals(nodeSettingCreateBean.getStrategy())) {
			Assert.hasText(nodeSettingCreateBean.getRoles(), "角色下找审批人，角色不能为空");
			for(String roleId : nodeSettingCreateBean.getRoles().split(",")) {
				Assert.notNull(wfResourcesService.findRole(roleId), "角色设置不正确：" + roleId);
			}
		} /*else if(WFApproverStrategy.ORG.toString().equals(nodeSettingCreateBean.getStrategy())) {
			Assert.hasText(nodeSettingCreateBean.getDeptParamIds(), "组织机构下找审批人，部门参数不能为空");
			for(String paramId : nodeSettingCreateBean.getDeptParamIds().split(",")) {
				wfTemplateParamJpaRepo.findById(paramId)
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
			}
		}*/ else if(WFApproverStrategy.ORG_ROLE.toString().equals(nodeSettingCreateBean.getStrategy())) {
			
			Assert.hasText(nodeSettingCreateBean.getRoles(), "角色不能为空");
			for(String roleId : nodeSettingCreateBean.getRoles().split(",")) {
				Assert.notNull(wfResourcesService.findRole(roleId), "角色设置不正确：" + roleId);
			}
			
			Assert.hasText(nodeSettingCreateBean.getOrgRoleMode(), "组织机构下通过角色找审批人，部门模式不能为空");
			Assert.isTrue(WFOrgRoleMode.include(nodeSettingCreateBean.getOrgRoleMode()), "组织机构角色选人部门模式设置不正确");
			// TODO 前台是单选，需要调整成多选
			
			if(WFOrgRoleMode.PARAMDEPT.toString().equals(nodeSettingCreateBean.getOrgRoleMode())) {
				Assert.hasText(nodeSettingCreateBean.getDeptParamId(), "流程参数中指定部门方式，部门参数不能为空");
				wfTemplateParamJpaRepo.findById(nodeSettingCreateBean.getDeptParamId())
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
			}
		} else if(WFApproverStrategy.PARAMUSER.toString().equals(nodeSettingCreateBean.getStrategy())) {
			Assert.hasText(nodeSettingCreateBean.getUserParamId(), "流程参数中指定部门方式，部门参数不能为空");
			wfTemplateParamJpaRepo.findById(nodeSettingCreateBean.getUserParamId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
		}
		
		//TODO 每个节点最好记录下当时的参数
		
		Assert.isTrue(WFApprovalMode.include(nodeSettingCreateBean.getApprovalMode()), "节点审批模式设置不正确");
		
		nodeSetting = new WFTemplateNodeSettingEntity();
		nodeSetting.setApprovalMode(nodeSettingCreateBean.getApprovalMode());
		nodeSetting.setId(UUID.randomUUID().toString());
		nodeSetting.setNodeId(nodeSettingCreateBean.getNodeId());
		nodeSetting.setApproverService(nodeSettingCreateBean.getApproverService());
		nodeSetting.setStrategy(nodeSettingCreateBean.getStrategy());
		nodeSetting.setRoles(nodeSettingCreateBean.getRoles());
		nodeSetting.setFormURL(nodeSettingCreateBean.getFormURL());
		nodeSetting.setHandleMode(nodeSettingCreateBean.getHandleMode());
		nodeSetting.setPassType(nodeSettingCreateBean.getPassType());
		nodeSetting.setFunctions(nodeSettingCreateBean.getFunctions());
		nodeSetting.setOrgRoleMode(nodeSettingCreateBean.getOrgRoleMode());
		nodeSetting.setUpSearch(nodeSettingCreateBean.getUpSearch());
		nodeSetting.setDeptParamId(nodeSettingCreateBean.getDeptParamId());
		nodeSetting.setPassService(nodeSettingCreateBean.getPassService());
		nodeSetting.setUserParamId(nodeSettingCreateBean.getUserParamId());
		
		wfTemplateNodeSettingJpaRepo.save(nodeSetting);
	}
	
	/**
	 * 删除节点属性
	 * @param settingId 节点属性ID
	 */
	@Transactional
	public void deleteNodeSetting(String settingId) {
		wfTemplateNodeSettingJpaRepo.deleteById(settingId);
	}
	
	/**
	 * 更新节点属性
	 * @param settingId 节点扩展ID
	 * @param handleType 审批模式
	 * @param strategy 审批人选择策略
	 * @param userService 自定义选人实现类
	 * @param roles 角色
	 * @param formURL 审批表单url
	 * @param approvalType 审批类型
	 * @param passType 工作项通过类型
	 * @param functions 审批功能
	 * @param orgRoleMode 选择组织机构的方式
	 * @param upSearch 是否向上级查找
	 * @param paramId 参数中指定部门ID是，指定参数ID
	 * @param passService 自定义节点通过的实现类名称
	 * @return 成功标志
	 */
	@Transactional
	public void updateNodeSetting(WFTemplateNodeSettingUpdateBean nodeSettingUpdateBean) {
		
		WFTemplateNodeSettingEntity nodeSetting = wfTemplateNodeSettingJpaRepo.findById(nodeSettingUpdateBean.getSettingId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "流程节点配置信息不存在"));
		
		Assert.hasText(nodeSettingUpdateBean.getStrategy(), "流程节点选人策略不能为空");
		
		Assert.isTrue(WFNodeHandleMode.include(nodeSettingUpdateBean.getHandleMode()), "节点审批类型设置不正确");
		Assert.isTrue(WFApprovalPassType.include(nodeSettingUpdateBean.getPassType()), "工作项通过类型设置不正确");
		
		if(StringUtils.isNotBlank(nodeSettingUpdateBean.getFunctions())) {
			for(String function : nodeSettingUpdateBean.getFunctions().split(",")) {
				Assert.isTrue(WFFunctions.include(function), "节点功能设置不正确");
			}
		}
		
		Assert.isTrue(WFApproverStrategy.include(nodeSettingUpdateBean.getStrategy()), "节点选人策略设置不正确");
		if(WFApproverStrategy.SERVICE.toString().equals(nodeSettingUpdateBean.getStrategy())) {
			Assert.hasText(nodeSettingUpdateBean.getApproverService(), "自定义实现类找审批人时，实现类设置不能为空");
		} else if(WFApproverStrategy.ROLE.toString().equals(nodeSettingUpdateBean.getStrategy())) {
			Assert.hasText(nodeSettingUpdateBean.getRoles(), "角色下找审批人，角色不能为空");
			for(String roleId : nodeSettingUpdateBean.getRoles().split(",")) {
				Assert.notNull(wfResourcesService.findRole(roleId), "角色设置不正确：" + roleId);
			}
		} /*else if(WFApproverStrategy.ORG.toString().equals(nodeSettingUpdateBean.getStrategy())) {
			Assert.hasText(nodeSettingUpdateBean.getDeptParamIds(), "组织机构下找审批人，部门参数不能为空");
			for(String paramId : nodeSettingUpdateBean.getDeptParamIds().split(",")) {
				wfTemplateParamJpaRepo.findById(paramId)
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
			}
		}*/ else if(WFApproverStrategy.ORG_ROLE.toString().equals(nodeSettingUpdateBean.getStrategy())) {
			
			Assert.hasText(nodeSettingUpdateBean.getRoles(), "角色不能为空");
			for(String roleId : nodeSettingUpdateBean.getRoles().split(",")) {
				Assert.notNull(wfResourcesService.findRole(roleId), "角色设置不正确：" + roleId);
			}
			
			Assert.hasText(nodeSettingUpdateBean.getOrgRoleMode(), "组织机构下通过角色找审批人，部门模式不能为空");
			Assert.isTrue(WFOrgRoleMode.include(nodeSettingUpdateBean.getOrgRoleMode()), "组织机构角色选人部门模式设置不正确");
			
			// 参数是单元，参数里面可以放多个值
			
			if(WFOrgRoleMode.PARAMDEPT.toString().equals(nodeSettingUpdateBean.getOrgRoleMode())) {
				Assert.hasText(nodeSettingUpdateBean.getDeptParamId(), "流程参数中指定部门方式，部门参数不能为空");
				wfTemplateParamJpaRepo.findById(nodeSettingUpdateBean.getDeptParamId())
					.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
			}
		} else if(WFApproverStrategy.PARAMUSER.toString().equals(nodeSettingUpdateBean.getStrategy())) {
			Assert.hasText(nodeSettingUpdateBean.getUserParamId(), "流程参数中指定部门方式，部门参数不能为空");
			wfTemplateParamJpaRepo.findById(nodeSettingUpdateBean.getUserParamId())
				.orElseThrow(() -> new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "组织机构下找审批人，部门参数设置不正确"));
		}
		
		Assert.isTrue(WFApprovalMode.include(nodeSettingUpdateBean.getApprovalMode()), "节点审批模式设置不正确");
		
		nodeSetting.setApprovalMode(nodeSettingUpdateBean.getApprovalMode());
		nodeSetting.setApproverService(nodeSettingUpdateBean.getApproverService());
		nodeSetting.setStrategy(nodeSettingUpdateBean.getStrategy());
		nodeSetting.setRoles(nodeSettingUpdateBean.getRoles());
		nodeSetting.setFormURL(nodeSettingUpdateBean.getFormURL());
		nodeSetting.setHandleMode(nodeSettingUpdateBean.getHandleMode());
		nodeSetting.setPassType(nodeSettingUpdateBean.getPassType());
		nodeSetting.setFunctions(nodeSettingUpdateBean.getFunctions());
		nodeSetting.setOrgRoleMode(nodeSettingUpdateBean.getOrgRoleMode());
		nodeSetting.setUpSearch(nodeSettingUpdateBean.getUpSearch());
		nodeSetting.setDeptParamId(nodeSettingUpdateBean.getDeptParamId());
		nodeSetting.setPassService(nodeSettingUpdateBean.getPassService());
		nodeSetting.setUserParamId(nodeSettingUpdateBean.getUserParamId());
		
		wfTemplateNodeSettingJpaRepo.save(nodeSetting);
	}
	
	/**
	 * 在流程启动前预处理流程信息<br>
	 * 即根据参数提前判断出流程的节点<br>
	 * 该方法只获取对应的审批节点的定义
	 * @param templateId 流程模板ID
	 * @param params 运行时参数
	 */
	public List<WFTemplateNodeEntity> pretreatmentProcess(String templateId, Map<String, String> params) {
		
		List<WFTemplateNodeEntity> returnList = new ArrayList<>();
		
		List<WFTemplateParamEntity> templateParams = wfTemplateParamJpaRepo.findByTemplateId(templateId);
		
		List<ProcessParamVO> paramVOs = new ArrayList<>();
		
		// 将流程参数保存起来
		for(WFTemplateParamEntity templateParam : templateParams) {
			
			ProcessParamVO vo = new ProcessParamVO();
			vo.setProperty(templateParam.getProperty());
			vo.setValueType(templateParam.getValueType());
			
			// 从传入参数中获取对应值,否则参数值为初始参数值
			if(params != null && params.containsKey(templateParam.getProperty())) {
				vo.setValue(params.get(templateParam.getProperty()));
			}else {
				vo.setValue(templateParam.getInitValue());
			}
			
			paramVOs.add(vo);
		}
		
		// 获取流程节点
		List<WFTemplateNodeEntity> nodes = wfTemplateNodeJapRepo.findByTemplateIdOrderByLevelAsc(templateId);
		
		// 存储流程节点临时缓存、流程审批设置临时缓存
		Map<String, WFTemplateNodeEntity> nodeMap = new HashMap<>();
		Map<String, WFTemplateNodeSettingEntity> settingMap = new HashMap<>();
		
		// 起始节点ID
		String beginNodeId = "";
		
		for(WFTemplateNodeEntity node : nodes) {
			nodeMap.put(node.getId(), node);
			if(WFNodeType.WORK.toString().equals(node.getType())) {
				WFTemplateNodeSettingEntity setting = wfTemplateNodeSettingJpaRepo.findByNodeId(node.getId());
				settingMap.put(node.getId(), setting);
			}
			
			if(WFNodeType.BEGIN.toString().equals(node.getType())) {
				beginNodeId = node.getId();
			}
		}
		
		// 获取节点之间关系
		List<WFTemplateLineEntity> lines = wfTemplateLineJpaRepo.findByTemplateId(templateId);
		// 记录起始节点和终止节点的关系临时缓存
		Map<String, List<WFTemplateLineEntity>> relations = new HashMap<>();
		
		for(WFTemplateLineEntity line : lines) {
			if(!relations.containsKey(line.getBeginNodeId())) {
				List<WFTemplateLineEntity> list = new ArrayList<>();
				relations.put(line.getBeginNodeId(), list);
			}
			relations.get(line.getBeginNodeId()).add(line);
		}
		// 计数器，防止while循环中，由于数据错误造成的死循环，控制在10,000次循环强制跳出
		int count = 0;
		while(count ++ < 10000) {
			
			// 不存在当前节点，则终止循环 TODO beginNodeId 是list
			if(!nodeMap.containsKey(beginNodeId)) {
				break;
			}
			
			// 找不到起始节点对应的终止节点，则终止循环
			// 当该节点是结束节点， 也找不到对应的终止节点，也就终止循环了
			if(!relations.containsKey(beginNodeId) 
					|| relations.get(beginNodeId) == null 
					|| relations.get(beginNodeId).size() == 0) {
				break;
			}
			
			WFTemplateNodeEntity beginNode = nodeMap.get(beginNodeId);
			
			// 审批节点
			if(WFNodeType.WORK.toString().equals(beginNode.getType())) {
				returnList.add(beginNode);
			}else if(WFNodeType.CONDITION.toString().equals(beginNode.getType())) {
				// 用于校验表达式匹配的标识，如果匹配则直接停止循环
				boolean match = false;

				// 分支节点，根据传入参数判断
				for(WFTemplateLineEntity entity : relations.get(beginNodeId)) {
					boolean result = expressionEngineService.match(entity.getRouteExpression(), paramVOs);
					if(result == true) {
						beginNodeId = entity.getEndNodeId();
						match = true;
						break;
					}
				}

				if(match == true) {
					continue;
				}
			}
			
			beginNodeId = relations.get(beginNodeId).get(0).getEndNodeId();
		}
		
		return returnList;
	}
}

package com.easywf.wf.template.bean;

import java.util.ArrayList;
import java.util.List;

import com.easywf.wf.template.entity.WFTemplateEntity;
import com.easywf.wf.template.entity.WFTemplateNodeEntity;
import com.easywf.wf.template.entity.WFTemplateNodeSettingEntity;
import com.easywf.wf.template.entity.WFTemplateParamEntity;

public class WFTemplateDetailVO {

	// 流程模板对象
	private WFTemplateEntity template;
	
	// 流程参数
	private List<WFTemplateParamEntity> params = new ArrayList<>();
	
	// 流程节点
	private List<WFTemplateNodeDetailVO> nodes = new ArrayList<>();
	
	// 流程节点关系
	private List<WFTemplateLineDetailVO> lines = new ArrayList<>();

	public void addNode(WFTemplateNodeEntity node, WFTemplateNodeSettingEntity setting) {
		WFTemplateNodeDetailVO vo = new WFTemplateNodeDetailVO();
		vo.setSetting(setting == null ? new WFTemplateNodeSettingEntity() : setting);
		vo.setNode(node);
		this.nodes.add(vo);
	}
	
	public WFTemplateEntity getTemplate() {
		return template;
	}

	public void setTemplate(WFTemplateEntity template) {
		this.template = template;
	}

	public List<WFTemplateParamEntity> getParams() {
		return params;
	}

	public void setParams(List<WFTemplateParamEntity> params) {
		this.params = params;
	}

	public List<WFTemplateNodeDetailVO> getNodes() {
		return nodes;
	}

	public void setNodes(List<WFTemplateNodeDetailVO> nodes) {
		this.nodes = nodes;
	}

	public List<WFTemplateLineDetailVO> getLines() {
		return lines;
	}

	public void setLines(List<WFTemplateLineDetailVO> lines) {
		this.lines = lines;
	}

}

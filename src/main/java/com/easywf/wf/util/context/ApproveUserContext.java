package com.easywf.wf.util.context;

import java.util.ArrayList;
import java.util.List;

import com.easywf.wf.process.vo.ProcessParamVO;

/**
 * 自定义选择审批用户的上下文对象，包括流程基础信息和当前选择用户的节点相关信息<br>
 * 其余信息可以通过调用流程获取详细信息来取得更多的上下文对象
 * @author wangcai
 * @version 1.0
 */
public class ApproveUserContext extends ProcessBaseContext{

	// 选择审批人的节点信息
	private WorkitemBaseContext workitem;
	
	// 流程参数
	private List<ProcessParamVO> params = new ArrayList<>();

	public WorkitemBaseContext getWorkitem() {
		return workitem;
	}

	public void setWorkitem(WorkitemBaseContext workitem) {
		this.workitem = workitem;
	}

	public List<ProcessParamVO> getParams() {
		return params;
	}

	public void setParams(List<ProcessParamVO> params) {
		this.params = params;
	}

}

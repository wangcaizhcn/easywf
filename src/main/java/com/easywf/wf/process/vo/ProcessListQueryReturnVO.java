package com.easywf.wf.process.vo;

import java.util.List;

/**
 * 查询流程列表的返回对象
 * @author wangcai
 * @version 1.0
 */
public class ProcessListQueryReturnVO {

	// 查询信息
	private ProcessListQuery query;
	
	// 结果列表
	private List<ProcessListVO> processList;
	
	// 列表总条数
	private long count;

	public ProcessListQuery getQuery() {
		return query;
	}

	public void setQuery(ProcessListQuery query) {
		this.query = query;
	}

	public List<ProcessListVO> getProcessList() {
		return processList;
	}

	public void setProcessList(List<ProcessListVO> processList) {
		this.processList = processList;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
}

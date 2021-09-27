package com.easywf.wf.common;

public class PageableQueryBean {

	// 每页显示多少条
	private int pageSize = 10;
	
	// 查询的页码
	private int pageNum = 1;

	public int getPageSize() {
		return pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	
}

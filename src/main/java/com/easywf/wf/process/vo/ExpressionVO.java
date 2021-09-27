package com.easywf.wf.process.vo;

/**
 * 路由表达式解析成可以进行计算的对象
 * @author wangcai
 *
 */
public class ExpressionVO {

	// 表达式属性名称
	private String key;

	// 表达式值
	private String value;
	
	// 操作符
	private String op;
	
	// 逻辑运算符
	private String lop;
	
	// 逻辑运算的另外一部分
	private ExpressionVO le;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getLop() {
		return lop;
	}

	public void setLop(String lop) {
		this.lop = lop;
	}

	public ExpressionVO getLe() {
		return le;
	}

	public void setLe(ExpressionVO le) {
		this.le = le;
	}
	
}

package com.easywf.wf.process.service;

import org.apache.commons.lang3.StringUtils;

import com.easywf.wf.process.vo.ExpressionVO;
import com.easywf.wf.template.constant.WFOperator;

/**
 * 表达式解析引擎
 * @author wangcai
 * @version 1.0
 */
public class ExpressionAnalysisService implements WFOperator {

	/**
	 * 将路由表达式解析成表达式对象
	 * @param expression 完整的路由表达式
	 * @return 可以进行计算的对象
	 */
	public static ExpressionVO analysis(String expression) {

		if(StringUtils.isBlank(expression)) {
			return null;
		}
		// 首先解析是否有逻辑运算符
		int i = expression.indexOf(OP_AND);
		int j = expression.indexOf(OP_OR);
		
		// 不存在逻辑运算符，则是一个最小的表达式
		if(i < 0 && j < 0) {
			return atomAnalysis(expression);
		}
		
		// 有逻辑运算符， 依次解析逻辑运算符分隔的表达式
		int index = 0;
		String lop = "";
		if(i == -1) {
			lop = OP_OR;
			index = j;
		}else if(j == -1) {
			lop = OP_AND;
			index = i;
		}else {
			index = Math.min(i, j);
			lop = i < j ? OP_AND : OP_OR;
		}
		
		String first = expression.substring(0, index);
		String second = expression.substring(index + 1);
		ExpressionVO vo = atomAnalysis(first);
		if(vo == null) {
			System.out.println("表达式错误，无法解析：" + first);
			return null;
		}
		ExpressionVO leVO = analysis(second);
		vo.setLop(lop);
		vo.setLe(leVO);
		return vo;
	}
	
	/**
	 * 解析最小的表达式
	 * @param expression
	 * @return
	 */
	private static ExpressionVO atomAnalysis(String expression) {
		String op = "";
		if(expression.indexOf(NOT_EQUAL) > -1) {
			op = NOT_EQUAL;
		}else if(expression.indexOf(LARGE_EQUAL_THAN) > -1) {
			op = LARGE_EQUAL_THAN;
		}else if(expression.indexOf(LESS_EQUAL_THAN) > -1) {
			op = LESS_EQUAL_THAN;
		}else if(expression.indexOf(EQUAL) > -1) {
			op = EQUAL;
		}else if(expression.indexOf(LARGE_THAN) > -1) {
			op = LARGE_THAN;
		}else if(expression.indexOf(LESS_THAN) > -1) {
			op = LESS_THAN;
		}else {
			System.out.println("表达式错误，未包含比较运算符，无法解析：" + expression);
			return null;
		}
		
		return createExpression(expression, op);
	}
	
	/**
	 * 将字符串解析成表达式对象
	 * @param expression
	 * @param op
	 * @return
	 */
	private static ExpressionVO createExpression(String expression, String op) {
		ExpressionVO vo = new ExpressionVO();
		String[] exp = expression.split(op);
		if(StringUtils.isNotBlank(exp[0]) && StringUtils.isNotBlank(exp[1])) {
			exp[0] = exp[0].trim();
			exp[1] = exp[1].trim();
			if(exp[0].startsWith(VARIABLE_PREFIX) && exp[0].endsWith(VARIABLE_SUFFIX)) {
				vo.setKey(exp[0].replace(VARIABLE_PREFIX, "").replace(VARIABLE_SUFFIX, ""));
				vo.setValue(exp[1]);
				vo.setOp(op);
			}else if(exp[1].startsWith(VARIABLE_PREFIX) && exp[1].endsWith(VARIABLE_SUFFIX)) {
				// 特别注意， >、>=、<、<= 如果调换位置，则操作符号也应该调换
				vo.setKey(exp[1].replace(VARIABLE_PREFIX, "").replace(VARIABLE_SUFFIX, ""));
				vo.setValue(exp[0]);
				if(op.equals(LARGE_THAN)) {
					vo.setOp(LESS_THAN);
				}else if(op.equals(LARGE_EQUAL_THAN)) {
					vo.setOp(LESS_EQUAL_THAN);
				}else if(op.equals(LESS_THAN)) {
					vo.setOp(LARGE_THAN);
				}else if(op.equals(LESS_EQUAL_THAN)) {
					vo.setOp(LARGE_EQUAL_THAN);
				}
			}else {
				System.out.println("表达式错误，未包含变量，无法解析：" + expression);
			}
			return vo;
		} else {
			System.out.println("表达式错误，无法解析：" + expression);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String str = "{{flag}} != test & 100000 <= {{hte}} | {{user}} = wangcai & {{state}} = true";
		ExpressionAnalysisService s = new ExpressionAnalysisService();
		ExpressionVO vo = s.analysis(str);
		System.out.println(vo);
	}
}

package com.easywf.wf.process.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import org.springframework.stereotype.Service;

import com.easywf.wf.process.vo.ExpressionVO;
import com.easywf.wf.process.vo.ProcessParamVO;
import com.easywf.wf.template.constant.WFOperator;
import com.easywf.wf.template.constant.WFParamValueType;

/**
 * 路由表达式解析计算引擎
 * @author wangcai
 * @version 1.5
 * @since 1.5 表达式计算从左到右全部计算，支持括号
 */
@Service
public class ExpressionEngineService implements WFOperator{
	
//	@Autowired
//	private ExpressionAnalysisService expressionAnalysisService;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	
	private static final String INNER_PARAM_PROPERTY = "$_WF_FLAG";
	
	
	private static ProcessParamVO innerParam = new ProcessParamVO();
	static {
		innerParam.setProperty(INNER_PARAM_PROPERTY);
		innerParam.setValueType(WFParamValueType.BOOLEAN.toString());
		innerParam.setValue("true");
	}
	
	/**
	 * 通过实际参数，判断路由表达式是否正确
	 * @param expression 路由表达式
	 * @param params 实际参数集合
	 * @return true:判断成功， false:判断失败
	 */
	public boolean match(String expression, List<ProcessParamVO> params) {
		
		params.add(innerParam);
		
		while(expression.indexOf("(") > -1 && expression.indexOf(")") > -1) {
			Stack<Integer> indexS = new Stack<>();
			boolean match = false;
			for(int i = 0; i < expression.length(); i ++) {
				if(expression.charAt(i) == '(') {
					indexS.push(i);
					continue;
				}
				
				if(expression.charAt(i) == ')') {
					if(indexS.size() == 0) {
						throw new RuntimeException("表达式错误，括号不匹配，缺失左括号");
					}
					int ii = indexS.pop();
					
					boolean result = calc(expression.substring(ii + 1, i), params);
					
					expression = expression.substring(0, ii) + " {{" + INNER_PARAM_PROPERTY + "}} = " + result + expression.substring(i + 1, expression.length());
					System.out.println(expression);
					match = true;
					break;
				}
			}
			
			if(match) continue;
			
			if(indexS.size() != 0) {
				throw new RuntimeException("表达式错误，括号不匹配，缺失右括号");
			}
		}
		return calc(expression, params);
	}
	
	/**
	 * 通过实际参数，判断路由表达式是否正确
	 * @param expression 路由表达式
	 * @param params 实际参数集合
	 * @return true:判断成功， false:判断失败
	 */
	private boolean calc(String expression, List<ProcessParamVO> params) {
		// 表达式最后是否取反， ture表示不取非， false表示取非
		boolean flag = true;
		if(expression.trim().startsWith(OP_INVERT)) {
			flag = false;
			expression = expression.replace(OP_INVERT, "");
		}
		
		ExpressionVO expVO = ExpressionAnalysisService.analysis(expression);
		boolean result = calc(expVO, params, "", true);
		if(flag == true) {
			return result;
		}else {
			return !result;
		}
	}
	
	/**
	 * 计算表达式结果
	 * @param expVO 可计算的表达式对象
	 * @param params 动态参数值
	 * @param lop 逻辑运算符
	 * @param result 上传逻辑运算结果
	 * @return
	 */
	private boolean calc(ExpressionVO expVO, List<ProcessParamVO> params, String lop, boolean result) {
		
		// 当前迭代中的计算结果
		boolean thisResult = false;
		
		// 如果是与操作， 当result为false时，不再执行本迭代中的运算
		// false && 表达式， 此时不论表达式为false还是true，结果都为false
		if(OP_AND.equals(lop) && result == false) {
			thisResult = false;
		}else if(OP_OR.equals(lop) && result == true) {
			// 如果是或操作， 当result为true时，不再执行本迭代中的运算
			// true && 表达式， 此时不论表达式为false还是true，结果都为true
			thisResult = true;
		}else {
			// 计算表达式
			// 首次进来，没有逻辑操作
			// 与操作，上次是true，需要判断这次是否为true
			// 或操作，上次是false，需要判断这次是否为false
			for(ProcessParamVO param : params) {
				if(param.getProperty().equals(expVO.getKey())) {
					// 每种操作单独处理条件运算
					switch (expVO.getOp()){
						case EQUAL:
							thisResult = evalEqual(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						case NOT_EQUAL:
							thisResult = evalNotEqual(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						case LARGE_THAN:
							thisResult = evalLargeThan(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						case LARGE_EQUAL_THAN:
							thisResult = evalLargeEqualThan(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						case LESS_THAN:
							thisResult = evalLessThan(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						case LESS_EQUAL_THAN:
							thisResult = evalLessEqualThan(param.getValueType(), expVO.getValue(), param.getValue());
							break;
						default:
							System.out.println("操作符无法解析：" + expVO.getOp());
					}
					
					// 与操作，计算总体结果
					if(OP_AND.equals(lop)) {
						if(result == true && thisResult == true) {
							thisResult = true;
						}else {
							thisResult = false;
						}
					}
					// 或操作，计算总体结果
					if(OP_OR.equals(lop)) {
						if(result == false && thisResult == false) {
							thisResult = false;
						}else {
							thisResult = true;
						}
					}
					break;
				}
			}
		}
		
		// 不存在逻辑运算，则直接返回
		if(expVO.getLe() == null) {
			return thisResult;
		}
		
		// 递归计算下个表达式
		return calc(expVO.getLe(), params, expVO.getLop(), thisResult);
	}
	
	/**
	 * =条件运算的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalEqual(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString())) {
			
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
//			if(expValue.startsWith("\"") || expValue.startsWith("'")) {
//				expValue = expValue.substring(1);
//			}
//			if(expValue.endsWith("\"") || expValue.endsWith("'")) {
//				expValue = expValue.substring(0, expValue.length() - 1);
//			}
			return expValue.equals(cValue);
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _expValue == _cValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _expValue == _cValue; 
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _expValue.getTime() == _cValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}else if(valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			Boolean _expValue = Boolean.valueOf(expValue);
			Boolean _cValue = Boolean.valueOf(cValue);
			return _expValue == _cValue; 
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}
	
	/**
	 * ！= 运算的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalNotEqual(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			return !expValue.equals(cValue);
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _expValue != _cValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _expValue != _cValue; 
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _expValue.getTime() != _cValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}else if(valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			Boolean _expValue = Boolean.valueOf(expValue);
			Boolean _cValue = Boolean.valueOf(cValue);
			return _expValue != _cValue; 
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}
	
	/**
	 * > 操作符的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalLargeThan(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString()) || valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			System.out.println("字符串和布尔类型不支持>运算符");
			return false;
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _cValue > _expValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _cValue > _expValue;
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _cValue.getTime() > _expValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}
	
	/**
	 * >= 操作符的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalLargeEqualThan(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString()) || valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			System.out.println("字符串和布尔类型不支持>=运算符");
			return false;
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _cValue >= _expValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _cValue >= _expValue;
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _cValue.getTime() >= _expValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}

	/**
	 * < 操作符的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalLessThan(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString()) || valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			System.out.println("字符串和布尔类型不支持<运算符");
			return false;
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _cValue < _expValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _cValue < _expValue;
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _cValue.getTime() < _expValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}
	
	/**
	 * <= 操作符的处理逻辑
	 * @param valueType
	 * @param expValue
	 * @param cValue
	 * @return
	 */
	private boolean evalLessEqualThan(String valueType, String expValue, String cValue) {
		if(valueType.equals(WFParamValueType.STRING.toString()) || valueType.equals(WFParamValueType.BOOLEAN.toString())) {
			System.out.println("字符串和布尔类型不支持<=运算符");
			return false;
		}else if(valueType.equals(WFParamValueType.LONG.toString())) {
			long _expValue = Long.valueOf(expValue);
			long _cValue = Long.valueOf(cValue);
			return _cValue <= _expValue;
		}else if(valueType.equals(WFParamValueType.DOUBLE.toString())) {
			double _expValue = Double.valueOf(expValue);
			double _cValue = Double.valueOf(cValue);
			return _cValue <= _expValue;
		}else if(valueType.equals(WFParamValueType.DATE.toString())) {
			if(expValue.startsWith("\"") && expValue.endsWith("\"") || expValue.startsWith("'") && expValue.endsWith("'")) {
				expValue = expValue.substring(1, expValue.length() - 1);
			}
			try {
				Date _expValue = sdf.parse(expValue);
				Date _cValue = sdf.parse(cValue);
				return _cValue.getTime() <= _expValue.getTime(); 
			}catch(Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("错误的值类型：" + valueType);
		return false;
	}
	
	public static void main(String[] args) {
		List<ProcessParamVO> params = new ArrayList<>();
		ProcessParamVO vo = new ProcessParamVO();
		vo.setProperty("date1");
		vo.setValueType(WFParamValueType.DATE.toString());
		vo.setValue("2019-01-01");
		params.add(vo);
		String exp = "{{date1}} <= \"2019-01-01\"";
		ExpressionEngineService s = new ExpressionEngineService();
		boolean res1 = s.calc(exp, params);
		System.out.println(res1);
	}
	
	public static void main1(String[] args) {
		
		// 设置参数
		List<ProcessParamVO> params = new ArrayList<>();
		ProcessParamVO vo = new ProcessParamVO();
		vo.setProperty("a");
		vo.setValueType(WFParamValueType.LONG.toString());
		vo.setValue("10");
		params.add(vo);
		
		String exp1 = "{{a}} > 10 & {{a}} < 11 | {{a}} = 10";  // false && true || true 从左到右计算，最后结果true
		String exp2 = "{{a}} > 9 | {{a}} < 10 | {{a}} < 11 & {{a}} != 10"; // true || false || true && false // 从左到右计算，最后结果false
		
		// true || (false || true && false)
		// 优先计算括号， 括号中从左到右计算，结果为false， 最后计算 true || (false) 结果为true
		String exp3 = "{{a}} > 9 | ({{a}} < 10 | {{a}} < 11 & {{a}} != 10)"; 
		
		// false || !(false || false)
		// 首先计算括号内的值，结果为false，在括号内部的表达式中有取反操作，所以最后结果为true，  最后计算 false || (!false)
		String exp4 = "{{a}} < 9 | (@! {{a}} < 10 | {{a}} > 11)";
		
		ExpressionEngineService s = new ExpressionEngineService();
		boolean res1 = s.calc(exp1, params);
		boolean res2 = s.calc(exp2, params);
		boolean res3 = s.match(exp3, params);
		boolean res4 = s.match(exp4, params);
		System.out.println(res1);
		System.out.println(res2);
		System.out.println(res3);
		System.out.println(res4);
		
	}
	
}

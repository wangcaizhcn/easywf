package com.easywf.wf.util.service;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;

import com.easywf.wf.common.EasyWFException;
import com.easywf.wf.common.ResultCodeConstant;
import com.easywf.wf.template.constant.WFParamValueType;

public class ParamValidityService {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void check(String value, String type) {
		
		if(type.equals(WFParamValueType.STRING.toString())) {
			return;
		}
		
		if(type.equals(WFParamValueType.LONG.toString())) {
			try {
				Long.valueOf(value);
			} catch(Exception e) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "Long类型参数对应的值不正确");
			}
		} else if(type.equals(WFParamValueType.DOUBLE.toString())) {
			try {
				Double.valueOf(value);
			} catch(Exception e) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "Double类型参数对应的值不正确");
			}
		} else if(type.equals(WFParamValueType.DATE.toString())) {
			try {
				sdf.parse(value);
			} catch(Exception e) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "Date类型参数对应的值不正确");
			}
		} else if(type.equals(WFParamValueType.BOOLEAN.toString())) {
			try {
				Boolean.valueOf(value);
			} catch(Exception e) {
				throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "Boolean类型参数对应的值不正确");
			}
		} else {
			throw new EasyWFException(ResultCodeConstant.ARGUMENT_INVALID_ERROR, "未知的参数值类型：" + type);
		}
	}
}

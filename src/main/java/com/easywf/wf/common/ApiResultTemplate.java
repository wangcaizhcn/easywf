package com.easywf.wf.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

public class ApiResultTemplate {

    private static <T> ApiResult<T> result(T data, String code, String message) {
        
    	ApiResult<T> result = new ApiResult<>();
    	result.setCode(code);
    	result.setMessage(message);
    	result.setData(data);
    	
    	return result;
    	
    }

    public static <T> ApiResult<T> result(String code) {
        return result(null, code, null);
    }

    public static <T> ApiResult<T> success(T data) {
        return result(data, ResultCodeConstant.REQUEST_SUCCESS, null);
    }

    public static <T> ApiResult<T> success(T data, String message) {
        return result(data, ResultCodeConstant.REQUEST_SUCCESS, message);
    }

    public static <T> ApiResult<T> success(String message) {
        return result(null, ResultCodeConstant.REQUEST_SUCCESS, message);
    }

    public static <T> ApiResult<List<T>> success(List<T> list) {
    	
    	ApiResult<List<T>> result = new ApiResult<>();
    	
    	result.setCode(ResultCodeConstant.REQUEST_SUCCESS);
    	result.setData(list);
    	result.setTotal(list.size());
    	
    	return result;
    }
    
    public static <T> ApiResult<List<T>> success(List<T> list, String message) {
    	
    	ApiResult<List<T>> result = new ApiResult<>();
    	
    	result.setCode(ResultCodeConstant.REQUEST_SUCCESS);
    	result.setData(list);
    	result.setTotal(list.size());
    	result.setMessage(message);
    	
    	return result;
    }
    
    public static <T> ApiResult<List<T>> success(Page<T> page) {
    	
    	ApiResult<List<T>> result = new ApiResult<>();
    	
    	result.setCode(ResultCodeConstant.REQUEST_SUCCESS);
    	result.setData(page.getContent());
    	result.setTotal(page.getTotalElements());
    	
    	return result;
    }

    public static <T> ApiResult<T> success() {
        return result(ResultCodeConstant.REQUEST_SUCCESS);
    }

    public static <T> ApiResult<T> failure() {
        return result(ResultCodeConstant.SYSTEM_ERROR);
    }

    public static <T> ApiResult<T> failure(String code) {
        return failure(code, null);
    }

    public static <T> ApiResult<T> failure(String code, String message) {
        if (ResultCodeConstant.REQUEST_SUCCESS.equals(code) ) {
            throw new RuntimeException("成功的标识码不能用于失败返回");
        }
        return result(null, code, message);
    }

    public static <T> ApiResult<T> failure(EasyWFException ex) {
    	
    	ApiResult<T> result = new ApiResult<>();
    	result.setCode(StringUtils.isBlank(ex.getCode()) ? ResultCodeConstant.SYSTEM_ERROR : ex.getCode());
    	result.setMessage(ex.getMessage());
        return result;
    }

}

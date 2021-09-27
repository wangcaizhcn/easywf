package com.easywf.wf.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理，只处理com.easywf.wf包下的，不影响业务包
 */
@ControllerAdvice(basePackages = {"com.easywf.wf"/* , "com.test" */})
public class GlobalExceptionHandler {

	private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class); 
	
	/**
	 * 业务异常
	 * @param <T>
	 * @param ex
	 * @return
	 */
    @ExceptionHandler(EasyWFException.class)
    @ResponseBody
    public <T> ApiResult<T> exceptionHandler(EasyWFException ex) {
        log.error("<== 异常：({}, {})", ex.getCode(), ex.getMessage());
        return ApiResultTemplate.failure(ex);
    }

    /**
     * Assert 拦截
     * @param <T>
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public <T> ApiResult<T> exceptionHandler(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResultTemplate.failure(ResultCodeConstant.ARGUMENT_INVALID_ERROR, ex.getMessage());
    }

    /**
     * 未预料异常
     * @param <T>
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public <T> ApiResult<T> exceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        return ApiResultTemplate.failure(ResultCodeConstant.SYSTEM_ERROR, ex.getMessage());
    }
}

package com.easywf.wf.process.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.easywf.wf.common.ApiResult;
import com.easywf.wf.common.ApiResultTemplate;
import com.easywf.wf.process.bean.ApproveBean;
import com.easywf.wf.process.bean.WFProcessCreateBean;
import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.constant.WFProcessState;
import com.easywf.wf.process.constant.WFWorkitemApproveResult;
import com.easywf.wf.process.service.ProcessInstanceService;
import com.easywf.wf.process.service.ProcessQueryOldService;
import com.easywf.wf.process.vo.ApproveDetailVO;
import com.easywf.wf.process.vo.ProcessDetailVO;
import com.easywf.wf.process.vo.ProcessListQuery;
import com.easywf.wf.process.vo.ProcessListQueryReturnVO;
import com.easywf.wf.process.vo.ProcessListVO;

@RestController
public class ProcessController {

	@Autowired
	private ProcessInstanceService processInstanceService;
	
	@Autowired
	private ProcessQueryOldService processQueryService;
	
	@PostMapping("/wf/process/test")
	public ApiResult<String> test(String templateId, String promoter, String recordId, 
		String title, String reason, String paramStr) {
		//paramStr的规则  key#valuekey#value
		Map<String, String> param = new HashMap<>();
		if(StringUtils.isNotBlank(paramStr)) {
			String[] str = paramStr.split("\\*!\\*");
			for(String p : str) {
				if(StringUtils.isNotBlank(p)) {
					String[] temp = p.split("\\*");
					param.put(temp[0], temp.length == 1? "" : temp[1]);
				}
			}
		}
		
		WFProcessCreateBean processCreateBean = new WFProcessCreateBean();
		processCreateBean.setBaseTemplateId(templateId);
		processCreateBean.setPromoter(promoter);
		processCreateBean.setRecordId(recordId);
		processCreateBean.setTitle(title);
		processCreateBean.setReason(reason);
		processCreateBean.setParams(param);
		
		String processId = processInstanceService.createAndStartProcess(processCreateBean);
		return ApiResultTemplate.success(processId);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/wf/process/test1")
	public ApiResult test1(@RequestBody ApproveBean bean/* String approver, String workitemId, String approveResult, String opinions */){
		processInstanceService.approve(bean.getApprover(), bean.getWorkitemId(), WFWorkitemApproveResult.valueOf(bean.getApproveResult()), bean.getOpinions());
		return ApiResultTemplate.success();
	}
	
	@GetMapping("/wf/process/test2")
	public ApiResult<ProcessDetailVO> test2(String processId){
		ProcessDetailVO vo = processQueryService.getProcessApprove(processId);
		return ApiResultTemplate.success(vo);
	}
	
	@GetMapping("/wf/process/test3")
	public ApiResult<ProcessListQueryReturnVO> test3(String templateId, String userId, String userName, String state, String approveState, int pageSize, int pageNum){
		ProcessListQuery query = new ProcessListQuery();
		query.setTemplateId(templateId);
		query.setPageNum(pageNum);
		query.setPageSize(pageSize);
		if(StringUtils.isNotBlank(approveState))
		query.setApproveState(WFProcessApproveState.valueOf(approveState));
		if(StringUtils.isNotBlank(state)) {
			query.setProcessState(WFProcessState.valueOf(state));
		}
		
		query.setPromoterId(userId);
		query.setPromoterName(userName);
		
		ProcessListQueryReturnVO vo = processQueryService.getProcessList(query);
		return ApiResultTemplate.success(vo);
	}
	
	/**
	 * 驳回工作流
	 * @param processId 流程实例ID
	 * @param rejector 驳回人
	 * @param opinions 驳回意见
	 * @return JSON格式
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/wf/process/reject")
	public ApiResult reject(String processId, String rejector, String opinions){
		processInstanceService.reject(processId, rejector, opinions);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 流程转交
	 * @param approveId 审批项ID
	 * @param transferApprover 转交的审批人ID
	 * @return JSON格式数据
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/wf/process/transfer")
	public ApiResult transfer(String approveId, String transferApprover){
		processInstanceService.transfer(approveId, transferApprover);
		return ApiResultTemplate.success();
	}
	
	/**
	 * 获取用户提交的流程
	 * @param promoter 用户ID
	 * @return JSON格式
	 */
	@GetMapping("/wf/process/my")
	public ApiResult<List<ProcessListVO>> getMyProcess(String promoter){
		List<ProcessListVO> list = processQueryService.myProcess(promoter);
		return ApiResultTemplate.success(list);
	}
	
	@GetMapping("/wf/process/myprocess")
	public ApiResult<ProcessListQueryReturnVO> myProcess(String promoterId, String templateId, String processState,
			Date submitDateBegin, Date submitDateEnd, Date finishDateBegin, Date finishDateEnd,
			Integer pageSize, Integer pageNum){
			ProcessListQuery query = new ProcessListQuery();
			query.setFinishDateBegin(finishDateBegin);
			query.setFinishDateEnd(finishDateEnd);
			query.setPageNum(pageNum);
			query.setPageSize(pageSize);
			query.setProcessState(WFProcessState.valueOf(processState));
			query.setPromoterId(promoterId);
			query.setSubmitDateBegin(submitDateBegin);
			query.setSubmitDateEnd(submitDateEnd);
			query.setTemplateId(templateId);
			
			ProcessListQueryReturnVO vo = processQueryService.myProcessList(query);
			
			return ApiResultTemplate.success(vo);
	}
	
	/**
	 * 获取用户的代办列表
	 * @param user 用户ID
	 * @return JSON格式
	 */
//	@GetMapping("/wf/process/db")
//	public ApiResult<List<ProcessListVO>> getUserProcess(String user){
//		List<ProcessListVO> list = processQueryService.myDB(user);
//		return ApiResultTemplate.success(list);
//	}
	
	/**
	 * 获取用户的代办列表
	 * @param user 用户ID
	 * @return JSON格式
	 */
	@GetMapping("/wf/process/db")
	public ApiResult<ProcessListQueryReturnVO> getUserProcess(String user){
		ProcessListQuery query = new ProcessListQuery();
		ProcessListQueryReturnVO vo = processQueryService.myApprovePendingProcessList(user, query);
		return ApiResultTemplate.success(vo);
	}
	
	
	/**
	 * 审批明细
	 * @param processId 流程实例ID
	 * @return JSON结构数据
	 */
	@GetMapping("/wf/process/approvedetail")
	public ApiResult<List<ApproveDetailVO>> approveDetail(String processId){
		List<ApproveDetailVO> list = processQueryService.approveDetail(processId);
		return ApiResultTemplate.success(list);
	}
	
	/**
	 * 更新流程参数
	 * @param workitemId 工作项ID
	 * @param paramStr 参数字符串
	 * @return 接口返回JSON格式
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/wf/process/updateparam")
	public ApiResult updateParam(String workitemId, String paramStr){
		Map<String, String> param = new HashMap<>();
		if(StringUtils.isNotBlank(paramStr)) {
			String[] str = paramStr.split("\\*!\\*");
			for(String p : str) {
				if(StringUtils.isNotBlank(p)) {
					String[] temp = p.split("\\*");
					param.put(temp[0], temp.length == 1 ? "" : temp[1]);
				}
			}
		}
		
		processInstanceService.updateParam(workitemId, param);
		return ApiResultTemplate.success();
	}
}

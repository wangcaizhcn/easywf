package com.easywf.wf.util.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.easywf.wf.process.constant.WFProcessApproveState;
import com.easywf.wf.process.service.ProcessQueryOldService;
import com.easywf.wf.process.vo.ApproveDetailVO;
import com.easywf.wf.util.context.ProcessContext;
import com.easywf.wf.util.service.WorkitemPassService;

@Service("TestWorkitemPassService")
public class TestWorkitemPassService implements WorkitemPassService {

	@Autowired
	private ProcessQueryOldService processQueryService;
	
	@Override
	public boolean pass(ProcessContext pc) {
		System.out.println("========自定义判断节点是否通过===========");
		List<ApproveDetailVO> vos = processQueryService.approveDetail(pc.getProcessId());
		// 如果高级设备主管（id=9913）通过，则不管其他主管是否通过，流程就通过
		for(ApproveDetailVO vo : vos) {
			if(vo.getApproverId().equals("9913")) {
				if(vo.getApproveResult().equals(WFProcessApproveState.PASS.toString())) {
					return true;
				}
			}
		}
		return false;
	}

}

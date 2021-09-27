package com.easywf.wf.transfer.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.easywf.wf.resources.entity.UserEntity;
import com.easywf.wf.resources.repository.UserJpaRepo;
import com.easywf.wf.transfer.entity.WFAutoTransferSettingEntity;
import com.easywf.wf.transfer.repository.WFAutoTransferSettingJpaRepo;
import com.easywf.wf.transfer.vo.WFAutoTransferSettingVO;

@Service
public class WFAutoTransferSettingService {

	@Autowired
	private WFAutoTransferSettingJpaRepo wfAutoTransferSettingJpaRepo;
	
	@Qualifier("wfUserJpaRepo")
	private UserJpaRepo userJpaRepo;
	
	public List<WFAutoTransferSettingVO> obtainList(String userId) {
		List<WFAutoTransferSettingEntity> list = wfAutoTransferSettingJpaRepo.findByApproverIdOrderByBeginTimeDesc(userId);
		if (CollectionUtils.isEmpty(list)) {
			return new LinkedList<>();
		} else {
			List<WFAutoTransferSettingVO> voList = new LinkedList<>();
			for (WFAutoTransferSettingEntity entity : list) {
				WFAutoTransferSettingVO vo = new WFAutoTransferSettingVO();
				vo.setId(entity.getId());
				vo.setApproverId(entity.getApproverId());
				vo.setBeginTime(entity.getBeginTime());
				vo.setEndTime(entity.getEndTime());
				vo.setTransferUserId(entity.getTransferUserId());
				vo.setUpdateTime(entity.getUpdateTime());
				
//				BeanUtil.copyBeanProperties(vo, entity);
				UserEntity user = userJpaRepo.findById(entity.getTransferUserId()).get();
				vo.setTransferUserName(user.getName());
				voList.add(vo);
			}
			return voList;
		}
	}
	
	public WFAutoTransferSettingVO obtainVO(String id) {
		WFAutoTransferSettingEntity entity = wfAutoTransferSettingJpaRepo.findById(id).get();
		if (entity == null) {
			return null;
		} else {
			WFAutoTransferSettingVO vo = new WFAutoTransferSettingVO();
//			BeanUtil.copyBeanProperties(vo, entity);
			vo.setId(entity.getId());
			vo.setApproverId(entity.getApproverId());
			vo.setBeginTime(entity.getBeginTime());
			vo.setEndTime(entity.getEndTime());
			vo.setTransferUserId(entity.getTransferUserId());
			vo.setUpdateTime(entity.getUpdateTime());
			UserEntity user = userJpaRepo.findById(entity.getTransferUserId()).get();
			vo.setTransferUserName(user.getName());
//			vo.setTransferUserName(userprofileStub.obtainUserName(entity.getTransferUserId(), ConfigUtil.obatainApiToken()));
			return vo;
		}
	}
	
	public WFAutoTransferSettingEntity obtain(String id) {
		return wfAutoTransferSettingJpaRepo.findById(id).get();
	}
	
	public void save(WFAutoTransferSettingEntity entity) {
		wfAutoTransferSettingJpaRepo.save(entity);
	}
	
	public void delete(List<String> idList) {
		List<WFAutoTransferSettingEntity> entityList = wfAutoTransferSettingJpaRepo.findByIdIn(idList);
		if (CollectionUtils.isNotEmpty(entityList)) {
			wfAutoTransferSettingJpaRepo.deleteAll(entityList);
		}
	}
}

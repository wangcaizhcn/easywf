package com.easywf.wf.transfer.vo;

import com.easywf.wf.transfer.entity.WFAutoTransferSettingEntity;

public class WFAutoTransferSettingVO extends WFAutoTransferSettingEntity {

	private String transferUserName;

	public String getTransferUserName() {
		return transferUserName;
	}

	public void setTransferUserName(String transferUserName) {
		this.transferUserName = transferUserName;
	}
}

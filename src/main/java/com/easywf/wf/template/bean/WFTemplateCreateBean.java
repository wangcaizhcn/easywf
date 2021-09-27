package com.easywf.wf.template.bean;

public class WFTemplateCreateBean {

	private String name;
	
	private String description;
	
	private boolean paap = false;
	
	private boolean saap = false;

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isPaap() {
		return paap;
	}

	public boolean isSaap() {
		return saap;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPaap(boolean paap) {
		this.paap = paap;
	}

	public void setSaap(boolean saap) {
		this.saap = saap;
	}
	
}

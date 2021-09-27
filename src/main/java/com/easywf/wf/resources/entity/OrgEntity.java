package com.easywf.wf.resources.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "easy_wf_org")
public class OrgEntity {

	@Id
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "parent_id")
	private String parentId;
	
	@Column(name = "level")
	private int level;
	
	@Column(name = "path")
	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}

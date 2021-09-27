package com.easywf.wf.template.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 流程节点实体对象
 * @author wangcai
 * @version 1.0
 */
@Entity
@Table(name = "easy_wf_template_node",
	indexes = {@Index(name = "idx_template_id",  columnList="template_id", unique = false)})
@org.hibernate.annotations.Table(appliesTo = "easy_wf_template_node", comment = "流程节点表")
public class WFTemplateNodeEntity {

	@Id
	@Column(columnDefinition = "varchar(36)")
	private String id;
	
	@Column(name = "template_id", columnDefinition = "varchar(36) COMMENT '所属流程模板ID'")
	private String templateId;
	
	@Column(name = "name", columnDefinition = "varchar(100) COMMENT '节点名称'")
	private String name;
	
	@Column(name = "display", columnDefinition = "varchar(100) COMMENT '节点显示名称，如果为空，则默认为节点名称'")
	private String display;
	
	@Column(name = "description", columnDefinition = "varchar(255) COMMENT '节点描述信息'")
	private String description;
	
	@Column(name = "type", columnDefinition = "varchar(20) COMMENT '节点类型，如开始节点、结束节点、自动节点等'")
	private String type;
	
	@Column(name = "child_template_id", columnDefinition = "varchar(36) COMMENT '流程节点为子流程时，指定子流程模板ID'")
	private String childTemplateId;
	
	@Column(name = "sync_variable", columnDefinition = "tinyint(1) default 0 COMMENT '流程节点为子流程时，当子流程结束后，是否同步子流程的变量到主流程，true同步，false不同步，默认为false'")
	private boolean syncVariable;
	
	@Column(name = "auto_service", columnDefinition = "varchar(255) COMMENT '流程节点为自动节点时，设置的实现类'")
	private String autoService;
	
	@Column(name = "level", columnDefinition = "int(11) COMMENT '流程节点的顺序层级，用于显示'")
	private int level;
	
	@Column(name = "p_x", columnDefinition = "double COMMENT '流程节点在图中的X坐标，用于显示'")
	private double x = 0D;
	
	@Column(name = "p_y", columnDefinition = "double COMMENT '流程节点在图中的Y坐标，用于显示'")
	private double y = 0D;
	
	public void position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 获取流程节点ID
	 * @return 流程节点ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * 设置流程节点ID
	 * @param id 流程节点ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取流程节点名称
	 * @return 流程节点名称
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 设置流程节点名称
	 * @param name 流程节点名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取显示名称
	 * @return 显示名称
	 */
	public String getDisplay() {
		return display;
	}

	/**
	 * 设置显示名称
	 * @param display 显示名称
	 */
	public void setDisplay(String display) {
		this.display = display;
	}

	/**
	 * 获取流程节点描述
	 * @return 流程节点描述
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 设置流程节点描述
	 * @param description 流程节点描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 获取所属流程模板的模板ID
	 * @return 所属流程模板的模板ID
	 */
	public String getTemplateId() {
		return templateId;
	}

	/**
	 * 设置所属流程模板的模板ID
	 * @param templateId 所属流程模板的模板ID
	 */
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	/**
	 * 获取流程节点类型
	 * @return 流程节点类型，参考{@link com.easywf.wf.template.constant.WFNodeType}
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置流程节点类型
	 * @param type 流程节点类型，参考{@link com.easywf.wf.template.constant.WFNodeType}
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 获取子流程节点的子流程模板ID
	 * @return 子流程模板ID
	 */
	public String getChildTemplateId() {
		return childTemplateId;
	}
	
	/**
	 * 设置子流程节点的子流程模板ID
	 * @param childTemplateId 子流程模板ID
	 */
	public void setChildTemplateId(String childTemplateId) {
		this.childTemplateId = childTemplateId;
	}
	
	/**
	 * 获取自动节点的实现类
	 * @return 自动节点的实现类
	 */
	public String getAutoService() {
		return autoService;
	}

	/**
	 * 设置自动节点的实现类
	 * @param autoService 自动节点的实现类
	 */
	public void setAutoService(String autoService) {
		this.autoService = autoService;
	}

	/**
	 * 获取是否同步子流程变量
	 * @return true:子流程结束后，变量同步到主流程<br>false:子流程结束后，不同步变量到主流程
	 */
	public boolean isSyncVariable() {
		return syncVariable;
	}

	/**
	 * 设置是否同步子流程变量
	 * @param syncVariable 是否同步子流程变量<br>
	 * true:子流程结束后，变量同步到主流程<br>false:子流程结束后，不同步变量到主流程
	 */
	public void setSyncVariable(boolean syncVariable) {
		this.syncVariable = syncVariable;
	}
	
	/**
	 * 获取节点的顺序
	 * @return 节点的顺序
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 设置节点的顺序
	 * @param level 节点的顺序
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}

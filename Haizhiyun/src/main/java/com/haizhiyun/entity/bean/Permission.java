package com.haizhiyun.entity.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 权限
 */
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String uuid;
	private String permissionName;
	private String url;
	//0是/不填否
	private String father;
	private Long pid;
	// 角色已有权限状态0有
	private String mark;
	private List<Permission> childrenPermission;
	private Timestamp createTime;
	private Timestamp updateTime;

	public Permission() {
		super();
	}

	public Permission(Long id) {
		super();
		this.id = id;
	}
	
	public Permission(String url) {
		super();
		this.url = url;
	}

	public Permission(String uuid, String permissionName, String father, String url, Long pid, String mark) {
		super();
		this.uuid = uuid;
		this.permissionName = permissionName;
		this.father = father;
		this.url = url;
		this.pid = pid;
		this.mark = mark;
	}

	public Permission(Long id, String uuid, String permissionName, String father, String url, Long pid, String mark,
			Timestamp createTime, Timestamp updateTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.permissionName = permissionName;
		this.father = father;
		this.url = url;
		this.pid = pid;
		this.mark = mark;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Permission(List<Permission> childrenPermission) {
		super();
		this.childrenPermission = childrenPermission;
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", uuid=" + uuid + ", permissionName=" + permissionName + ", url=" + url
				+ ", pid=" + pid + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public List<Permission> getChildrenPermission() {
		return childrenPermission;
	}

	public void setChildrenPermission(List<Permission> childrenPermission) {
		this.childrenPermission = childrenPermission;
	}

}

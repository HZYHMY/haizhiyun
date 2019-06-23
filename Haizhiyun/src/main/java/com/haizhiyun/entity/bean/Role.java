package com.haizhiyun.entity.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 
 * 角色
 *
 */
public class Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String uuid;
	private String roleName;
	private List<Permission> permissionList;
	private Timestamp createTime;
	private Timestamp updateTime;

	public Role() {
		super();
	}

	public Role(Long id) {
		super();
		this.id = id;
	}

	public Role(String roleName) {
		super();
		this.roleName = roleName;
	}

	public Role(Long id, String uuid, String roleName, Timestamp createTime, Timestamp updateTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.roleName = roleName;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Role(String uuid, String roleName) {
		super();
		this.uuid = uuid;
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", uuid=" + uuid + ", roleName=" + roleName + ", permissionList=" + permissionList
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public List<Permission> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	public Role(List<Permission> permissionList) {
		super();
		this.permissionList = permissionList;
	}


}

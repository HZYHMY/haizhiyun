package com.haizhiyun.entity.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class RolePermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String uuid;
	private Long roleId;
	private Long permissionId;

	public RolePermission() {
		super();
	}


	public RolePermission(Long id, String uuid, Long roleId, Long permissionId) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.roleId = roleId;
		this.permissionId = permissionId;
	}


	public RolePermission(String uuid, Long roleId, Long permissionId) {
		super();
		this.uuid = uuid;
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	public RolePermission(Long roleId, Long permissionId) {
		super();
		this.roleId = roleId;
		this.permissionId = permissionId;
	}



	@Override
	public String toString() {
		return "RolePermission [id=" + id + ", uuid=" + uuid + ", roleId=" + roleId + ", permissionId=" + permissionId
				+ "]";
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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Long permissionId) {
		this.permissionId = permissionId;
	}

}

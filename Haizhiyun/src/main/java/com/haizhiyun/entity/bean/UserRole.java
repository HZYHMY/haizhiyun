package com.haizhiyun.entity.bean;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户-角色
 */
public class UserRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String uuid;
	private Long userId;
	private Long roleId;

	public UserRole() {
		super();
	}

	

	public UserRole(Long id, String uuid, Long userId, Long roleId) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.userId = userId;
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", uuid=" + uuid + ", userId=" + userId + ", roleId=" + roleId + "]";
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}

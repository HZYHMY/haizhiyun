package com.haizhiyun.entity.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String uuid;
	private String userName;
	private String password;
	// 盐
	private String salt;
	private Role role;
	private Timestamp createTime;
	private Timestamp updateTime;

	public User() {
		super();
	}

	public User(String userName) {
		super();
		this.userName = userName;
	}

	public User(Long id, String uuid, String userName, String password, String salt, Timestamp createTime,
			Timestamp updateTime) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.userName = userName;
		this.password = password;
		this.salt = salt;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", salt=" + salt + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}

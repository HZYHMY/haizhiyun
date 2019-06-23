package com.haizhiyun.service;

import org.apache.ibatis.annotations.Param;

import com.haizhiyun.entity.bean.UserRole;

public interface UserRoleService {
	/**
	 * 创建用户角色
	 * 
	 * @param userRole
	 * @return int
	 */
	public int createUserRole(UserRole userRole);

	/**
	 * 删除用户角色
	 * 
	 * @param userRole
	 * @return int
	 */
	public int deleteUseRole(@Param("userId") Long userId);
}

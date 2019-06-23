package com.haizhiyun.dao;


import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.haizhiyun.entity.bean.UserRole;
@Transactional
public interface UserRoleMapper {
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

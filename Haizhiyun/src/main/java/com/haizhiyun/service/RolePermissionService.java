package com.haizhiyun.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haizhiyun.entity.bean.RolePermission;

public interface RolePermissionService {
	/**
	 * 创建角色权限
	 * 
	 * @param list
	 * @return int
	 */
	public int createRolePermission(@Param("list") List<RolePermission> list);

	/**
	 * 删除角色权限
	 * 
	 * @param uuids
	 * @return int
	 */
	public int deleteRolePermission(@Param("roleId") Long roleId);
}

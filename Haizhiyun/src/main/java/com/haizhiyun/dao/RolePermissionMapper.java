package com.haizhiyun.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.haizhiyun.entity.bean.RolePermission;
@Transactional
public interface RolePermissionMapper {
	/**
	 * 创建角色权限
	 * 
	 * @param list
	 * @return int
	 */
	public int createRolePermission(@Param("list")List<RolePermission> list);

	/**
	 * 删除角色权限
	 * 
	 * @param uuids
	 * @return int
	 */
	public int deleteRolePermission(@Param("roleId") Long roleId);
}

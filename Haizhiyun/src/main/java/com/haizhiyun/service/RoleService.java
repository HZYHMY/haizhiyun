package com.haizhiyun.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haizhiyun.entity.bean.Role;

public interface RoleService {
	/**
	 * 创建角色
	 * 
	 * @param role
	 * @return int
	 */
	public int createRole(Role role);

	/**
	 * 条件查询
	 * 
	 * @param role
	 * @return role
	 */
	public List<Role> selectRole(Role role);
	/**
	 * 查询某个角色
	 */
	public Role selectRoleById(Long roleId);

	/**
	 * 查询角色已有权限
	 * 
	 * @param roleId
	 * @return list
	 */
	public List<String> selectRolePermissionList(@Param("roleId") Long roleId);

	/**
	 * 修改角色
	 * 
	 * @param role
	 * @return int
	 */
	public int updateRole(Role role);

	/**
	 * 删除角色
	 * 
	 * @param uuids
	 * @return int
	 */
	public int deleteRole(@Param("roleId") Long roleId);
}

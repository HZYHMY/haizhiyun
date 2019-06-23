package com.haizhiyun.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haizhiyun.entity.bean.Permission;

public interface PermissionService {
	/**
	 * 创建权限
	 * 
	 * @param permission
	 * @return int
	 */
	public int createPermission(Permission permission);

	/**
	 * 修改权限
	 * 
	 * @param permission
	 * @return int
	 */
	public int updatePermission(Permission permission);

	/**
	 * 删除权限
	 * 
	 * @param uuids
	 * @return int
	 */
	public int deletePermission(@Param("uuid") String uuid);

	/**
	 * 查询权限
	 * 
	 * @param permission
	 * @return list
	 */
	public List<Permission> selectPermissionList(Permission permission);
	/**
	 * 查询所有父权限s
	 * 
	 * @param permission
	 * @return list
	 */
	public List<Permission> selecAllFathertPermissionList(Permission permission);
	/**
	 * 查询权限
	 * @param permission
	 * @return
	 */
	public Permission selectPermission(Permission permission);

	/**
	 * 树形结构
	 * 
	 * @param permission
	 * @return list
	 */
	public List<Permission> selectAllPermissionList(Long roleId);
}

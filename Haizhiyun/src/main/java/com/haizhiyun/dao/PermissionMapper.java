package com.haizhiyun.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import com.haizhiyun.entity.bean.Permission;
@Transactional
public interface PermissionMapper {
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
	
}

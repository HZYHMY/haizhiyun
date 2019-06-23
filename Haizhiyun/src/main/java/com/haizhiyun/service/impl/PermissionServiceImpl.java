package com.haizhiyun.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.haizhiyun.dao.PermissionMapper;
import com.haizhiyun.entity.bean.Permission;
import com.haizhiyun.entity.bean.Role;
import com.haizhiyun.service.PermissionService;
import com.haizhiyun.service.RoleService;
import com.haizhiyun.util.ConstantUtil;
@Service
public class PermissionServiceImpl implements PermissionService{

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionMapper permissionMapper;

	@Override
	public int createPermission(Permission permission) {
		int i = permissionMapper.createPermission(permission);
		return i;
	}

	@Override
	public int updatePermission(Permission permission) {
		int i = permissionMapper.updatePermission(permission);
		return i;
	}

	@Override
	public int deletePermission(String uuid) {
		int i = permissionMapper.deletePermission(uuid);
		return i;
	}

	@Override
	public List<Permission> selectPermissionList(Permission permission) {
		List<Permission> permissionlist = new ArrayList<>();
		//查询所有一级父权限
		Permission permissions = new Permission();
		permissions.setPid(0L);
		List<Permission> fathertPermissionList = permissionMapper.selectPermissionList(permissions);
		//遍历父权限
		if (!ObjectUtils.isEmpty(fathertPermissionList)&&!fathertPermissionList.equals(null)) {
			//添加所有父权限
			permissionlist.addAll(fathertPermissionList);
			//递归原理查询是否有子权限
			init(fathertPermissionList);
		}
		return permissionlist;
	}
	/*
	 * 添加子权限
	 */
	private void init(List<Permission> fathertPermissionList) {
		for (Permission newPermission : fathertPermissionList) {
			//根据主键查询子集
			Permission pe = new Permission();
			pe.setPid(newPermission.getId());
			List<Permission> chirderPermissionList = permissionMapper.selectPermissionList(pe);
			//添加子集
			if (!ObjectUtils.isEmpty(chirderPermissionList)) {
				newPermission.setChildrenPermission(chirderPermissionList);
				//抽取公共部门再遍历循环
				init(chirderPermissionList);
			}

		}
	}


	@Override
	public List<Permission> selectAllPermissionList(Long roleId) {
		//查询角色已有的权限
		List<String> rolePermissionList = roleService.selectRolePermissionList(roleId);
		List<Permission> permissionList = new ArrayList<>();
		//查询所有权限
		Permission permission = new Permission();
		permission.setPid(0L);
		List<Permission> fatherPermissionList = permissionMapper.selectPermissionList(permission);
		if (!ObjectUtils.isEmpty(fatherPermissionList)) {
			//遍历所有父权限看是否有角色权限
			rolePermissionInit(rolePermissionList, fatherPermissionList);
		}
		permissionList.addAll(fatherPermissionList);
		return permissionList;
	}

	private void rolePermissionInit(List<String> rolePermissionList, List<Permission> fatherPermissionList) {
		for (Permission fatherPermission : fatherPermissionList) {
			if (rolePermissionList.contains(fatherPermission.getUrl())) {
				fatherPermission.setMark(ConstantUtil.ZREO);
				//查询是否有子权限
				Permission permission = new Permission();
				permission.setPid(fatherPermission.getId());
				//查询所有子权限
				List<Permission> chirderPermissionList = permissionMapper.selectPermissionList(permission);
				if (!ObjectUtils.isEmpty(chirderPermissionList)) {
					fatherPermission.setChildrenPermission(chirderPermissionList);
					rolePermissionInit(rolePermissionList, chirderPermissionList);
				}
			}
		}
	}
	@Override
	public List<Permission> selecAllFathertPermissionList(Permission permission) {
		List<Permission> selectAllFatherPermissionList = permissionMapper.selectPermissionList(permission);
		return selectAllFatherPermissionList;
	}
	@Override
	public Permission selectPermission(Permission permission) {
		List<Permission> selectPermissionList = permissionMapper.selectPermissionList(permission);
		if (selectPermissionList.size()>0) {
			Permission newPermission = permissionMapper.selectPermissionList(permission).get(0);
			return newPermission;
		}
		return null;
	}
}

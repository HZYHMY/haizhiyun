package com.haizhiyun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.haizhiyun.dao.RoleMapper;
import com.haizhiyun.entity.bean.Permission;
import com.haizhiyun.entity.bean.Role;
import com.haizhiyun.service.PermissionService;
import com.haizhiyun.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionService permissionService;

	@Override
	public int createRole(Role role) {
		int i = roleMapper.createRole(role);
		return i;
	}

	@Override
	public List<Role> selectRole(Role role) {
		List<Role> roleList = roleMapper.selectRole(role);
		return roleList;
	}

	@Override
	public List<String> selectRolePermissionList(Long roleId) {
		List<String> permissionList = roleMapper.selectRolePermissionList(roleId);
		return permissionList;
	}

	@Override
	public int updateRole(Role role) {
		int i = roleMapper.updateRole(role);
		return i;
	}

	@Override
	public int deleteRole(Long roleId) {
		int i = roleMapper.deleteRole(roleId);
		return i;
	}

	@Override
	public Role selectRoleById(Long roleId) {
		Role role2 = new Role(roleId);
		Role role = roleMapper.selectRole(role2).get(0);
		//查询他的权限
		List<Permission> rolePermissionList = permissionService.selectAllPermissionList(roleId);
		if (!ObjectUtils.isEmpty(rolePermissionList)) {
			//添加他的权限供于前端展示并修改
			role.setPermissionList(rolePermissionList);
		}
		return role;
	}

}

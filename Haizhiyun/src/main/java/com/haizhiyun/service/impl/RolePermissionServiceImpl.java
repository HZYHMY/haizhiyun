package com.haizhiyun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haizhiyun.dao.RolePermissionMapper;
import com.haizhiyun.entity.bean.RolePermission;
import com.haizhiyun.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	@Override
	public int createRolePermission(List<RolePermission> list) {
		int i = rolePermissionMapper.createRolePermission(list);
		return i;
	}

	@Override
	public int deleteRolePermission(Long roleId) {
		int i = rolePermissionMapper.deleteRolePermission(roleId);
		return i;
	}

}

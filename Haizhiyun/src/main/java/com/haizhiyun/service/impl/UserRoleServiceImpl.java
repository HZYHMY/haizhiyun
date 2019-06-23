package com.haizhiyun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haizhiyun.dao.UserRoleMapper;
import com.haizhiyun.entity.bean.UserRole;
import com.haizhiyun.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Override
	public int createUserRole(UserRole userRole) {
		int i = userRoleMapper.createUserRole(userRole);

		return i;
	}

	@Override
	public int deleteUseRole(Long userId) {
		int i = userRoleMapper.deleteUseRole(userId);
		return i;
	}

}

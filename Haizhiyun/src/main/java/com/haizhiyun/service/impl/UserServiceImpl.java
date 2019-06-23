package com.haizhiyun.service.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haizhiyun.dao.UserMapper;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;

	@Override
	public int addUser(User user) {
		int i = userMapper.addUser(user);
		return i;
	}

	@Override
	public int updatePassword(User user) {
		int i =  userMapper.updatePassword(user);
		return i;
	}

	@Override
	public User getUser(User user) {
		User newUser = userMapper.getUser(user);
		return newUser;
	}

	@Override
	public int deleteUser( Long userId) {
		int i = userMapper.deleteUser(userId);
		return i;
	}

	@Override
	public List<String> selectUserPermissionList(Long userId) {
		List<String> userPermissionList = userMapper.selectUserPermissionList(userId);
		return userPermissionList;
	}

}

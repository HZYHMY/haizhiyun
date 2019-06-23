package com.haizhiyun.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.haizhiyun.entity.bean.User;

public interface UserService {
	/**
	 * 添加用户
	 * @param user
	 * @return 
	 */
	public int addUser(User user);
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	public int updatePassword(User user) ;
	/**
	 * 根据用户名查询
	 * @param userName
	 * @return
	 */
	public User getUser(User user);
	/**
	 * 注销
	 * @param uuid
	 * @return
	 */
	public int deleteUser(@Param("userId") Long userId);
	/**
	 * 查询用户已有权限
	 * @param userId
	 * @return list
	 */
	public List<String> selectUserPermissionList(@Param("userId") Long userId);
}

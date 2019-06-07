package com.haizhiyun.dao;

import org.springframework.transaction.annotation.Transactional;

import com.haizhiyun.entity.bean.User;
@Transactional
public interface UserMapper {
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
	public int deleteUser(String uuid);
	/**
	 * 根据用户名查询
	 */
	public User getUserByName(String userName);
	
}

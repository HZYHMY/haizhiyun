package com.haizhiyun.handle;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import com.haizhiyun.dao.UserMapper;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
import com.haizhiyun.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	
	private static Logger logger = LoggerFactory.getLogger(AuthRealm.class);

	@Autowired
	private UserService userService;
	@Autowired
	private UserMapper userMapper;
	/**
	 * 权限验证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.debug("---------------权限验证-----------------");
		System.err.println("--------------------------权限验证--------------------------");
		//获取用户
		String userName = (String) principals.getPrimaryPrincipal();
		if (ObjectUtils.isEmpty(userName)) {
			return null;
		}
		//查询用户
		User user = userService.getUser(new User(userName));
		//获取所有权限
		List<String> permissionList = userService.selectUserPermissionList(user.getId());
		for (String permission : permissionList) {
			System.err.println("---------------权限："+permission);
		}
		SimpleAuthorizationInfo newInfo = new SimpleAuthorizationInfo();
		//添加用户一有的权限
		newInfo.addStringPermissions(permissionList);
		return  newInfo;
	}
	/**
	 * 身份验证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.debug("---------------身份验证-----------------");
		UsernamePasswordToken newToken=(UsernamePasswordToken) token;
		//获取用户名
		String userName = newToken.getUsername();
		//查询用户是否存在
		User user = userMapper.getUserByName(userName);
		System.err.println("----------------------"+user);
		if (ObjectUtils.isEmpty(user)) {
			throw new MyException(ErrorCode.USER_EXIST, "用户不存在");
		}
		//认证
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user.getUserName(), user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());
		return info;
	}

}

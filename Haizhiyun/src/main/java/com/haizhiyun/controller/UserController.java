package com.haizhiyun.controller;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.haizhiyun.annotation.PassToken;
import com.haizhiyun.annotation.UserLoginToken;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.entity.bean.UserRole;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
import com.haizhiyun.service.UserRoleService;
import com.haizhiyun.service.UserService;
import com.haizhiyun.util.JWTUtil;
import com.haizhiyun.util.PasswordUtil;
import com.haizhiyun.util.RedisUtil;
import com.haizhiyun.util.SaltUtil;
import com.haizhiyun.util.UuidUtil;

import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * UserController swagger生成文档
 * http://localhost:8080/swagger-ui.html
 * @return
 */
@Api(description = "用户")
@RestController
@RequestMapping(value = "/user")
public class UserController {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	/**
	 * 创建用户
	 */
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RedisUtil redisUtil;

	@ApiOperation(value = "创建用户", notes = "创建用户")
	@UserLoginToken
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userName", required = true, paramType = "create", value = "用户名", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "create", value = "密码", dataType = "String"),
		@ApiImplicitParam(name = "roleId", required = true, paramType = "create", value = "角色", dataType = "Long")})
	@RequiresPermissions("/user/createUser")
	@PostMapping(value = "/createUser")
	public JSONObject createUser(@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "password", required = true) String password,@RequestParam(value = "roleId",required = true) Long roleId) {
		logger.debug("---------------创建用户-------------");
		User user = new User();
		user.setUserName(userName);
		//查询是否已注册
		User checkUser = userService.getUser(user);
		if (!ObjectUtils.isEmpty(checkUser)) {
			throw new MyException(ErrorCode.USER_INFO, "该用户名已存在");
		}
		user.setUuid(UuidUtil.getUuid());
		String salt = SaltUtil.getRandomString();
		user.setPassword(PasswordUtil.getPasswordAfterSaltBySHA256(password, salt));
		user.setSalt(salt);
		int i =  userService.addUser(user);
		if (i<0) {
			throw new MyException(ErrorCode.USER_INFO, "用户创建失败");
		}
		//选择角色
		User newuser = userService.getUser(user);
		UserRole userRole = new UserRole();
		userRole.setUuid(UuidUtil.getUuid());
		userRole.setUserId(newuser.getId());
		userRole.setRoleId(roleId);
		int j = userRoleService.createUserRole(userRole);
		if (j < 0) {
			throw new MyException(ErrorCode.USER_INFO, "用户角色创建失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("message", "SUCCESS");
		return userResult;

	}

	/**
	 * 删除用户
	 */
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@UserLoginToken
	@RequiresPermissions("/user/deleteUser")
	@PostMapping(value = "/deleteUser")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userId", required = true, paramType = "delete", value = "userId", dataType = "Long") })
	public JSONObject deleteUserByUuid(@RequestParam(name = "userId", required = true) Long userId) {
		logger.debug("---------------删除用户-------------");
        //删除已绑定角色
		int j = userRoleService.deleteUseRole(userId);
		if (j < 0) {
			throw	new MyException(ErrorCode.USER_INFO, "用户角色解绑失败");
		}
		int i = userService.deleteUser(userId);
		if (i<0) {
			throw	new MyException(ErrorCode.USER_INFO, "用户删除失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("message", "SUCCESS");
		return userResult;

	}

	/**
	 * 修改用户
	 * 
	 * @param uuid
	 * @return
	 */
	@ApiOperation(value = "修改密码", notes = "修改密码")
	@UserLoginToken
	@RequiresPermissions("/user/updateUser")
	@PostMapping(value = "/updateUser")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", required = true, paramType = "update", value = "uuid", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "update", value = "密码", dataType = "String") })
	public JSONObject updateUserByUuid(@RequestParam(name = "uuid", required = true) String uuid,
			@RequestParam(name = "password", required = true) String password) {
		logger.debug("---------------修改密码-------------");
		// 验证
		User user = new User();
		user.setUuid(uuid);
		User newUser = userService.getUser(user);
		if (ObjectUtils.isEmpty(newUser)) {
			throw new MyException(ErrorCode.USER_EXIST, "用户不存在");
		}
		user.setPassword(PasswordUtil.getPasswordAfterSaltBySHA256(password, newUser.getSalt()));
		int i = userService.updatePassword(newUser);
		if (i<0) {
			throw new MyException(ErrorCode.USER_INFO, "修改失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("message", "SUCCESS");
		return userResult;

	}
	/**
	 * 登录
	 * @param userName
	 * @param password
	 * @return
	 */
	@ApiOperation(value = "用户登录", notes = "用户登录")
	@PassToken
//	@UserLoginToken
//	@RequiresPermissions("/user/login")
	@PostMapping(value = "/login")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userName", required = true, paramType = "create", value = "用户名", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "create", value = "密码", dataType = "String") })
	public JSONObject logIn(@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "password", required = true) String password,HttpServletRequest request) {
		logger.debug("---------------用户登录-------------");
		//是否记住
		boolean rememberMe = Boolean.parseBoolean(request.getParameter("rememberMe"));
		User user = new User();
		user.setUserName(userName);
		User newUser = userService.getUser(user);
		user.setPassword(PasswordUtil.getPasswordAfterSaltBySHA256(newUser.getSalt(),password));
		User newUsers = userService.getUser(user);
		//用户名密码验证
		if (ObjectUtils.isEmpty(newUsers)) {
			throw new MyException(ErrorCode.USER_INFO, "用户名或密码错误");
		}
		try {
			//shiro设置身份
			Subject subject = SecurityUtils.getSubject();
			UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password);
			usernamePasswordToken.setRememberMe(rememberMe);
			//登录
			subject.login(usernamePasswordToken);
		} catch (AuthenticationException e) {
			throw new MyException(ErrorCode.USER_INFO, "登录认证失败");
		}
		
		//单点登录
		String token = JWTUtil.createJWT(newUsers);
		if (!ObjectUtils.isEmpty(token)) {
			request.getSession().setAttribute("uuid", newUsers.getUuid());
			//session声明周期
			request.getSession().setMaxInactiveInterval(24*60*60);
			//存入redis
			redisUtil.save(newUsers.getUuid(), token);
			redisUtil.setSaveTime(newUsers.getUuid(), 15*60);
		}
		JSONObject resultUser = new JSONObject();
		resultUser.put("token", token);
		resultUser.put("message", "SUCCESS");
		return resultUser;
	}
	@ApiOperation(value = "登出" ,notes = "登出")
	@UserLoginToken
	@RequiresPermissions("/user/logout")
	@PostMapping(value = "/logout")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", required = true, paramType = "delete", value = "uuid", dataType = "String") })
	public JSONObject logout(@RequestParam(name = "uuid", required = true) String uuid) {
		logger.debug("---------------用户登出-------------");
		//单点登录退出
		String token = (String) redisUtil.get(uuid);
		if (ObjectUtils.isEmpty(token)) {
			redisUtil.delete(uuid);
			throw new MyException(ErrorCode.USER_EXIST, "token过时，请重新登录");
		}
		Claims body = JWTUtil.validateJWT(token);
		//清除用户信息
		body.clear();
		//验证
		logger.debug("---------------------------------用户退出--------------------------------------"+body);
		//清除缓存
		redisUtil.delete(uuid);
		//shiro退出
		SecurityUtils.getSubject().logout();
		JSONObject resultUser = new JSONObject();
		resultUser.put("message", "成功退出");
		return resultUser;
	}

}

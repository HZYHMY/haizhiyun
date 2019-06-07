package com.haizhiyun.controller;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.haizhiyun.annotation.PassToken;
import com.haizhiyun.annotation.UserLoginToken;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
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
	private RedisUtil redisUtil;

	@ApiOperation(value = "创建用户", notes = "创建用户")
	@UserLoginToken
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userName", required = true, paramType = "create", value = "用户名", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "create", value = "密码", dataType = "String") })
	@PostMapping(value = "/createUser")
	public JSONObject createUser(@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "password", required = true) String password) {
		logger.debug("---------------创建用户-------------");
		User user = new User();
		user.setUuid(UuidUtil.getUuid());
		user.setUserName(userName);
		String salt = SaltUtil.getRandomString();
		user.setPassword(PasswordUtil.getPassword(password, salt));
		user.setSalt(salt);
		int i =  userService.addUser(user);
		if (i<0) {
			new MyException(ErrorCode.USER_INFO, "用户创建失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("msg", "SUCCESS");
		return userResult;

	}

	/**
	 * 删除用户
	 */
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@UserLoginToken
	@PostMapping(value = "/deleteUser")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", required = true, paramType = "delete", value = "uuid", dataType = "String") })
	public JSONObject deleteUserByUuid(@RequestParam(name = "uuid", required = true) String uuid) {
		//验证
		User user = new User();
		user.setUuid(uuid);
		User newUser = userService.getUser(user);
		if (ObjectUtils.isEmpty(newUser)) {
			throw new MyException(ErrorCode.USER_INFO, "用户不存在");
		}

		int i = userService.deleteUser(uuid);
		if (i<0) {
			new MyException(ErrorCode.USER_INFO, "用户删除失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("msg", "SUCCESS");
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
	@PostMapping(value = "/updateUser")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", required = true, paramType = "update", value = "uuid", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "update", value = "密码", dataType = "String") })
	public JSONObject updateUserByUuid(@RequestParam(name = "uuid", required = true) String uuid,
			@RequestParam(name = "password", required = true) String password) {
		// 验证
		User user = new User();
		user.setUuid(uuid);
		User newUser = userService.getUser(user);
		if (ObjectUtils.isEmpty(newUser)) {
			throw new MyException(ErrorCode.USER_EXIST, "用户不存在");
		}
		user.setPassword(PasswordUtil.getPassword(password, newUser.getSalt()));
		int i = userService.updatePassword(newUser);
		if (i<0) {
			throw new MyException(ErrorCode.USER_INFO, "修改失败");
		}
		JSONObject userResult = new JSONObject();
		userResult.put("msg", "SUCCESS");
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
	@UserLoginToken
	@PostMapping(value = "/login")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "userName", required = true, paramType = "create", value = "用户名", dataType = "String"),
		@ApiImplicitParam(name = "password", required = true, paramType = "create", value = "密码", dataType = "String") })
	public JSONObject logIn(@RequestParam(name = "userName", required = true) String userName,
			@RequestParam(name = "password", required = true) String password,HttpServletRequest request) {
		System.err.println(userName+"------------"+password);
		User user = new User();
		user.setUserName(userName);
		User newUser = userService.getUser(user);
		System.err.println("user"+user.toString());
		user.setPassword(PasswordUtil.getPassword(password, newUser.getSalt()));
		User newUsers = userService.getUser(user);
		System.err.println("user:=============="+newUsers);
		//用户名密码验证
		if (ObjectUtils.isEmpty(newUsers)) {
			throw new MyException(ErrorCode.USER_INFO, "用户名或密码错误");
		}
		String token = JWTUtil.createJWT(newUsers);
		if (!ObjectUtils.isEmpty(token)) {
			request.getSession().setAttribute("uuid", newUsers.getUuid());
			//
			request.getSession().setMaxInactiveInterval(10*60);
			//存入redis
			redisUtil.save(newUsers.getUuid(), token);
			redisUtil.setSaveTime(newUsers.getUuid(), 10*60);
		}
		JSONObject resultUser = new JSONObject();
		resultUser.put("token", token);
		resultUser.put("msg", "SUCCESS");
		return resultUser;
	}
	@ApiOperation(value = "登出" ,notes = "登出")
	@UserLoginToken
	@PostMapping(value = "logout")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "uuid", required = true, paramType = "delete", value = "uuid", dataType = "String") })
	public JSONObject logout(@RequestParam(name = "uuid", required = true) String uuid) {
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
		JSONObject resultUser = new JSONObject();
		resultUser.put("msg", "成功退出");
		return resultUser;
	}

}

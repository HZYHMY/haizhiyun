package com.haizhiyun.handle;

import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.haizhiyun.annotation.PassToken;
import com.haizhiyun.annotation.UserLoginToken;
import com.haizhiyun.controller.UserController;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;
import com.haizhiyun.service.UserService;
import com.haizhiyun.util.JWTUtil;
import com.haizhiyun.util.RedisUtil;

/**
 *拦截器
 */
public class AuthenticationInterceptor implements HandlerInterceptor{
	
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);
   /**
    * 预处理
    */
	@Autowired
	private UserService userService;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("------------------------进入拦截器----------------------------------");
		//获取token
//		String token = request.getHeader("Authorization");
		String uuid = (String) request.getSession().getAttribute("uuid");
	    logger.debug("-------------------拦截器==========================uuid:"+uuid);
	    String token = "";
	    if (!ObjectUtils.isEmpty(uuid)) {
	    	 token = (String) redisUtil.get(uuid);
	    }
	    logger.debug("-------------------拦截器==========================token:"+token);
		//如果不是映射的方法直接通过
		if (!(handler instanceof HandlerMethod) ) {
			return true;
		}
		HandlerMethod handleMethod=(HandlerMethod) handler;
		//获取方法
		Method method = handleMethod.getMethod();
		//检查是否有passtoken的注释，有则跳过
		if (method.isAnnotationPresent(PassToken.class)) {
			//获取注释
			PassToken passToken = method.getAnnotation(PassToken.class);
		    if (passToken.required()) {
				return true;
			}
		}
		//检查有没有需要用户权限的注释，有则跳过认证
		if (method.isAnnotationPresent(UserLoginToken.class)) {
			UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
		     if (userLoginToken.required()) {
		    	 //执行认证
				if (token == null) {
					logger.debug("--------------token过时，请重新登录---------------");
					throw new MyException(ErrorCode.USER_EXIST, "token过期，请重新登录");
				}
				//token存在，获取token中的userId
				String newUuid = "";
				try {
					//解析token
					newUuid = JWTUtil.validateJWT(token).getId();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//通过uuid获取用户
				User user = new User();
				user.setUuid(newUuid);
				User newUser = userService.getUser(user);
				if (ObjectUtils.isEmpty(newUser)) {
					logger.debug("--------------用户不存在---------------");
					throw new MyException(ErrorCode.USER_EXIST, "用户不存在");
				}
				//通过秘钥加密算法生成验证令牌签名工具
				//用户存在验证token(核实)
				JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(JWTUtil.JWT_SECERT)).build();
			    try {
			    	logger.debug("--------------用户存在验证token(核实)----------------");
			    	//使用生成token配置选项对给定令牌执行验证。
			    	DecodedJWT verify = jwtVerifier.verify(token);
			    	logger.debug("--------------用户存在验证token(对比)----------------"+verify.getToken());
			    	if (!verify.getToken().equals(token)) {
			    		throw new MyException(ErrorCode.USER_EXIST, "token不真实");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//描述，token通过
		          return true;
		     }
		}
		//标记通过
		return true;
	}
	
}

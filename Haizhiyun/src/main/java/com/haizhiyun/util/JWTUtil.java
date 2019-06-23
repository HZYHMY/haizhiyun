package com.haizhiyun.util;

import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.haizhiyun.controller.UserController;
import com.haizhiyun.entity.bean.User;
import com.haizhiyun.exception.ErrorCode;
import com.haizhiyun.exception.MyException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
public class JWTUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JWTUtil.class);
	//服务器key，秘钥
	public static final String JWT_SECERT = "test_jwt_secert";
	//转json
	private static final ObjectMapper objectMapper = new  ObjectMapper();
	/*
	 * 获取key
	 */
	public static SecretKey getKey() {
		logger.debug("--------------------------生成秘钥key-----------------------------");
		try {
			//转字节
			byte[] encodeKey = JWT_SECERT.getBytes("UTF-8");
			//加密后秘钥
			SecretKeySpec key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 *  签发JWT，创建token的方法
	 * @param id jwt唯一身份标识，主要用来作为一次性token，从而货币供给
	 * @param ISS JWT签发者
	 * @param subject jwt所面向的用户，payload（负载）中记录的public claims，档当环境中就就是用户的登录名
	 * @param ttlMillis 有效时间s
	 * @return token token是一次性的的，是为一个用户的有效登录周期准备的一个token。用户退出或超时，token失效
	 * 注意：Payload(负载)与claims（私密）二选一
	 */
	public static String createJWT(User user) {
		logger.debug("-------------------生成token-----------------------");
		//加密算法
		SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
		//当前日期
		long currentTimeMillis = System.currentTimeMillis();
		Date date = new Date(currentTimeMillis);
		//获取私钥
		SecretKey secretKey=getKey();
		//秘密，用于储存用户信息
		HashMap<String, Object> claims = new HashMap<>();
		claims.put(user.getUserName(), user.getUserName());
		claims.put(user.getPassword(), user.getPassword());
		//创建JET的构造器，指定加密算法，生成token
		JwtBuilder builder = Jwts.builder()
				//唯一标识
				.setId(user.getUuid())
				//签发者
				.setIssuer(user.getUserName())
				.setSubject(user.getUserName())
				//生效时间
				.setIssuedAt(date)
				//设定私钥和算法
				.signWith(hs256, secretKey)
				//Claim储存用户账户密码
				.addClaims(claims)
				//唯一标识
				.setAudience(user.getUuid());
		//设置有效时间
		if (currentTimeMillis >= 0) {
			long expMillis=currentTimeMillis + 60*1000*15;
			Date newDate = new Date(expMillis);
			builder.setExpiration(newDate);
		}
		return builder.compact();

	}
	/**
	 * 解析token
	 * @param token
	 * @return
	 */
	public static Claims validateJWT(String token) {
		logger.debug("-------------------解析token------------------------");
		//获取私钥
		SecretKey secreKey=getKey();
		Claims body = null;
		try {
			body = Jwts.parser()
					.setSigningKey(secreKey)
					//解析tokan
					.parseClaimsJws(token)
					.getBody();//getBody获取的就是token中记录的payload数据。就是payload中保存的所有的claims。
		}catch (ExpiredJwtException e) {
			logger.debug("------------------token失效，请重新登录--------------------------");
			throw new MyException(ErrorCode.USER_EXIST, "请重新登录");
		} catch (Exception e) {
			throw new MyException(ErrorCode.USER_INFO, "校验失败");
		}
		return body;

	}

}

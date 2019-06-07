package com.haizhiyun.handle;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * 日志处理
 */
@Aspect
@Component
public class LogAspect {

	private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

	// 切点
	@Pointcut(value = "execution(* com.haizhiyun.controller.*.*(..))")
	public void log() {

	}

	// 前置处理
	@Before(value = "log()")
	public void beforeLog(JoinPoint joinPoint) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// url
		StringBuffer url = request.getRequestURL();
		// 请求方法
		String method = request.getMethod();
		logger.info("url:" + url + "----------" + "method:" + hashCode());
	}

	// 后置处理
	@AfterReturning(value = "log()", returning = "obj")
	public void afterReturningLog(JoinPoint joinPoint, Object obj) {
		String result = obj.toString();
		logger.info("result:" + result);
	}
}

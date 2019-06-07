package com.haizhiyun.handle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *拦截器配置拦截规则
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer{
    /**
     * 拦截登记
     */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
		registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");
	}
	@Bean
	public AuthenticationInterceptor  authenticationInterceptor() {
		return new AuthenticationInterceptor();
	}

}

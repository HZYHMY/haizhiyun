package com.haizhiyun.handle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring容器会检测容器中的所有Bean，如果发现某个Bean实现了ApplicationContextAware接口，Spring容器会在创建该Bean之后，自动调用该Bean的setApplicationContextAware()方法，调用该方法时，会将容器本身作为参数传给该方法——该方法中的实现部分将Spring传入的参数（容器本身）赋给该类对象的applicationContext实例变量，因此接下来可以通过该applicationContext实例变量来访问容器本身。
 */
@Component
public class SpringContextHanddle implements ApplicationContextAware {

	private static ApplicationContext context;

	/*
	 * context不能为空
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (context == null) {
			context = applicationContext;
		}

	}

	/*
	 * 获取context
	 */
	public static ApplicationContext getApplicationContext() {
		return context;

	}

	/*
	 * 从静态变量applicationContext中得到Bean, 自动转型为所赋值对象的类型.
	 */
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);

	}

	/*
	 * 从静态变量applicationContext中得到Bean, 自动转型为所赋值对象的类型
	 */
	public static <T> T getBean(Class<T> requiredType) {

		return getApplicationContext().getBean(requiredType);

	}
	public static void newInstance(Object obj){
		AutowireCapableBeanFactory beanFactory = getApplicationContext().getAutowireCapableBeanFactory();
		beanFactory.autowireBean(obj);
	}

}

package com.haizhiyun.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	// 存值
	public void save(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	// 取值
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);

	}
	//删除
	public Boolean delete(String key) {
		Boolean flag = redisTemplate.delete(key);
		return flag;
	}
	//生命周期
	public void setSaveTime(String key,long timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	};
}

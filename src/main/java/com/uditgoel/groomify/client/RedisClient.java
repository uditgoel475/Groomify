package com.uditgoel.groomify.client;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisClient")
public class RedisClient extends RedisTemplate<String, Object> {

	public RedisClient(RedisConnectionFactory connectionFactory) {
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
	}

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	public void setValue(final String key, final String value, long timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, timeout, unit);
	}

	public String getValue(final String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}

	public void deleteValue(final String key) {
		redisTemplate.delete(key);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}
}

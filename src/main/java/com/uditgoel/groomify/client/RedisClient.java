package com.uditgoel.groomify.client;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisClient")
public class RedisClient extends RedisTemplate<String, Object> {

	private final RedisTemplate<String, Object> redisTemplate;

	public RedisClient(RedisConnectionFactory connectionFactory,
			@Qualifier("redisTemplate") RedisTemplate<String, Object> redisTemplate) {
		setConnectionFactory(connectionFactory);
		afterPropertiesSet();
		this.redisTemplate = redisTemplate;
	}

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

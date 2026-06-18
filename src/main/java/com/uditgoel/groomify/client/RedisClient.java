package com.uditgoel.groomify.client;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

	@Override
	public Boolean delete(final String key) {
		return redisTemplate.delete(key);
	}

	public void sadd(final String key, final String value) {
		redisTemplate.opsForSet().add(key, value);
	}

	public Set<String> smembers(final String key) {
		Set<Object> raw = redisTemplate.opsForSet().members(key);
		if (raw == null) {
			return Collections.emptySet();
		}
		return raw.stream().map(o -> (String) o).collect(Collectors.toSet());
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}
}

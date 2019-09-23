package com.niit.lookatme.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisNode.NodeType;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ComponentScan("com.niit.lookatme")
public class RedisConfiguration {

	@Value("${datasource.redis.master.host}")
	private String redisMasterHost;

	@Value("${datasource.redis.master.port}")
	private int redisMasterPort;

	@Value("${datasource.redis.master.pass}")
	private String redisMasterPass;

	@Value("${datasource.redis.slave.host}")
	private String redisSlaveHost;

	@Value("${datasource.redis.slave.port}")
	private int redisSlavePort;

	@Value("${datasource.redis.slave.pass}")
	private String redisSlavePass;

	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {

		/*RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
		redisClusterConfiguration.setPassword(RedisPassword.of(redisMasterPass));

		RedisNode redisNodeMaster = RedisNode.newRedisNode().withId("master")
				.listeningAt(redisMasterHost, redisMasterPort).promotedAs(NodeType.MASTER).build();
		RedisNode redisNodeSlave = RedisNode.newRedisNode().withId("slave").slaveOf("master")
				.listeningAt(redisSlaveHost, redisSlavePort).promotedAs(NodeType.SLAVE).build();

		redisClusterConfiguration.addClusterNode(redisNodeMaster);
		redisClusterConfiguration.addClusterNode(redisNodeSlave);

		return new LettuceConnectionFactory(redisClusterConfiguration);*/
		
		return new LettuceConnectionFactory(redisMasterHost, redisMasterPort);

	}

	@Bean(name = "redisTemplate")
	public <V> RedisTemplate<String, V> redisTemplate() {
		RedisTemplate<String, V> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}

}
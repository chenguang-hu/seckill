package com.example.seckill.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    public RedisSerializer<Object> jackson2JsonRedisSerializer(){
        Jackson2JsonRedisSerializer<Object> serializer=new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);
        return serializer;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 生成默认配置, 通过configuration对象可对缓存进行自定义
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                // 设置key为String序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer()))
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(20L));

        RedisCacheManager cacheManager=RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(configuration)
                .transactionAware()
                .build();
        return cacheManager;
    }

    // 修改默认的RedisTemplate持久化方式
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        // 设置key值序列化方式为String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置value值序列化方式为JSON
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        // 设置hash的key值序列化方式为String
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 设置hash的value值序列化方式为String
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        return redisTemplate;
    }
}

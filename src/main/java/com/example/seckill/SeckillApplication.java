package com.example.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching  // 开启声明式缓存, 利用注解控制缓存的读写
@EnableScheduling   // 启用任务调度功能
public class SeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }

}

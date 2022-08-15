package com.example.binance.config;

import com.example.binance.dto.CandleItem;
import com.example.binance.dto.ConsumeInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, List<CandleItem>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<CandleItem>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}

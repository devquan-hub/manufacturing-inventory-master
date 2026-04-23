package com.manufacturing.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置类
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用String序列化器
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // 使用JSON序列化器
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Redis缓存查询Lua脚本
     * 先检查布隆过滤器，再查询缓存
     * KEYS[1]: 布隆过滤器key
     * KEYS[2]: 缓存key
     * 返回: 1=缓存命中, 0=缓存未命中或布隆过滤器不存在
     */
    @Bean
    public DefaultRedisScript<Long> cacheCheckScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(
            "local bloomKey = KEYS[1] " +
            "local cacheKey = KEYS[2] " +
            "local cacheValue = redis.call('GET', cacheKey) " +
            "if cacheValue then return 1 else return 0 end"
        );
        script.setResultType(Long.class);
        return script;
    }

    /**
     * 缓存写入Lua脚本（带TTL）
     * KEYS[1]: 缓存key
     * ARGV[1]: 缓存值
     * ARGV[2]: TTL（秒）
     */
    @Bean
    public DefaultRedisScript<String> cacheSetScript() {
        DefaultRedisScript<String> script = new DefaultRedisScript<>();
        script.setScriptText(
            "redis.call('SET', KEYS[1], ARGV[1], 'EX', ARGV[2]) " +
            "return 'OK'"
        );
        script.setResultType(String.class);
        return script;
    }

    /**
     * 缓存删除Lua脚本（同时删除缓存和布隆过滤器）
     * KEYS[1]: 缓存key前缀（会删除所有匹配的前缀）
     * KEYS[2]: 布隆过滤器key
     * KEYS[3]: 要从布隆过滤器删除的值
     */
    @Bean
    public DefaultRedisScript<Long> cacheDeleteScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(
            "local keys = redis.call('KEYS', KEYS[1] .. '*') " +
            "for i, key in ipairs(keys) do redis.call('DEL', key) end " +
            "if KEYS[3] ~= '' then redis.call('DEL', KEYS[2]) end " +
            "return #keys"
        );
        script.setResultType(Long.class);
        return script;
    }
}

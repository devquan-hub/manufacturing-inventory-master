package com.manufacturing.inventory.cache;

import com.google.common.hash.Funnels;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * 布隆过滤器工具类
 * 用于快速判断数据是否存在，避免无效的数据库查询
 */
@Component
public class BloomFilterUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public BloomFilterUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 添加元素到布隆过滤器
     * @param bloomKey 布隆过滤器key
     * @param value 要添加的值
     */
    public void add(String bloomKey, String value) {
        // 使用Redis的SETBIT实现布隆过滤器
        long hash = hash(value);
        redisTemplate.opsForValue().setBit(bloomKey, hash % 1024 * 8, true);
    }

    /**
     * 批量添加元素到布隆过滤器
     * @param bloomKey 布隆过滤器key
     * @param values 要添加的值列表
     */
    public void addAll(String bloomKey, java.util.Collection<String> values) {
        for (String value : values) {
            add(bloomKey, value);
        }
    }

    /**
     * 检查元素是否可能存在于布隆过滤器中
     * @param bloomKey 布隆过滤器key
     * @param value 要检查的值
     * @return true=可能存在, false=一定不存在
     */
    public boolean mightExist(String bloomKey, String value) {
        long hash = hash(value);
        Boolean bit = redisTemplate.opsForValue().getBit(bloomKey, hash % 1024 * 8);
        return bit != null && bit;
    }

    /**
     * 删除布隆过滤器
     * @param bloomKey 布隆过滤器key
     */
    public void delete(String bloomKey) {
        redisTemplate.delete(bloomKey);
    }

    /**
     * 计算字符串的哈希值
     */
    private long hash(String value) {
        // 使用MurmurHash算法
        int h = 0;
        for (int i = 0; i < value.length(); i++) {
            h = 31 * h + value.charAt(i);
        }
        return h;
    }
}
package com.manufacturing.inventory.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 缓存服务工具类
 * 提供通用的缓存查询、写入、删除操作
 * 结合布隆过滤器实现高效缓存
 * 支持空值缓存，防止缓存穿透
 * 当Redis不可用时，自动穿透到数据库，不影响业务
 */
@Component
public class CacheService {

    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    /** 空值标记对象 - 用于标记缓存的空值 */
    private static final Object NULL_VALUE_MARKER = new Object();

    private final RedisTemplate<String, Object> redisTemplate;
    private final BloomFilterUtil bloomFilterUtil;
    private final ObjectMapper objectMapper;

    public CacheService(RedisTemplate<String, Object> redisTemplate,
                        BloomFilterUtil bloomFilterUtil,
                        ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.bloomFilterUtil = bloomFilterUtil;
        this.objectMapper = objectMapper;
    }

    /**
     * 检查Redis是否可用
     */
    private boolean isRedisAvailable() {
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            return true;
        } catch (Exception e) {
            log.warn("Redis不可用，将穿透到数据库查询: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 带布隆过滤器的缓存查询
     * 当Redis不可用时，直接返回null，穿透到数据库
     * @param cachePrefix 缓存key前缀
     * @param cacheKey 完整缓存key
     * @param bloomKey 布隆过滤器key
     * @param typeReference 结果类型引用
     * @return 缓存结果，null表示缓存未命中或空值
     */
    public <T> T getWithBloom(String cachePrefix, String cacheKey, String bloomKey,
                               TypeReference<T> typeReference) {
        // Redis不可用时穿透数据库
        if (!isRedisAvailable()) {
            return null;
        }

        try {
            // 1. 先检查缓存是否存在
            Object cached = redisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                // 检查是否是空值标记
                if (cached == NULL_VALUE_MARKER) {
                    log.debug("缓存命中空值: {}", cacheKey);
                    return null; // 返回null表示数据库中也不存在
                }
                log.debug("缓存命中: {}", cacheKey);
                return convertValue(cached, typeReference);
            }

            // 2. 检查布隆过滤器（可选，快速判断）
            if (bloomKey != null && !bloomKey.isEmpty()) {
                String bloomId = extractBloomId(cacheKey, cachePrefix);
                if (bloomId != null && !bloomFilterUtil.mightExist(bloomKey, bloomId)) {
                    log.debug("布隆过滤器判断不存在: {}", bloomKey);
                    return null;
                }
            }

            return null;
        } catch (Exception e) {
            log.warn("缓存读取异常，穿透到数据库: {}, error: {}", cacheKey, e.getMessage());
            return null;
        }
    }

    /**
     * 写入缓存（数据或空值）
     * 当Redis不可用时，静默失败，不影响业务
     * @param cacheKey 缓存key
     * @param value 要缓存的值（可为null，缓存空值标记）
     * @param ttlSeconds 过期时间（秒）
     */
    public void set(String cacheKey, Object value, long ttlSeconds) {
        if (!isRedisAvailable()) {
            return;
        }

        try {
            // 如果value为null，缓存空值标记
            Object cacheValue = (value == null) ? NULL_VALUE_MARKER : value;
            redisTemplate.opsForValue().set(cacheKey, cacheValue, ttlSeconds, TimeUnit.SECONDS);
            log.debug("缓存写入成功: {}, TTL: {}s, 空值: {}", cacheKey, ttlSeconds, value == null);
        } catch (Exception e) {
            log.warn("缓存写入失败: {}, error: {}", cacheKey, e.getMessage());
        }
    }

    /**
     * 写入缓存（使用默认TTL）
     * @param cacheKey 缓存key
     * @param value 要缓存的值（可为null，缓存空值标记）
     */
    public void set(String cacheKey, Object value) {
        set(cacheKey, value, CacheNames.TTL_PRODUCT_WAREHOUSE);
    }

    /**
     * 设置空值缓存（防止缓存穿透）
     * @param cacheKey 缓存key
     * @param ttlSeconds 过期时间（秒），默认30秒
     */
    public void setNullValue(String cacheKey) {
        set(cacheKey, NULL_VALUE_MARKER, CacheNames.TTL_NULL_VALUE);
    }

    /**
     * 设置空值缓存（防止缓存穿透）
     * @param cacheKey 缓存key
     * @param ttlSeconds 过期时间（秒）
     */
    public void setNullValue(String cacheKey, long ttlSeconds) {
        set(cacheKey, NULL_VALUE_MARKER, ttlSeconds);
    }

    /**
     * 删除缓存
     * 当Redis不可用时，静默失败
     * @param cacheKey 缓存key
     */
    public void delete(String cacheKey) {
        if (!isRedisAvailable()) {
            return;
        }

        try {
            redisTemplate.delete(cacheKey);
            log.debug("缓存删除成功: {}", cacheKey);
        } catch (Exception e) {
            log.warn("缓存删除失败: {}, error: {}", cacheKey, e.getMessage());
        }
    }

    /**
     * 删除指定前缀的所有缓存
     * @param prefix 前缀
     */
    public void deleteByPrefix(String prefix) {
        if (!isRedisAvailable()) {
            return;
        }

        try {
            var keys = redisTemplate.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
                log.debug("批量删除缓存成功: {}, 数量: {}", prefix, keys.size());
            }
        } catch (Exception e) {
            log.warn("批量删除缓存失败: {}, error: {}", prefix, e.getMessage());
        }
    }

    /**
     * 添加数据到布隆过滤器
     * @param bloomKey 布隆过滤器key
     * @param id 数据ID
     */
    public void addToBloom(String bloomKey, String id) {
        if (!isRedisAvailable()) {
            return;
        }

        try {
            bloomFilterUtil.add(bloomKey, id);
            log.debug("布隆过滤器添加成功: {}, id: {}", bloomKey, id);
        } catch (Exception e) {
            log.warn("布隆过滤器添加失败: {}, error: {}", bloomKey, e.getMessage());
        }
    }

    /**
     * 检查布隆过滤器
     * @param bloomKey 布隆过滤器key
     * @param id 数据ID
     * @return true=可能存在, false=一定不存在
     */
    public boolean checkBloom(String bloomKey, String id) {
        if (!isRedisAvailable()) {
            return true;
        }

        try {
            return bloomFilterUtil.mightExist(bloomKey, id);
        } catch (Exception e) {
            log.warn("布隆过滤器检查失败: {}, error: {}", bloomKey, e.getMessage());
            return true;
        }
    }

    /**
     * 删除布隆过滤器
     * @param bloomKey 布隆过滤器key
     */
    public void deleteBloom(String bloomKey) {
        if (!isRedisAvailable()) {
            return;
        }

        try {
            bloomFilterUtil.delete(bloomKey);
        } catch (Exception e) {
            log.warn("布隆过滤器删除失败: {}, error: {}", bloomKey, e.getMessage());
        }
    }

    /**
     * 生成缓存key
     * @param prefix 前缀
     * @param params 查询参数
     * @return 缓存key
     */
    public String generateKey(String prefix, Object... params) {
        StringBuilder sb = new StringBuilder(prefix);
        for (Object param : params) {
            sb.append(":").append(param != null ? param.toString() : "null");
        }
        return sb.toString();
    }

    /**
     * 类型转换
     */
    @SuppressWarnings("unchecked")
    private <T> T convertValue(Object value, TypeReference<T> typeReference) {
        try {
            if (value instanceof String) {
                return objectMapper.readValue((String) value, typeReference);
            }
            return objectMapper.convertValue(value, typeReference);
        } catch (Exception e) {
            log.warn("缓存数据转换失败: {}", e.getMessage());
            return (T) value;
        }
    }

    /**
     * 从缓存key中提取用于布隆过滤器的标识
     */
    private String extractBloomId(String cacheKey, String prefix) {
        if (cacheKey.startsWith(prefix)) {
            String remainder = cacheKey.substring(prefix.length());
            int nextColon = remainder.indexOf(':');
            if (nextColon > 0) {
                return remainder.substring(1, nextColon);
            }
            return remainder.length() > 1 ? remainder.substring(1) : null;
        }
        return null;
    }
}

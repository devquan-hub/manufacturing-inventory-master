package com.manufacturing.inventory.cache;

/**
 * 缓存名称常量定义
 * 定义各模块的缓存key前缀
 */
public final class CacheNames {

    private CacheNames() {}

    // ==================== 项目隔离前缀 ====================
    /** 项目前缀 - 防止多项目key冲突 */
    public static final String PROJECT_PREFIX = "manufacturing-inventory:";

    // ==================== 商品模块 ====================
    /** 商品列表缓存 */
    public static final String PRODUCT_PAGE = PROJECT_PREFIX + "cache:product:page:";
    /** 商品详情缓存 */
    public static final String PRODUCT_DETAIL = PROJECT_PREFIX + "cache:product:detail:";
    /** 商品布隆过滤器 */
    public static final String PRODUCT_BLOOM = PROJECT_PREFIX + "bloom:product";

    // ==================== 仓库模块 ====================
    /** 仓库列表缓存 */
    public static final String WAREHOUSE_PAGE = PROJECT_PREFIX + "cache:warehouse:page:";
    /** 仓库详情缓存 */
    public static final String WAREHOUSE_DETAIL = PROJECT_PREFIX + "cache:warehouse:detail:";
    /** 仓库布隆过滤器 */
    public static final String WAREHOUSE_BLOOM = PROJECT_PREFIX + "bloom:warehouse";

    // ==================== 销售模块 ====================
    /** 销售订单列表缓存 */
    public static final String SALES_ORDER_PAGE = PROJECT_PREFIX + "cache:sales:order:page:";
    /** 销售订单详情缓存 */
    public static final String SALES_ORDER_DETAIL = PROJECT_PREFIX + "cache:sales:order:detail:";
    /** 销售出库列表缓存 */
    public static final String SALES_OUTBOUND_PAGE = PROJECT_PREFIX + "cache:sales:outbound:page:";
    /** 销售订单布隆过滤器 */
    public static final String SALES_ORDER_BLOOM = PROJECT_PREFIX + "bloom:sales:order";

    // ==================== 生产模块 ====================
    /** 生产计划列表缓存 */
    public static final String PRODUCTION_PLAN_PAGE = PROJECT_PREFIX + "cache:production:plan:page:";
    /** 生产计划详情缓存 */
    public static final String PRODUCTION_PLAN_DETAIL = PROJECT_PREFIX + "cache:production:plan:detail:";
    /** 生产计划布隆过滤器 */
    public static final String PRODUCTION_PLAN_BLOOM = PROJECT_PREFIX + "bloom:production:plan";

    // ==================== 库存模块 ====================
    /** 库存查询列表缓存 */
    public static final String INVENTORY_PAGE = PROJECT_PREFIX + "cache:inventory:page:";
    /** 库存记录列表缓存 */
    public static final String INVENTORY_RECORD_PAGE = PROJECT_PREFIX + "cache:inventory:record:page:";
    /** 库存布隆过滤器 */
    public static final String INVENTORY_BLOOM = PROJECT_PREFIX + "bloom:inventory";

    // ==================== TTL设置（秒）====================
    /** 空值缓存TTL（秒）- 防止缓存穿透，设置30秒 */
    public static final long TTL_NULL_VALUE = 30;

    /** 商品/仓库缓存TTL（秒）- 基础数据变化不频繁，设置10分钟 */
    public static final long TTL_PRODUCT_WAREHOUSE = 600;

    /** 销售订单缓存TTL（秒）- 变化较频繁，设置5分钟 */
    public static final long TTL_SALES = 300;

    /** 生产计划缓存TTL（秒）- 变化中等，设置8分钟 */
    public static final long TTL_PRODUCTION = 480;

    /** 库存缓存TTL（秒）- 变化非常频繁，设置2分钟 */
    public static final long TTL_INVENTORY = 120;

    /** 缓存key生成前缀 */
    public static final String CACHE_KEY_PREFIX = PROJECT_PREFIX + "cache:";
}

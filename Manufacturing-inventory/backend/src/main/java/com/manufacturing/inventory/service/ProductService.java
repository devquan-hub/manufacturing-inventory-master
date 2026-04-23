package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.manufacturing.inventory.cache.CacheNames;
import com.manufacturing.inventory.cache.CacheService;
import com.manufacturing.inventory.common.BusinessException;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.BasProduct;
import com.manufacturing.inventory.mapper.BasProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {

    private final BasProductMapper productMapper;
    private final CacheService cacheService;

    public ProductService(BasProductMapper productMapper, CacheService cacheService) {
        this.productMapper = productMapper;
        this.cacheService = cacheService;
    }

    public PageResult<BasProduct> page(Integer pageNum, Integer pageSize, String keyword, Integer category) {
        // 生成缓存key
        String cacheKey = cacheService.generateKey(
                CacheNames.PRODUCT_PAGE,
                pageNum,
                pageSize,
                keyword != null ? keyword : "",
                category != null ? category : ""
        );

        // 尝试从缓存获取
        PageResult<BasProduct> cached = cacheService.getWithBloom(
            CacheNames.PRODUCT_PAGE, cacheKey, CacheNames.PRODUCT_BLOOM,
            new TypeReference<PageResult<BasProduct>>() {}
        );
        if (cached != null) {
            return cached;
        }

        // 查询数据库
        Page<BasProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BasProduct> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like(BasProduct::getName, keyword)
                   .or()
                   .like(BasProduct::getCode, keyword);
        }
        if (category != null) {
            wrapper.eq(BasProduct::getCategory, category);
        }
        wrapper.orderByDesc(BasProduct::getCreateTime);

        IPage<BasProduct> result = productMapper.selectPage(page, wrapper);
        PageResult<BasProduct> pageResult = PageUtil.of(result);

        // 只缓存有数据的分页结果，空结果不缓存（避免大量无效key）
        if (result.getTotal() > 0) {
            cacheService.set(cacheKey, pageResult, CacheNames.TTL_PRODUCT_WAREHOUSE);
        }

        return pageResult;
    }

    public BasProduct getById(Long id) {
        String cacheKey = CacheNames.PRODUCT_DETAIL + id;

        // 尝试从缓存获取
        BasProduct cached = cacheService.getWithBloom(
            CacheNames.PRODUCT_DETAIL, cacheKey, CacheNames.PRODUCT_BLOOM,
            new TypeReference<BasProduct>() {}
        );
        if (cached != null) {
            return cached;
        }

        BasProduct product = productMapper.selectById(id);
        if (product == null) {
            // 缓存空值标记，防止缓存穿透（30秒后过期）
            cacheService.setNullValue(cacheKey);
            throw new BusinessException("商品不存在");
        }

        // 写入缓存
        cacheService.set(cacheKey, product, CacheNames.TTL_PRODUCT_WAREHOUSE);

        return product;
    }

    public void save(BasProduct product) {
        productMapper.insert(product);

        // 删除列表缓存
        cacheService.deleteByPrefix(CacheNames.PRODUCT_PAGE);

        // 添加到布隆过滤器
        cacheService.addToBloom(CacheNames.PRODUCT_BLOOM, String.valueOf(product.getId()));
    }

    public void update(BasProduct product) {
        productMapper.updateById(product);

        // 删除相关缓存
        cacheService.delete(CacheNames.PRODUCT_DETAIL + product.getId());
        cacheService.deleteByPrefix(CacheNames.PRODUCT_PAGE);
    }

    public void delete(Long id) {
        productMapper.deleteById(id);

        // 删除相关缓存
        cacheService.delete(CacheNames.PRODUCT_DETAIL + id);
        cacheService.deleteByPrefix(CacheNames.PRODUCT_PAGE);
    }
}

package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.manufacturing.inventory.cache.CacheNames;
import com.manufacturing.inventory.cache.CacheService;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.BasWarehouse;
import com.manufacturing.inventory.mapper.BasWarehouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private final BasWarehouseMapper warehouseMapper;
    private final CacheService cacheService;

    public WarehouseService(BasWarehouseMapper warehouseMapper, CacheService cacheService) {
        this.warehouseMapper = warehouseMapper;
        this.cacheService = cacheService;
    }

    public PageResult<BasWarehouse> page(Integer pageNum, Integer pageSize, String keyword) {
        // 生成缓存key
        String cacheKey = cacheService.generateKey(
            CacheNames.WAREHOUSE_PAGE, pageNum, pageSize,
            keyword != null ? keyword : ""
        );

        // 尝试从缓存获取
        PageResult<BasWarehouse> cached = cacheService.getWithBloom(
            CacheNames.WAREHOUSE_PAGE, cacheKey, CacheNames.WAREHOUSE_BLOOM,
            new TypeReference<PageResult<BasWarehouse>>() {}
        );
        if (cached != null) {
            return cached;
        }

        // 查询数据库
        Page<BasWarehouse> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BasWarehouse> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(BasWarehouse::getName, keyword).or().like(BasWarehouse::getCode, keyword);
        }
        wrapper.orderByDesc(BasWarehouse::getCreateTime);
        IPage<BasWarehouse> result = warehouseMapper.selectPage(page, wrapper);
        PageResult<BasWarehouse> pageResult = PageUtil.of(result);

        // 写入缓存（仓库数据变化不频繁，设置60分钟）
        cacheService.set(cacheKey, pageResult, CacheNames.TTL_PRODUCT_WAREHOUSE);

        return pageResult;
    }

    public List<BasWarehouse> list() {
        String cacheKey = CacheNames.WAREHOUSE_PAGE + "list";

        List<BasWarehouse> cached = cacheService.getWithBloom(
            CacheNames.WAREHOUSE_PAGE, cacheKey, CacheNames.WAREHOUSE_BLOOM,
            new TypeReference<List<BasWarehouse>>() {}
        );
        if (cached != null) {
            return cached;
        }

        List<BasWarehouse> result = warehouseMapper.selectList(
            new LambdaQueryWrapper<BasWarehouse>().orderByAsc(BasWarehouse::getCode)
        );

        // 写入缓存（仓库数据变化不频繁，设置60分钟）
        cacheService.set(cacheKey, result, CacheNames.TTL_PRODUCT_WAREHOUSE);

        return result;
    }

    public void save(BasWarehouse warehouse) {
        warehouseMapper.insert(warehouse);

        // 删除列表缓存
        cacheService.deleteByPrefix(CacheNames.WAREHOUSE_PAGE);

        // 添加到布隆过滤器
        cacheService.addToBloom(CacheNames.WAREHOUSE_BLOOM, String.valueOf(warehouse.getId()));
    }

    public void update(BasWarehouse warehouse) {
        warehouseMapper.updateById(warehouse);

        // 删除相关缓存
        cacheService.delete(CacheNames.WAREHOUSE_DETAIL + warehouse.getId());
        cacheService.deleteByPrefix(CacheNames.WAREHOUSE_PAGE);
    }

    public void delete(Long id) {
        warehouseMapper.deleteById(id);

        // 删除相关缓存
        cacheService.delete(CacheNames.WAREHOUSE_DETAIL + id);
        cacheService.deleteByPrefix(CacheNames.WAREHOUSE_PAGE);
    }
}
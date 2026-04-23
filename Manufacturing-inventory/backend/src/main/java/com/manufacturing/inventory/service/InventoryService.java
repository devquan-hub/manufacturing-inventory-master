package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.manufacturing.inventory.cache.CacheNames;
import com.manufacturing.inventory.cache.CacheService;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.StcInventory;
import com.manufacturing.inventory.entity.StcRecord;
import com.manufacturing.inventory.mapper.StcInventoryMapper;
import com.manufacturing.inventory.mapper.StcRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final StcInventoryMapper inventoryMapper;
    private final StcRecordMapper recordMapper;
    private final CacheService cacheService;

    public InventoryService(StcInventoryMapper inventoryMapper,
                           StcRecordMapper recordMapper,
                           CacheService cacheService) {
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
        this.cacheService = cacheService;
    }

    public PageResult<StcInventory> inventoryPage(Integer pageNum, Integer pageSize, Long warehouseId, Long productId) {
        // 生成缓存key
        String cacheKey = cacheService.generateKey(
            CacheNames.INVENTORY_PAGE, pageNum, pageSize,
            warehouseId != null ? warehouseId : "",
            productId != null ? productId : ""
        );

        // 尝试从缓存获取
        PageResult<StcInventory> cached = cacheService.getWithBloom(
            CacheNames.INVENTORY_PAGE, cacheKey, CacheNames.INVENTORY_BLOOM,
            new TypeReference<PageResult<StcInventory>>() {}
        );
        if (cached != null) {
            return cached;
        }

        // 查询数据库
        Page<StcInventory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<StcInventory> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(StcInventory::getWarehouseId, warehouseId);
        }
        if (productId != null) {
            wrapper.eq(StcInventory::getProductId, productId);
        }
        wrapper.orderByDesc(StcInventory::getCreateTime);
        IPage<StcInventory> result = inventoryMapper.selectPage(page, wrapper);
                PageResult<StcInventory> pageResult = PageUtil.of(result);

        // 写入缓存（库存数据变化非常频繁，设置10分钟）
        cacheService.set(cacheKey, pageResult, CacheNames.TTL_INVENTORY);

        return pageResult;
    }

    public List<StcInventory> inventoryList(Long warehouseId) {
        String cacheKey = CacheNames.INVENTORY_PAGE + "list:" + (warehouseId != null ? warehouseId : "all");

        List<StcInventory> cached = cacheService.getWithBloom(
            CacheNames.INVENTORY_PAGE, cacheKey, CacheNames.INVENTORY_BLOOM,
            new TypeReference<List<StcInventory>>() {}
        );
        if (cached != null) {
            return cached;
        }

        LambdaQueryWrapper<StcInventory> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(StcInventory::getWarehouseId, warehouseId);
        }
        wrapper.gt(StcInventory::getQuantity, 0);
        List<StcInventory> result = inventoryMapper.selectList(wrapper);

        // 写入缓存（空列表也缓存）
        cacheService.set(cacheKey, result, CacheNames.TTL_INVENTORY);

        return result;
    }

    public PageResult<StcRecord> recordPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId, Integer type) {
        // 生成缓存key
        String cacheKey = cacheService.generateKey(
            CacheNames.INVENTORY_RECORD_PAGE, pageNum, pageSize,
            productId != null ? productId : "",
            warehouseId != null ? warehouseId : "",
            type != null ? type : ""
        );

        // 尝试从缓存获取
        PageResult<StcRecord> cached = cacheService.getWithBloom(
            CacheNames.INVENTORY_RECORD_PAGE, cacheKey, CacheNames.INVENTORY_BLOOM,
            new TypeReference<PageResult<StcRecord>>() {}
        );
        if (cached != null) {
            return cached;
        }

        // 查询数据库
        Page<StcRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<StcRecord> wrapper = new LambdaQueryWrapper<>();
        if (productId != null) {
            wrapper.eq(StcRecord::getProductId, productId);
        }
        if (warehouseId != null) {
            wrapper.eq(StcRecord::getWarehouseId, warehouseId);
        }
        if (type != null) {
            wrapper.eq(StcRecord::getType, type);
        }
        wrapper.orderByDesc(StcRecord::getOperateTime);
        IPage<StcRecord> result = recordMapper.selectPage(page, wrapper);
        PageResult<StcRecord> pageResult = PageUtil.of(result);

        // 写入缓存（空列表也缓存）
        cacheService.set(cacheKey, pageResult, CacheNames.TTL_INVENTORY);

        return pageResult;
    }

    public StcInventory getInventory(Long productId, Long warehouseId) {
        String cacheKey = CacheNames.PROJECT_PREFIX + "cache:inventory:detail:" + productId + ":" + warehouseId;

        StcInventory cached = cacheService.getWithBloom(
            CacheNames.PROJECT_PREFIX + "cache:inventory:detail:", cacheKey, CacheNames.INVENTORY_BLOOM,
            new TypeReference<StcInventory>() {}
        );
        if (cached != null) {
            return cached;
        }

        StcInventory result = inventoryMapper.selectOne(
            new LambdaQueryWrapper<StcInventory>()
                .eq(StcInventory::getProductId, productId)
                .eq(StcInventory::getWarehouseId, warehouseId)
        );

        if (result != null) {
            // 写入缓存
            cacheService.set(cacheKey, result, CacheNames.TTL_INVENTORY);
        } else {
            // 缓存空值标记，防止缓存穿透
            cacheService.setNullValue(cacheKey);
        }

        return result;
    }
}
package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    public InventoryService(StcInventoryMapper inventoryMapper, StcRecordMapper recordMapper) {
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
    }

    public PageResult<StcInventory> inventoryPage(Integer pageNum, Integer pageSize, Long warehouseId, Long productId) {
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
        return PageUtil.of(result);
    }

    public List<StcInventory> inventoryList(Long warehouseId) {
        LambdaQueryWrapper<StcInventory> wrapper = new LambdaQueryWrapper<>();
        if (warehouseId != null) {
            wrapper.eq(StcInventory::getWarehouseId, warehouseId);
        }
        wrapper.gt(StcInventory::getQuantity, 0);
        return inventoryMapper.selectList(wrapper);
    }

    public PageResult<StcRecord> recordPage(Integer pageNum, Integer pageSize, Long productId, Long warehouseId, Integer type) {
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
        return PageUtil.of(result);
    }

    public StcInventory getInventory(Long productId, Long warehouseId) {
        return inventoryMapper.selectOne(
            new LambdaQueryWrapper<StcInventory>()
                .eq(StcInventory::getProductId, productId)
                .eq(StcInventory::getWarehouseId, warehouseId)
        );
    }
}

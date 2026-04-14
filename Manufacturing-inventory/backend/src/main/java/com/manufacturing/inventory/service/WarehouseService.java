package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.BasWarehouse;
import com.manufacturing.inventory.mapper.BasWarehouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {

    private final BasWarehouseMapper warehouseMapper;

    public WarehouseService(BasWarehouseMapper warehouseMapper) {
        this.warehouseMapper = warehouseMapper;
    }

    public PageResult<BasWarehouse> page(Integer pageNum, Integer pageSize, String keyword) {
        Page<BasWarehouse> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BasWarehouse> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(BasWarehouse::getName, keyword).or().like(BasWarehouse::getCode, keyword);
        }
        wrapper.orderByDesc(BasWarehouse::getCreateTime);
        IPage<BasWarehouse> result = warehouseMapper.selectPage(page, wrapper);
        return PageUtil.of(result);
    }

    public List<BasWarehouse> list() {
        return warehouseMapper.selectList(new LambdaQueryWrapper<BasWarehouse>().orderByAsc(BasWarehouse::getCode));
    }

    public void save(BasWarehouse warehouse) {
        warehouseMapper.insert(warehouse);
    }

    public void update(BasWarehouse warehouse) {
        warehouseMapper.updateById(warehouse);
    }

    public void delete(Long id) {
        warehouseMapper.deleteById(id);
    }
}

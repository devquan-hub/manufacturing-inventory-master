package com.manufacturing.inventory.controller;

import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.BasWarehouse;
import com.manufacturing.inventory.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/page")
    public Result<PageResult<BasWarehouse>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(warehouseService.page(pageNum, pageSize, keyword));
    }

    @GetMapping("/list")
    public Result<List<BasWarehouse>> list() {
        return Result.success(warehouseService.list());
    }

    @PostMapping
    public Result<Void> save(@RequestBody BasWarehouse warehouse) {
        warehouseService.save(warehouse);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody BasWarehouse warehouse) {
        warehouseService.update(warehouse);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        warehouseService.delete(id);
        return Result.success();
    }
}

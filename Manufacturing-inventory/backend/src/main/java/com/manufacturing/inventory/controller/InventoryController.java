package com.manufacturing.inventory.controller;

import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.StcInventory;
import com.manufacturing.inventory.entity.StcRecord;
import com.manufacturing.inventory.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/page")
    public Result<PageResult<StcInventory>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long productId) {
        return Result.success(inventoryService.inventoryPage(pageNum, pageSize, warehouseId, productId));
    }

    @GetMapping("/list")
    public Result<List<StcInventory>> list(@RequestParam(required = false) Long warehouseId) {
        return Result.success(inventoryService.inventoryList(warehouseId));
    }

    @GetMapping("/record/page")
    public Result<PageResult<StcRecord>> recordPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Integer type) {
        return Result.success(inventoryService.recordPage(pageNum, pageSize, productId, warehouseId, type));
    }
}

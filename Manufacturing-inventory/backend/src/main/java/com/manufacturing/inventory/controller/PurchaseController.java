package com.manufacturing.inventory.controller;

import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.PurInbound;
import com.manufacturing.inventory.entity.PurOrder;
import com.manufacturing.inventory.service.PurchaseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/order/page")
    public Result<PageResult<PurOrder>> orderPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(purchaseService.orderPage(pageNum, pageSize, keyword, status));
    }

    @PostMapping("/order")
    public Result<Void> createOrder(@RequestBody PurOrder order) {
        purchaseService.createOrder(order);
        return Result.success();
    }

    @PutMapping("/order/audit/{id}")
    public Result<Void> auditOrder(@PathVariable Long id) {
        purchaseService.auditOrder(id);
        return Result.success();
    }

    @PostMapping("/inbound")
    public Result<Void> createInbound(@RequestBody PurInbound inbound) {
        purchaseService.createInbound(inbound);
        return Result.success();
    }
}

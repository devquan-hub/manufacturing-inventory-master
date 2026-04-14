package com.manufacturing.inventory.controller;

import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.SalOrder;
import com.manufacturing.inventory.entity.SalOutbound;
import com.manufacturing.inventory.service.SalesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/order/page")
    public Result<PageResult<SalOrder>> orderPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(salesService.orderPage(pageNum, pageSize, keyword, status));
    }

    @PostMapping("/order")
    public Result<Void> createOrder(@RequestBody SalOrder order) {
        salesService.createOrder(order);
        return Result.success();
    }

    @PutMapping("/order/audit/{id}")
    public Result<Void> auditOrder(@PathVariable Long id) {
        salesService.auditOrder(id);
        return Result.success();
    }

    @PostMapping("/outbound")
    public Result<Void> createOutbound(@RequestBody SalOutbound outbound) {
        salesService.createOutbound(outbound);
        return Result.success();
    }
}

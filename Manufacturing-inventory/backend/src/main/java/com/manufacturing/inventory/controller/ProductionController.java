package com.manufacturing.inventory.controller;

import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.ProMaterialReq;
import com.manufacturing.inventory.entity.ProPlan;
import com.manufacturing.inventory.entity.ProProduct;
import com.manufacturing.inventory.entity.ProQuality;
import com.manufacturing.inventory.service.ProductionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/plan/page")
    public Result<PageResult<ProPlan>> planPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.success(productionService.planPage(pageNum, pageSize, keyword, status));
    }

    @PostMapping("/plan")
    public Result<Void> createPlan(@RequestBody ProPlan plan) {
        productionService.createPlan(plan);
        return Result.success();
    }

    @PutMapping("/plan/start/{id}")
    public Result<Void> startPlan(@PathVariable Long id) {
        productionService.startPlan(id);
        return Result.success();
    }

    @PutMapping("/plan/complete/{id}")
    public Result<Void> completePlan(@PathVariable Long id) {
        productionService.completePlan(id);
        return Result.success();
    }

    @PostMapping("/material-req")
    public Result<Void> saveMaterialReq(@RequestBody ProMaterialReq req) {
        productionService.saveMaterialReq(req);
        return Result.success();
    }

    @PostMapping("/report")
    public Result<Void> reportProduct(@RequestBody ProProduct product) {
        productionService.reportProduct(product);
        return Result.success();
    }

    @PostMapping("/quality")
    public Result<Void> qualityCheck(@RequestBody ProQuality quality) {
        productionService.qualityCheck(quality);
        return Result.success();
    }
}

package com.manufacturing.inventory.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manufacturing.inventory.common.BusinessException;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.*;
import com.manufacturing.inventory.mapper.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductionService {

    private final ProPlanMapper planMapper;
    private final ProMaterialReqMapper materialReqMapper;
    private final ProProductMapper productMapper;
    private final ProQualityMapper qualityMapper;

    public ProductionService(ProPlanMapper planMapper, ProMaterialReqMapper materialReqMapper,
                             ProProductMapper productMapper, ProQualityMapper qualityMapper) {
        this.planMapper = planMapper;
        this.materialReqMapper = materialReqMapper;
        this.productMapper = productMapper;
        this.qualityMapper = qualityMapper;
    }

    public PageResult<ProPlan> planPage(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        Page<ProPlan> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ProPlan> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(ProPlan::getPlanNo, keyword).or().like(ProPlan::getProductName, keyword);
        }
        if (status != null) {
            wrapper.eq(ProPlan::getStatus, status);
        }
        wrapper.orderByDesc(ProPlan::getCreateTime);
        IPage<ProPlan> result = planMapper.selectPage(page, wrapper);
        return PageUtil.of(result);
    }

    public void createPlan(ProPlan plan) {
        plan.setPlanNo("PP" + IdUtil.getSnowflakeNextIdStr().substring(10));
        plan.setStatus(1);
        planMapper.insert(plan);
    }

    public void startPlan(Long id) {
        ProPlan plan = planMapper.selectById(id);
        if (plan == null) throw new BusinessException("计划不存在");
        if (plan.getStatus() != 1) throw new BusinessException("只能启动待生产计划");
        plan.setStatus(2);
        planMapper.updateById(plan);
    }

    @Transactional
    public void completePlan(Long id) {
        ProPlan plan = planMapper.selectById(id);
        if (plan == null) throw new BusinessException("计划不存在");
        if (plan.getStatus() != 2) throw new BusinessException("只能完成生产中计划");
        plan.setStatus(3);
        plan.setCompletedQuantity(plan.getPlanQuantity());
        planMapper.updateById(plan);
    }

    public void saveMaterialReq(ProMaterialReq req) {
        req.setReqNo("PM" + IdUtil.getSnowflakeNextIdStr().substring(10));
        req.setStatus(1);
        materialReqMapper.insert(req);
    }

    @Transactional
    public void reportProduct(ProProduct product) {
        product.setReportNo("RP" + IdUtil.getSnowflakeNextIdStr().substring(10));
        product.setStatus(1);
        product.setReportTime(java.time.LocalDateTime.now());
        productMapper.insert(product);
    }

    public void qualityCheck(ProQuality quality) {
        quality.setQualityNo("QC" + IdUtil.getSnowflakeNextIdStr().substring(10));
        quality.setStatus(1);
        qualityMapper.insert(quality);
        
        // 更新报工单状态
        ProProduct proProduct = productMapper.selectById(quality.getProductId());
        if (proProduct != null) {
            proProduct.setStatus(2);
            productMapper.updateById(proProduct);
        }
    }
}

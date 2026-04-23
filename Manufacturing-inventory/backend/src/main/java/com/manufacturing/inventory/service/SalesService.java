package com.manufacturing.inventory.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.manufacturing.inventory.cache.CacheNames;
import com.manufacturing.inventory.cache.CacheService;
import com.manufacturing.inventory.common.BusinessException;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.SalOrder;
import com.manufacturing.inventory.entity.SalOutbound;
import com.manufacturing.inventory.entity.SalOutboundDetail;
import com.manufacturing.inventory.entity.StcInventory;
import com.manufacturing.inventory.entity.StcRecord;
import com.manufacturing.inventory.mapper.SalOrderMapper;
import com.manufacturing.inventory.mapper.SalOutboundDetailMapper;
import com.manufacturing.inventory.mapper.SalOutboundMapper;
import com.manufacturing.inventory.mapper.StcInventoryMapper;
import com.manufacturing.inventory.mapper.StcRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SalesService {

    private final SalOrderMapper orderMapper;
    private final SalOutboundMapper outboundMapper;
    private final SalOutboundDetailMapper outboundDetailMapper;
    private final StcInventoryMapper inventoryMapper;
    private final StcRecordMapper recordMapper;
    private final CacheService cacheService;

    public SalesService(SalOrderMapper orderMapper,
                        SalOutboundMapper outboundMapper,
                        SalOutboundDetailMapper outboundDetailMapper,
                        StcInventoryMapper inventoryMapper,
                        StcRecordMapper recordMapper,
                        CacheService cacheService) {
        this.orderMapper = orderMapper;
        this.outboundMapper = outboundMapper;
        this.outboundDetailMapper = outboundDetailMapper;
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
        this.cacheService = cacheService;
    }

    public PageResult<SalOrder> orderPage(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        // 生成缓存key
        String cacheKey = cacheService.generateKey(
            CacheNames.SALES_ORDER_PAGE, pageNum, pageSize,
            keyword != null ? keyword : "", status != null ? status : ""
        );

        // 尝试从缓存获取
        PageResult<SalOrder> cached = cacheService.getWithBloom(
            CacheNames.SALES_ORDER_PAGE, cacheKey, CacheNames.SALES_ORDER_BLOOM,
            new TypeReference<PageResult<SalOrder>>() {}
        );
        if (cached != null) {
            return cached;
        }

        // 查询数据库
        Page<SalOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SalOrder> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SalOrder::getOrderNo, keyword);
        }
        if (status != null) {
            wrapper.eq(SalOrder::getStatus, status);
        }
        wrapper.orderByDesc(SalOrder::getCreateTime);
        IPage<SalOrder> result = orderMapper.selectPage(page, wrapper);
        PageResult<SalOrder> pageResult = PageUtil.of(result);

        // 写入缓存（销售数据变化较频繁，设置15分钟）
        cacheService.set(cacheKey, pageResult, CacheNames.TTL_SALES);

        return pageResult;
    }

    @Transactional
    public void createOrder(SalOrder order) {
        order.setOrderNo("SO" + IdUtil.getSnowflakeNextIdStr().substring(10));
        order.setStatus(1);
        orderMapper.insert(order);

        // 删除列表缓存
        cacheService.deleteByPrefix(CacheNames.SALES_ORDER_PAGE);

        // 添加到布隆过滤器
        cacheService.addToBloom(CacheNames.SALES_ORDER_BLOOM, order.getOrderNo());
    }

    public void auditOrder(Long id) {
        SalOrder order = orderMapper.selectById(id);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 1) throw new BusinessException("只能审核待审核订单");
        order.setStatus(2);
        orderMapper.updateById(order);

        // 删除相关缓存
        cacheService.deleteByPrefix(CacheNames.SALES_ORDER_PAGE);
    }

    public PageResult<SalOutbound> outboundPage(Integer pageNum, Integer pageSize, String keyword, Long orderId) {
        String cacheKey = cacheService.generateKey(
            CacheNames.SALES_OUTBOUND_PAGE, pageNum, pageSize,
            keyword != null ? keyword : "", orderId != null ? orderId : ""
        );

        PageResult<SalOutbound> cached = cacheService.getWithBloom(
            CacheNames.SALES_OUTBOUND_PAGE, cacheKey, null,
            new TypeReference<PageResult<SalOutbound>>() {}
        );
        if (cached != null) {
            return cached;
        }

        Page<SalOutbound> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SalOutbound> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(SalOutbound::getOutboundNo, keyword);
        }
        if (orderId != null) {
            wrapper.eq(SalOutbound::getOrderId, orderId);
        }
        wrapper.orderByDesc(SalOutbound::getCreateTime);
        IPage<SalOutbound> result = outboundMapper.selectPage(page, wrapper);
        PageResult<SalOutbound> pageResult = PageUtil.of(result);

        // 写入缓存（销售数据变化较频繁，设置15分钟）
        cacheService.set(cacheKey, pageResult, CacheNames.TTL_SALES);

        return pageResult;
    }

    @Transactional
    public void createOutbound(SalOutbound outbound) {
        // 检查库存
        List<SalOutboundDetail> details = outboundDetailMapper.selectList(
            new LambdaQueryWrapper<SalOutboundDetail>().eq(SalOutboundDetail::getOutboundId, outbound.getId())
        );

        for (SalOutboundDetail detail : details) {
            StcInventory inv = inventoryMapper.selectOne(
                new LambdaQueryWrapper<StcInventory>()
                    .eq(StcInventory::getProductId, detail.getProductId())
                    .eq(StcInventory::getWarehouseId, outbound.getWarehouseId())
            );

            if (inv == null || inv.getQuantity().compareTo(detail.getQuantity()) < 0) {
                throw new BusinessException("库存不足: " + detail.getProductName());
            }
        }

        outbound.setOutboundNo("OS" + IdUtil.getSnowflakeNextIdStr().substring(10));
        outbound.setStatus(1);
        outboundMapper.insert(outbound);

        // 扣减库存
        reduceInventory(outbound);

        // 删除缓存
        cacheService.deleteByPrefix(CacheNames.SALES_OUTBOUND_PAGE);
        cacheService.deleteByPrefix(CacheNames.INVENTORY_PAGE);
        cacheService.deleteByPrefix(CacheNames.INVENTORY_RECORD_PAGE);
    }

    private void reduceInventory(SalOutbound outbound) {
        List<SalOutboundDetail> details = outboundDetailMapper.selectList(
            new LambdaQueryWrapper<SalOutboundDetail>().eq(SalOutboundDetail::getOutboundId, outbound.getId())
        );

        for (SalOutboundDetail detail : details) {
            StcInventory inv = inventoryMapper.selectOne(
                new LambdaQueryWrapper<StcInventory>()
                    .eq(StcInventory::getProductId, detail.getProductId())
                    .eq(StcInventory::getWarehouseId, outbound.getWarehouseId())
            );

            BigDecimal beforeQty = inv.getQuantity();
            BigDecimal afterQty = beforeQty.subtract(detail.getQuantity());

            inv.setQuantity(afterQty);
            inventoryMapper.updateById(inv);

            // 记录库存流水
            StcRecord record = new StcRecord();
            record.setProductId(detail.getProductId());
            record.setProductName(detail.getProductName());
            record.setWarehouseId(outbound.getWarehouseId());
            record.setType(2);
            record.setInOut(2);
            record.setQuantity(detail.getQuantity());
            record.setBeforeQuantity(beforeQty);
            record.setAfterQuantity(afterQty);
            record.setBusinessId(outbound.getId());
            record.setBusinessNo(outbound.getOutboundNo());
            record.setOperateTime(LocalDateTime.now());
            recordMapper.insert(record);
        }
    }
}
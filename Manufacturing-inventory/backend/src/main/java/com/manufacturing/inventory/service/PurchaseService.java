package com.manufacturing.inventory.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manufacturing.inventory.common.BusinessException;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.PurInbound;
import com.manufacturing.inventory.entity.PurInboundDetail;
import com.manufacturing.inventory.entity.PurOrder;
import com.manufacturing.inventory.entity.StcInventory;
import com.manufacturing.inventory.entity.StcRecord;
import com.manufacturing.inventory.mapper.PurInboundDetailMapper;
import com.manufacturing.inventory.mapper.PurInboundMapper;
import com.manufacturing.inventory.mapper.PurOrderMapper;
import com.manufacturing.inventory.mapper.StcInventoryMapper;
import com.manufacturing.inventory.mapper.StcRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchaseService {

    private final PurOrderMapper orderMapper;
    private final PurInboundMapper inboundMapper;
    private final PurInboundDetailMapper inboundDetailMapper;
    private final StcInventoryMapper inventoryMapper;
    private final StcRecordMapper recordMapper;

    public PurchaseService(PurOrderMapper orderMapper,
                          PurInboundMapper inboundMapper, PurInboundDetailMapper inboundDetailMapper,
                          StcInventoryMapper inventoryMapper, StcRecordMapper recordMapper) {
        this.orderMapper = orderMapper;
        this.inboundMapper = inboundMapper;
        this.inboundDetailMapper = inboundDetailMapper;
        this.inventoryMapper = inventoryMapper;
        this.recordMapper = recordMapper;
    }

    public PageResult<PurOrder> orderPage(Integer pageNum, Integer pageSize, String keyword, Integer status) {
        Page<PurOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PurOrder> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(PurOrder::getOrderNo, keyword);
        }
        if (status != null) {
            wrapper.eq(PurOrder::getStatus, status);
        }
        wrapper.orderByDesc(PurOrder::getCreateTime);
        IPage<PurOrder> result = orderMapper.selectPage(page, wrapper);
        return PageUtil.of(result);
    }

    @Transactional
    public void createOrder(PurOrder order) {
        order.setOrderNo("PO" + IdUtil.getSnowflakeNextIdStr().substring(10));
        order.setStatus(1);
        orderMapper.insert(order);
    }

    public void auditOrder(Long id) {
        PurOrder order = orderMapper.selectById(id);
        if (order == null) throw new BusinessException("订单不存在");
        if (order.getStatus() != 1) throw new BusinessException("只能审核待审核订单");
        order.setStatus(2);
        orderMapper.updateById(order);
    }

    @Transactional
    public void createInbound(PurInbound inbound) {
        inbound.setInboundNo("PI" + IdUtil.getSnowflakeNextIdStr().substring(10));
        inbound.setStatus(1);
        inboundMapper.insert(inbound);
        
        // 更新库存
        updateInventory(inbound);
    }

    private void updateInventory(PurInbound inbound) {
        List<PurInboundDetail> details = inboundDetailMapper.selectList(
            new LambdaQueryWrapper<PurInboundDetail>().eq(PurInboundDetail::getInboundId, inbound.getId())
        );
        
        for (PurInboundDetail detail : details) {
            // 更新或插入库存
            StcInventory inv = inventoryMapper.selectOne(
                new LambdaQueryWrapper<StcInventory>()
                    .eq(StcInventory::getProductId, detail.getProductId())
                    .eq(StcInventory::getWarehouseId, inbound.getWarehouseId())
            );
            
            BigDecimal beforeQty = inv != null ? inv.getQuantity() : BigDecimal.ZERO;
            BigDecimal afterQty = beforeQty.add(detail.getQuantity());
            
            if (inv == null) {
                inv = new StcInventory();
                inv.setProductId(detail.getProductId());
                inv.setProductName(detail.getProductName());
                inv.setWarehouseId(inbound.getWarehouseId());
                inv.setQuantity(afterQty);
                inventoryMapper.insert(inv);
            } else {
                inv.setQuantity(afterQty);
                inventoryMapper.updateById(inv);
            }
            
            // 记录库存流水
            StcRecord record = new StcRecord();
            record.setProductId(detail.getProductId());
            record.setProductName(detail.getProductName());
            record.setWarehouseId(inbound.getWarehouseId());
            record.setType(1);
            record.setInOut(1);
            record.setQuantity(detail.getQuantity());
            record.setBeforeQuantity(beforeQty);
            record.setAfterQuantity(afterQty);
            record.setBusinessId(inbound.getId());
            record.setBusinessNo(inbound.getInboundNo());
            record.setOperateTime(LocalDateTime.now());
            recordMapper.insert(record);
        }
    }
}

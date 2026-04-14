package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 采购订单明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pur_order_detail")
public class PurOrderDetail extends BaseEntity {
    private Long orderId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private BigDecimal actualQuantity;
    private String remark;
}


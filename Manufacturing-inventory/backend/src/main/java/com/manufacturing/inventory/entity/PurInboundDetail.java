package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 采购入库单明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pur_inbound_detail")
public class PurInboundDetail extends BaseEntity {
    private Long inboundId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String remark;
}


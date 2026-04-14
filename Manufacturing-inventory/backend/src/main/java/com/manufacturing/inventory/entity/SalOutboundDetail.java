package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 销售出库单明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sal_outbound_detail")
public class SalOutboundDetail extends BaseEntity {
    private Long outboundId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String remark;
}


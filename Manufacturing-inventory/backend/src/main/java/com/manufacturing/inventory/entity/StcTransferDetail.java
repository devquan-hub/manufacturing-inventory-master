package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 库存调拨明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_transfer_detail")
public class StcTransferDetail extends BaseEntity {
    private Long transferId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private String remark;
}


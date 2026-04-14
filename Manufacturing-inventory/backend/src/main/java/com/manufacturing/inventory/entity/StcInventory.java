package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 库存表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_inventory")
public class StcInventory extends BaseEntity {
    private Long productId;
    private String productName;
    private Long warehouseId;
    private BigDecimal quantity;
    private BigDecimal frozenQuantity;
    private String remark;
}


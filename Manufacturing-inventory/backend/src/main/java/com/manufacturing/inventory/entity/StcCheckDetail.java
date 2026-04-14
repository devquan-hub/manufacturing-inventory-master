package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 库存盘点明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_check_detail")
public class StcCheckDetail extends BaseEntity {
    private Long checkId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal bookQuantity;
    private BigDecimal actualQuantity;
    private BigDecimal profitLossQuantity;
    private String remark;
}


package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 生产领料单明细
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_material_req_detail")
public class ProMaterialReqDetail extends BaseEntity {
    private Long reqId;
    private Long productId;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal actualQuantity;
    private String remark;
}


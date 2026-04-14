package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存记录表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_record")
public class StcRecord extends BaseEntity {
    private Long productId;
    private String productName;
    private Long warehouseId;
    private String warehouseName;
    private Integer type;
    private Integer inOut;
    private BigDecimal quantity;
    private BigDecimal beforeQuantity;
    private BigDecimal afterQuantity;
    private Long businessId;
    private String businessNo;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operateTime;
    
    private String remark;
}


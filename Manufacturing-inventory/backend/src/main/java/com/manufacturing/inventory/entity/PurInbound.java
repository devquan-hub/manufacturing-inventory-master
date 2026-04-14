package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购入库单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pur_inbound")
public class PurInbound extends BaseEntity {
    private String inboundNo;
    private Long orderId;
    private Long supplierId;
    private Long warehouseId;
    private Integer status;
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime inboundDate;
    private String remark;
}


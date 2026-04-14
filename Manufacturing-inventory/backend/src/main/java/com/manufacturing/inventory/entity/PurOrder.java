package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 采购订单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pur_order")
public class PurOrder extends BaseEntity {
    private String orderNo;
    private Long supplierId;
    private Long warehouseId;
    private Integer status;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectDate;
    private String remark;
}


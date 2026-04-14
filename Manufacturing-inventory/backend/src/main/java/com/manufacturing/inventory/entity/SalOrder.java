package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售订单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sal_order")
public class SalOrder extends BaseEntity {
    private String orderNo;
    private Long customerId;
    private Long warehouseId;
    private Integer status;
    private BigDecimal totalAmount;
    private BigDecimal receivedAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expectDate;
    private String remark;
}


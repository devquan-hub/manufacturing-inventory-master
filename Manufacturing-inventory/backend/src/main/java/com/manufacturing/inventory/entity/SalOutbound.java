package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售出库单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sal_outbound")
public class SalOutbound extends BaseEntity {
    private String outboundNo;
    private Long orderId;
    private Long customerId;
    private Long warehouseId;
    private Integer status;
    private BigDecimal totalAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime outboundDate;
    private String remark;
}


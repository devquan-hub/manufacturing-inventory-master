package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 库存调拨单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_transfer")
public class StcTransfer extends BaseEntity {
    private String transferNo;
    private Long fromWarehouseId;
    private Long toWarehouseId;
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime transferDate;
    
    private String remark;
}


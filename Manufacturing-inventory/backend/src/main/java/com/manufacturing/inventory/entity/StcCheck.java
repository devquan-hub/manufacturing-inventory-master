package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 库存盘点单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stc_check")
public class StcCheck extends BaseEntity {
    private String checkNo;
    private Long warehouseId;
    private Integer status;
    private Long checkerId;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime checkDate;
    
    private String remark;
}


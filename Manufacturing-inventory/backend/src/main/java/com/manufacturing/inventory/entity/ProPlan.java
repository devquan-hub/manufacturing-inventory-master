package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 生产计划
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_plan")
public class ProPlan extends BaseEntity {
    private String planNo;
    private String productName;
    private Long productId;
    private String spec;
    private String unit;
    private Long deptId;
    private Integer planQuantity;
    private Integer completedQuantity;
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime expectFinishDate;
    
    private String remark;
}


package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 生产报工单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_product")
public class ProProduct extends BaseEntity {
    private String reportNo;
    private Long planId;
    private Long materialReqId;
    private Long deptId;
    private Integer qualifiedQuantity;
    private Integer defectiveQuantity;
    private Integer status;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportTime;
    
    private String remark;
}


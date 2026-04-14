package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生产质检单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_quality")
public class ProQuality extends BaseEntity {
    private String qualityNo;
    private Long productId;
    private Integer inspectQuantity;
    private Integer qualifiedQuantity;
    private Integer defectiveQuantity;
    private Integer status;
    private Long inspectorId;
    private String result;
    private String remark;
}


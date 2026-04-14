package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生产领料单
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pro_material_req")
public class ProMaterialReq extends BaseEntity {
    private String reqNo;
    private Long planId;
    private Integer status;
    private Long applicantId;
    private String remark;
}


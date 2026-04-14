package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓库表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bas_warehouse")
public class BasWarehouse extends BaseEntity {
    private String code;
    private String name;
    private Long managerId;
    private String phone;
    private String address;
    private Integer type;
    private Integer status;
    private Integer isDefault;
    private String remark;
}

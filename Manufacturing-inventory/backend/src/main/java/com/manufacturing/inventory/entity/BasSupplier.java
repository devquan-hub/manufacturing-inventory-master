package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bas_supplier")
public class BasSupplier extends BaseEntity {
    private String code;
    private String name;
    private String contact;
    private String phone;
    private String address;
    private String bank;
    private String account;
    private Integer status;
    private String remark;
}

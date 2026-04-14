package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bas_customer")
public class BasCustomer extends BaseEntity {
    private String code;
    private String name;
    private String contact;
    private String phone;
    private String address;
    private String bank;
    private String account;
    private Integer levelType;
    private Integer status;
    private String remark;
}

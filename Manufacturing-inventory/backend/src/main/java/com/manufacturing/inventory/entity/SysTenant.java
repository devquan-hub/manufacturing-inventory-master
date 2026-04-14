package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_tenant")
public class SysTenant extends BaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String name;
    private String code;
    private String contact;
    private String phone;
    private Integer status;
    private String remark;
}

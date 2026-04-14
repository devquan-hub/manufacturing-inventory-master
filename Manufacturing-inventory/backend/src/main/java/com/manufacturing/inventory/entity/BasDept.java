package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bas_dept")
public class BasDept extends BaseEntity {
    private Long parentId;
    private String name;
    private Integer sort;
    private Integer status;
    private String remark;
}

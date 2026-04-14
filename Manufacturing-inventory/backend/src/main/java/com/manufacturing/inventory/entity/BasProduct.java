package com.manufacturing.inventory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

/**
 * 商品表（支持原材料/半成品/成品）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bas_product")
public class BasProduct extends BaseEntity {
    private String code;
    private String name;
    private String spec;
    private String unit;
    private Integer category;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private Integer safetyStock;
    private String barCode;
    private String imageUrl;
    private Integer status;
    private String remark;
}


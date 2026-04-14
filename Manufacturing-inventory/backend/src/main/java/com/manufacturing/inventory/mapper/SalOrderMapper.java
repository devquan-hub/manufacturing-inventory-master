package com.manufacturing.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.manufacturing.inventory.entity.SalOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SalOrderMapper extends BaseMapper<SalOrder> {
}

package com.manufacturing.inventory.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.manufacturing.inventory.common.BusinessException;
import com.manufacturing.inventory.common.PageResult;
import com.manufacturing.inventory.common.PageUtil;
import com.manufacturing.inventory.entity.BasProduct;
import com.manufacturing.inventory.mapper.BasProductMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {

    private final BasProductMapper productMapper;

    public ProductService(BasProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public PageResult<BasProduct> page(Integer pageNum, Integer pageSize, String keyword, Integer category) {
        Page<BasProduct> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BasProduct> wrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            wrapper.like(BasProduct::getName, keyword)
                   .or()
                   .like(BasProduct::getCode, keyword);
        }
        if (category != null) {
            wrapper.eq(BasProduct::getCategory, category);
        }
        wrapper.orderByDesc(BasProduct::getCreateTime);
        
        IPage<BasProduct> result = productMapper.selectPage(page, wrapper);
        return PageUtil.of(result);
    }

    public BasProduct getById(Long id) {
        BasProduct product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        return product;
    }

    public void save(BasProduct product) {
        productMapper.insert(product);
    }

    public void update(BasProduct product) {
        productMapper.updateById(product);
    }

    public void delete(Long id) {
        productMapper.deleteById(id);
    }
}

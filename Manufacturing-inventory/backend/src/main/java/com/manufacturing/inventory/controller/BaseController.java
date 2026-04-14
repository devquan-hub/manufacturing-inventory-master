package com.manufacturing.inventory.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.manufacturing.inventory.common.Result;
import com.manufacturing.inventory.entity.BasSupplier;
import com.manufacturing.inventory.entity.BasCustomer;
import com.manufacturing.inventory.entity.BasDept;
import com.manufacturing.inventory.mapper.BasSupplierMapper;
import com.manufacturing.inventory.mapper.BasCustomerMapper;
import com.manufacturing.inventory.mapper.BasDeptMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/base")
public class BaseController {

    private final BasSupplierMapper supplierMapper;
    private final BasCustomerMapper customerMapper;
    private final BasDeptMapper deptMapper;

    public BaseController(BasSupplierMapper supplierMapper, BasCustomerMapper customerMapper, BasDeptMapper deptMapper) {
        this.supplierMapper = supplierMapper;
        this.customerMapper = customerMapper;
        this.deptMapper = deptMapper;
    }

    @GetMapping("/supplier/list")
    public Result<List<BasSupplier>> supplierList() {
        return Result.success(supplierMapper.selectList(new LambdaQueryWrapper<BasSupplier>().orderByAsc(BasSupplier::getCode)));
    }

    @PostMapping("/supplier")
    public Result<Void> saveSupplier(@RequestBody BasSupplier supplier) {
        supplierMapper.insert(supplier);
        return Result.success();
    }

    @PutMapping("/supplier")
    public Result<Void> updateSupplier(@RequestBody BasSupplier supplier) {
        supplierMapper.updateById(supplier);
        return Result.success();
    }

    @DeleteMapping("/supplier/{id}")
    public Result<Void> deleteSupplier(@PathVariable Long id) {
        supplierMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/customer/list")
    public Result<List<BasCustomer>> customerList() {
        return Result.success(customerMapper.selectList(new LambdaQueryWrapper<BasCustomer>().orderByAsc(BasCustomer::getCode)));
    }

    @PostMapping("/customer")
    public Result<Void> saveCustomer(@RequestBody BasCustomer customer) {
        customerMapper.insert(customer);
        return Result.success();
    }

    @PutMapping("/customer")
    public Result<Void> updateCustomer(@RequestBody BasCustomer customer) {
        customerMapper.updateById(customer);
        return Result.success();
    }

    @DeleteMapping("/customer/{id}")
    public Result<Void> deleteCustomer(@PathVariable Long id) {
        customerMapper.deleteById(id);
        return Result.success();
    }

    @GetMapping("/dept/list")
    public Result<List<BasDept>> deptList() {
        return Result.success(deptMapper.selectList(new LambdaQueryWrapper<BasDept>().orderByAsc(BasDept::getSort)));
    }

    @PostMapping("/dept")
    public Result<Void> saveDept(@RequestBody BasDept dept) {
        deptMapper.insert(dept);
        return Result.success();
    }

    @PutMapping("/dept")
    public Result<Void> updateDept(@RequestBody BasDept dept) {
        deptMapper.updateById(dept);
        return Result.success();
    }

    @DeleteMapping("/dept/{id}")
    public Result<Void> deleteDept(@PathVariable Long id) {
        deptMapper.deleteById(id);
        return Result.success();
    }
}

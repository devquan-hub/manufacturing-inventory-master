<template>
  <div>
    <div class="page-header">
      <span class="page-title">库存记录</span>
    </div>

    <div class="search-bar">
      <el-form inline :model="searchForm">
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择" clearable>
            <el-option label="采购入库" :value="1" />
            <el-option label="销售出库" :value="2" />
            <el-option label="生产领料" :value="3" />
            <el-option label="成品入库" :value="4" />
            <el-option label="调拨出库" :value="5" />
            <el-option label="调拨入库" :value="6" />
            <el-option label="盘点调整" :value="7" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库">
          <el-select v-model="searchForm.warehouseId" placeholder="请选择" clearable>
            <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-card">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="warehouseName" label="仓库" width="120" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            {{ typeText[row.type] }}
          </template>
        </el-table-column>
        <el-table-column prop="inOut" label="出入库" width="80">
          <template #default="{ row }">
            <el-tag :type="row.inOut === 1 ? 'success' : 'danger'">{{ row.inOut === 1 ? '入库' : '出库' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100" />
        <el-table-column prop="beforeQuantity" label="变动前" width="100" />
        <el-table-column prop="afterQuantity" label="变动后" width="100" />
        <el-table-column prop="businessNo" label="单据号" width="180" />
        <el-table-column prop="operateTime" label="操作时间" width="160" />
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" @change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { inventoryApi, warehouseApi } from '../api'

const searchForm = reactive({ type: null, warehouseId: null })
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const warehouses = ref([])

const typeText = { 1: '采购入库', 2: '销售出库', 3: '生产领料', 4: '成品入库', 5: '调拨出库', 6: '调拨入库', 7: '盘点调整' }

const loadData = async () => {
  try {
    const res = await inventoryApi.recordPage({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  }
}

const loadWarehouses = async () => {
  try {
    const res = await warehouseApi.list()
    warehouses.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const reset = () => {
  searchForm.type = null
  searchForm.warehouseId = null
  loadData()
}

onMounted(() => {
  loadData()
  loadWarehouses()
})
</script>

<template>
  <div>
    <div class="page-header">
      <span class="page-title">库存查询</span>
    </div>

    <div class="search-bar">
      <el-form inline :model="searchForm">
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
        <el-table-column prop="productId" label="商品ID" width="100" />
        <el-table-column prop="productName" label="商品名称" />
        <el-table-column prop="warehouseId" label="仓库ID" width="100" />
        <el-table-column prop="quantity" label="库存数量" width="120" />
        <el-table-column prop="frozenQuantity" label="冻结数量" width="100" />
        <el-table-column prop="remark" label="备注" />
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" @change="loadData" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { inventoryApi, warehouseApi } from '../api'

const searchForm = reactive({ warehouseId: null })
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const warehouses = ref([])

const loadData = async () => {
  try {
    const res = await inventoryApi.page({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
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
  searchForm.warehouseId = null
  loadData()
}

onMounted(() => {
  loadData()
  loadWarehouses()
})
</script>

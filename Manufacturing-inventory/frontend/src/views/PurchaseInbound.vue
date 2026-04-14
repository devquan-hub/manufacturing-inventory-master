<template>
  <div>
    <div class="page-header">
      <span class="page-title">采购入库</span>
    </div>

    <div class="table-card">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="inboundNo" label="入库单号" width="180" />
        <el-table-column prop="supplierId" label="供应商ID" width="100" />
        <el-table-column prop="warehouseId" label="入库仓库ID" width="100" />
        <el-table-column prop="totalAmount" label="入库金额" width="120" />
        <el-table-column prop="inboundDate" label="入库日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '已完成' : '待入库' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { purchaseApi } from '../api'

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

const loadData = async () => {
  try {
    const res = await purchaseApi.orderPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => loadData())
</script>

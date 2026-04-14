<template>
  <div>
    <div class="page-header">
      <span class="page-title">销售出库</span>
    </div>

    <div class="table-card">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="outboundNo" label="出库单号" width="180" />
        <el-table-column prop="customerId" label="客户ID" width="100" />
        <el-table-column prop="warehouseId" label="出库仓库ID" width="100" />
        <el-table-column prop="totalAmount" label="出库金额" width="120" />
        <el-table-column prop="outboundDate" label="出库日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '已完成' : '待出库' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { salesApi } from '../api'

const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])

const loadData = async () => {
  try {
    const res = await salesApi.orderPage({ pageNum: pageNum.value, pageSize: pageSize.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => loadData())
</script>

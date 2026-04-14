<template>
  <div class="dashboard">
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #409eff">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.productCount }}</div>
            <div class="stat-label">商品数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #67c23a">
            <el-icon><OfficeBuilding /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.warehouseCount }}</div>
            <div class="stat-label">仓库数量</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #e6a23c">
            <el-icon><ShoppingCart /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.purchaseCount }}</div>
            <div class="stat-label">采购订单</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon" style="background: #f56c6c">
            <el-icon><Sell /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.salesCount }}</div>
            <div class="stat-label">销售订单</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近采购订单</span>
          </template>
          <el-table :data="recentPurchases" style="width: 100%">
            <el-table-column prop="orderNo" label="单号" width="150" />
            <el-table-column prop="supplierId" label="供应商" />
            <el-table-column prop="totalAmount" label="金额" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最近销售订单</span>
          </template>
          <el-table :data="recentSales" style="width: 100%">
            <el-table-column prop="orderNo" label="单号" width="150" />
            <el-table-column prop="customerId" label="客户" />
            <el-table-column prop="totalAmount" label="金额" />
            <el-table-column prop="status" label="状态">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { productApi, warehouseApi, purchaseApi, salesApi } from '../api'

const stats = ref({
  productCount: 0,
  warehouseCount: 0,
  purchaseCount: 0,
  salesCount: 0
})

const recentPurchases = ref([])
const recentSales = ref([])

onMounted(async () => {
  try {
    const [productRes, warehouseRes, purchaseRes, salesRes] = await Promise.all([
      productApi.page({ pageNum: 1, pageSize: 1 }),
      warehouseApi.list(),
      purchaseApi.orderPage({ pageNum: 1, pageSize: 5 }),
      salesApi.orderPage({ pageNum: 1, pageSize: 5 })
    ])
    
    stats.value.productCount = productRes.data.total
    stats.value.warehouseCount = warehouseRes.data.length
    stats.value.purchaseCount = purchaseRes.data.total
    stats.value.salesCount = salesRes.data.total
    recentPurchases.value = purchaseRes.data.records
    recentSales.value = salesRes.data.records
  } catch (e) {
    console.error(e)
  }
})

const getStatusType = (status) => {
  const types = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'info' }
  return types[status] || ''
}

const getStatusText = (status) => {
  const texts = { 1: '待审核', 2: '已审核', 3: '已完成', 4: '已取消' }
  return texts[status] || ''
}
</script>

<style scoped>
.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.stat-icon .el-icon {
  font-size: 30px;
  color: white;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  color: #999;
  margin-top: 5px;
}
</style>

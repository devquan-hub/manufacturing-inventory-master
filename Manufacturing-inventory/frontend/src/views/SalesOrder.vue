<template>
  <div>
    <div class="page-header">
      <span class="page-title">销售订单</span>
      <el-button type="primary" @click="openDialog()">新建订单</el-button>
    </div>

    <div class="search-bar">
      <el-form inline :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.keyword" placeholder="订单号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待审核" :value="1" />
            <el-option label="已审核" :value="2" />
            <el-option label="已完成" :value="3" />
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
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="customerId" label="客户ID" width="100" />
        <el-table-column prop="warehouseId" label="出库仓库ID" width="100" />
        <el-table-column prop="totalAmount" label="订单金额" width="120" />
        <el-table-column prop="orderDate" label="下单日期" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusText[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" v-if="row.status === 1" @click="handleAudit(row.id)">审核</el-button>
            <el-button link type="success" v-if="row.status === 2" @click="toOutbound(row)">出库</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" @change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" draggable title="新建销售订单" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="客户" prop="customerId">
          <el-input-number v-model="form.customerId" :min="1" />
        </el-form-item>
        <el-form-item label="出库仓库" prop="warehouseId">
          <el-input-number v-model="form.warehouseId" :min="1" />
        </el-form-item>
        <el-form-item label="订单日期">
          <el-date-picker v-model="form.orderDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { salesApi } from '../api'

const router = useRouter()
const searchForm = reactive({ keyword: '', status: null })
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const statusText = { 1: '待审核', 2: '已审核', 3: '已完成', 4: '已取消' }
const statusType = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'info' }

const form = reactive({ customerId: null, warehouseId: null, orderDate: '', remark: '' })

const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  warehouseId: [{ required: true, message: '请选择仓库', trigger: 'change' }]
}

const loadData = async () => {
  try {
    const res = await salesApi.orderPage({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch (e) {
    console.error(e)
  }
}

const reset = () => {
  searchForm.keyword = ''
  searchForm.status = null
  loadData()
}

const openDialog = () => {
  Object.assign(form, { customerId: null, warehouseId: null, orderDate: '', remark: '' })
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await salesApi.createOrder(form)
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadData()
      } catch (e) {
        console.error(e)
      }
    }
  })
}

const handleAudit = async (id) => {
  try {
    await salesApi.auditOrder(id)
    ElMessage.success('审核成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const toOutbound = (row) => {
  router.push({ path: '/sales/outbound', query: { orderId: row.id } })
}

onMounted(() => loadData())
</script>

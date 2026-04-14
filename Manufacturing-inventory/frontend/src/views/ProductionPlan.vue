<template>
  <div>
    <div class="page-header">
      <span class="page-title">生产计划</span>
      <el-button type="primary" @click="openDialog()">新建计划</el-button>
    </div>

    <div class="search-bar">
      <el-form inline :model="searchForm">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="计划号/产品名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择" clearable>
            <el-option label="待生产" :value="1" />
            <el-option label="生产中" :value="2" />
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
        <el-table-column prop="planNo" label="计划编号" width="180" />
        <el-table-column prop="productName" label="产品名称" />
        <el-table-column prop="planQuantity" label="计划数量" width="100" />
        <el-table-column prop="completedQuantity" label="已完成" width="100" />
        <el-table-column prop="deptId" label="生产部门" width="100" />
        <el-table-column prop="startDate" label="开始日期" width="120" />
        <el-table-column prop="expectFinishDate" label="预计完成" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="statusType[row.status]">{{ statusText[row.status] }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" v-if="row.status === 1" @click="handleStart(row.id)">启动</el-button>
            <el-button link type="success" v-if="row.status === 2" @click="handleComplete(row.id)">完成</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination style="margin-top: 20px" v-model:current-page="pageNum" v-model:page-size="pageSize" :total="total" layout="total, sizes, prev, pager, next" @change="loadData" />
    </div>

    <el-dialog v-model="dialogVisible" title="新建生产计划" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="产品名称" prop="productName">
          <el-input v-model="form.productName" />
        </el-form-item>
        <el-form-item label="产品ID">
          <el-input-number v-model="form.productId" :min="1" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.spec" />
        </el-form-item>
        <el-form-item label="单位">
          <el-input v-model="form.unit" />
        </el-form-item>
        <el-form-item label="生产部门">
          <el-input-number v-model="form.deptId" :min="1" />
        </el-form-item>
        <el-form-item label="计划数量" prop="planQuantity">
          <el-input-number v-model="form.planQuantity" :min="1" />
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预计完成">
          <el-date-picker v-model="form.expectFinishDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
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
import { productionApi } from '../api'

const searchForm = reactive({ keyword: '', status: null })
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const statusText = { 1: '待生产', 2: '生产中', 3: '已完成' }
const statusType = { 1: 'warning', 2: 'primary', 3: 'success' }

const form = reactive({
  productName: '', productId: null, spec: '', unit: '套', deptId: null,
  planQuantity: 0, startDate: '', expectFinishDate: '', remark: ''
})

const rules = {
  productName: [{ required: true, message: '请输入产品名称', trigger: 'blur' }],
  planQuantity: [{ required: true, message: '请输入计划数量', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await productionApi.planPage({ pageNum: pageNum.value, pageSize: pageSize.value, ...searchForm })
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
  Object.assign(form, { productName: '', productId: null, spec: '', unit: '套', deptId: null, planQuantity: 0, startDate: '', expectFinishDate: '', remark: '' })
  dialogVisible.value = true
}

const handleSave = async () => {
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await productionApi.createPlan(form)
        ElMessage.success('创建成功')
        dialogVisible.value = false
        loadData()
      } catch (e) {
        console.error(e)
      }
    }
  })
}

const handleStart = async (id) => {
  try {
    await productionApi.startPlan(id)
    ElMessage.success('启动成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleComplete = async (id) => {
  try {
    await productionApi.completePlan(id)
    ElMessage.success('完成成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => loadData())
</script>

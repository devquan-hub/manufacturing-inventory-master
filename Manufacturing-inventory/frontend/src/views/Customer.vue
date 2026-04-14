<template>
  <div>
    <div class="page-header">
      <span class="page-title">客户管理</span>
      <el-button type="primary" @click="openDialog()">新增客户</el-button>
    </div>

    <div class="table-card">
      <el-table :data="tableData" border stripe>
        <el-table-column prop="code" label="客户编码" width="120" />
        <el-table-column prop="name" label="客户名称" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="120" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="levelType" label="等级" width="80">
          <template #default="{ row }">
            {{ levelText[row.levelType] }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" ref="formRef" label-width="100px">
        <el-form-item label="客户编码" prop="code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="客户名称" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contact" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" />
        </el-form-item>
        <el-form-item label="客户等级">
          <el-select v-model="form.levelType" style="width: 100%">
            <el-option label="普通客户" :value="1" />
            <el-option label="VIP客户" :value="2" />
            <el-option label="战略客户" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { baseApi } from '../api'

const tableData = ref([])
const dialogVisible = ref(false)
const formRef = ref()

const levelText = { 1: '普通', 2: 'VIP', 3: '战略' }

const form = reactive({
  id: null, code: '', name: '', contact: '', phone: '', address: '', levelType: 1, status: 1, remark: ''
})

const dialogTitle = computed(() => form.id ? '编辑客户' : '新增客户')

const loadData = async () => {
  try {
    const res = await baseApi.customerList()
    tableData.value = res.data
  } catch (e) {
    console.error(e)
  }
}

const openDialog = (row) => {
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, { id: null, code: '', name: '', contact: '', phone: '', address: '', levelType: 1, status: 1, remark: '' })
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    if (form.id) {
      await baseApi.updateCustomer(form)
    } else {
      await baseApi.saveCustomer(form)
    }
    ElMessage.success('操作成功')
    dialogVisible.value = false
    loadData()
  } catch (e) {
    console.error(e)
  }
}

const handleDelete = async (id) => {
  await ElMessageBox.confirm('确定要删除吗？', '提示', { type: 'warning' })
  try {
    await baseApi.deleteCustomer(id)
    ElMessage.success('删除成功')
    loadData()
  } catch (e) {
    console.error(e)
  }
}

onMounted(() => loadData())
</script>

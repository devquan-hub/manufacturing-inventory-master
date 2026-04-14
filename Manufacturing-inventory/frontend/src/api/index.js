import request from '../utils/request'

export const authApi = {
  login: (data) => request.post('/auth/login', data),
  changePassword: (data) => request.post('/auth/change-password', data)
}

export const productApi = {
  page: (params) => request.get('/product/page', { params }),
  getById: (id) => request.get(`/product/${id}`),
  save: (data) => request.post('/product', data),
  update: (data) => request.put('/product', data),
  delete: (id) => request.delete(`/product/${id}`)
}

export const warehouseApi = {
  page: (params) => request.get('/warehouse/page', { params }),
  list: () => request.get('/warehouse/list'),
  save: (data) => request.post('/warehouse', data),
  update: (data) => request.put('/warehouse', data),
  delete: (id) => request.delete(`/warehouse/${id}`)
}

export const purchaseApi = {
  orderPage: (params) => request.get('/purchase/order/page', { params }),
  createOrder: (data) => request.post('/purchase/order', data),
  auditOrder: (id) => request.put(`/purchase/order/audit/${id}`),
  createInbound: (data) => request.post('/purchase/inbound', data)
}

export const salesApi = {
  orderPage: (params) => request.get('/sales/order/page', { params }),
  createOrder: (data) => request.post('/sales/order', data),
  auditOrder: (id) => request.put(`/sales/order/audit/${id}`),
  createOutbound: (data) => request.post('/sales/outbound', data)
}

export const productionApi = {
  planPage: (params) => request.get('/production/plan/page', { params }),
  createPlan: (data) => request.post('/production/plan', data),
  startPlan: (id) => request.put(`/production/plan/start/${id}`),
  completePlan: (id) => request.put(`/production/plan/complete/${id}`)
}

export const inventoryApi = {
  page: (params) => request.get('/inventory/page', { params }),
  list: (params) => request.get('/inventory/list', { params }),
  recordPage: (params) => request.get('/inventory/record/page', { params })
}

export const baseApi = {
  supplierList: () => request.get('/base/supplier/list'),
  saveSupplier: (data) => request.post('/base/supplier', data),
  updateSupplier: (data) => request.put('/base/supplier', data),
  deleteSupplier: (id) => request.delete(`/base/supplier/${id}`),
  customerList: () => request.get('/base/customer/list'),
  saveCustomer: (data) => request.post('/base/customer', data),
  updateCustomer: (data) => request.put('/base/customer', data),
  deleteCustomer: (id) => request.delete(`/base/customer/${id}`),
  deptList: () => request.get('/base/dept/list')
}

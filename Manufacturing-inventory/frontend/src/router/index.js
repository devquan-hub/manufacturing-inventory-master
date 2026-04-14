import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../store/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'product',
        name: 'Product',
        component: () => import('../views/Product.vue')
      },
      {
        path: 'warehouse',
        name: 'Warehouse',
        component: () => import('../views/Warehouse.vue')
      },
      {
        path: 'purchase/order',
        name: 'PurchaseOrder',
        component: () => import('../views/PurchaseOrder.vue')
      },
      {
        path: 'purchase/inbound',
        name: 'PurchaseInbound',
        component: () => import('../views/PurchaseInbound.vue')
      },
      {
        path: 'sales/order',
        name: 'SalesOrder',
        component: () => import('../views/SalesOrder.vue')
      },
      {
        path: 'sales/outbound',
        name: 'SalesOutbound',
        component: () => import('../views/SalesOutbound.vue')
      },
      {
        path: 'production/plan',
        name: 'ProductionPlan',
        component: () => import('../views/ProductionPlan.vue')
      },
      {
        path: 'inventory',
        name: 'Inventory',
        component: () => import('../views/Inventory.vue')
      },
      {
        path: 'inventory/record',
        name: 'InventoryRecord',
        component: () => import('../views/InventoryRecord.vue')
      },
      {
        path: 'base/supplier',
        name: 'Supplier',
        component: () => import('../views/Supplier.vue')
      },
      {
        path: 'base/customer',
        name: 'Customer',
        component: () => import('../views/Customer.vue')
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: () => import('../views/ChangePassword.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.path !== '/login' && !authStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && authStore.isLoggedIn) {
    next('/dashboard')
  } else {
    next()
  }
})

export default router

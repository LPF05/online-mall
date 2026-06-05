<template>
  <div class="order-list-page">
    <div class="container">
      <h1 class="page-title">我的订单</h1>

      <el-empty v-if="!authStore.isAuthenticated" description="请先登录">
        <router-link to="/login" class="btn btn-primary">去登录</router-link>
      </el-empty>

      <template v-else>
        <div class="order-tabs">
          <el-tabs v-model="activeTab" @tab-change="handleTabChange">
            <el-tab-pane label="全部" name="all">
              <span class="tab-count">{{ orders.length }}</span>
            </el-tab-pane>
            <el-tab-pane label="待支付" name="pending">
              <span class="tab-count">{{ pendingCount }}</span>
            </el-tab-pane>
            <el-tab-pane label="待发货" name="paid">
              <span class="tab-count">{{ paidCount }}</span>
            </el-tab-pane>
            <el-tab-pane label="待收货" name="shipped">
              <span class="tab-count">{{ shippedCount }}</span>
            </el-tab-pane>
            <el-tab-pane label="待评价" name="completed">
              <span class="tab-count">{{ completedCount }}</span>
            </el-tab-pane>
          </el-tabs>
        </div>

        <el-card v-if="isLoading" class="loading-card">
          <el-spin :size="36" />
        </el-card>

        <el-empty v-else-if="orders.length === 0" description="暂无订单">
          <router-link to="/products" class="btn btn-primary">去购物</router-link>
        </el-empty>

        <div v-else class="orders-list">
        <el-card 
          v-for="order in filteredOrders" 
          :key="order.id" 
          class="order-card" 
          shadow="hover"
        >
          <div class="order-header">
            <span class="order-no">订单号：{{ order.orderNo }}</span>
            <el-tag :type="getStatusType(order.status)">
              {{ getStatusText(order.status) }}
            </el-tag>
          </div>
          <div class="order-items">
            <div 
              v-for="(item, index) in getOrderItems(order)" 
              :key="index" 
              class="order-item"
            >
              <span class="item-name">{{ item.productName }}</span>
              <span class="item-qty">x{{ item.quantity }}</span>
              <span class="item-price">¥{{ (item.price * item.quantity).toFixed(2) }}</span>
            </div>
          </div>
          <div class="order-footer">
            <span class="order-total">总计：¥{{ order.totalAmount.toFixed(2) }}</span>
            <div class="order-actions">
              <router-link :to="`/orders/${order.orderNo}`" class="btn btn-outline btn-sm">
                查看详情
              </router-link>
              <el-button
                v-if="order.status === 0"
                type="primary"
                size="small"
                :loading="payingOrder === order.orderNo"
                @click="handlePay(order.orderNo)"
              >
                支付
              </el-button>
              <el-button
                v-if="order.status === 2"
                type="primary"
                size="small"
                @click="handleConfirmReceipt(order.orderNo)"
              >
                确认收货
              </el-button>
              <el-button
                v-if="order.status === 3"
                type="primary"
                size="small"
                @click="handleReview(order.orderNo)"
              >
                去评价
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { orderAPI, type Order } from '@/api/order'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const userStore = useUserStore()
const orders = ref<Order[]>([])
const isLoading = ref(false)
const payingOrder = ref<string | null>(null)
const activeTab = ref('all')

const filteredOrders = computed(() => {
  if (activeTab.value === 'all') {
    return orders.value
  }
  const statusMap: Record<string, number> = {
    pending: 0,
    paid: 1,
    shipped: 2,
    completed: 3
  }
  return orders.value.filter(o => o.status === statusMap[activeTab.value])
})

const pendingCount = computed(() => orders.value.filter(o => o.status === 0).length)
const paidCount = computed(() => orders.value.filter(o => o.status === 1).length)
const shippedCount = computed(() => orders.value.filter(o => o.status === 2).length)
const completedCount = computed(() => orders.value.filter(o => o.status === 3).length)

const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待支付',
    1: '待发货',
    2: '待收货',
    3: '待评价',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getOrderItems = (order: Order) => {
  if (!order.items) return []
  if (typeof order.items === 'string') {
    return JSON.parse(order.items as string)
  }
  return order.items
}

const getStatusType = (status: number) => {
  const typeMap: Record<number, string> = {
    0: 'warning',
    1: 'primary',
    2: 'info',
    3: 'success',
    4: 'danger'
  }
  return typeMap[status] || 'default'
}

const handleTabChange = () => {
}

const handleConfirmReceipt = async (orderNo: string) => {
  try {
    await orderAPI.confirmReceipt(orderNo)
    ElMessage.success('确认收货成功')
    loadOrders()
  } catch (error) {
    ElMessage.error('确认失败：' + (error as Error).message)
  }
}

const handleReview = async (_orderNo: string) => {
  ElMessage.info('评价功能开发中')
}

const loadOrders = async () => {
  isLoading.value = true
  try {
    const data = await orderAPI.getOrderList()
    orders.value = data || []
  } catch (error) {
    console.error('Failed to load orders:', error)
  } finally {
    isLoading.value = false
  }
}

const handlePay = async (orderNo: string) => {
  payingOrder.value = orderNo
  try {
    await orderAPI.payOrder(orderNo)
    ElMessage.success('支付成功！')
    await userStore.fetchUser()
    await loadOrders()
  } catch (error) {
    ElMessage.error('支付失败：' + (error as Error).message)
  } finally {
    payingOrder.value = null
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    loadOrders()
  }
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.order-list-page {
  padding: $spacing-lg 0;

  .page-title {
    font-size: $font-size-xxl;
    font-weight: 700;
    margin-bottom: $spacing-xl;
  }

  .loading-card {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: $spacing-xl * 2;
  }

  .orders-list {
    display: flex;
    flex-direction: column;
    gap: $spacing-lg;
  }

  .order-card {
    overflow: hidden;
  }

  .order-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-md $spacing-lg;
    background: $background-color;
    border-bottom: 1px solid $border-light;

    .order-no {
      font-weight: 600;
      color: $text-primary;
    }
  }

  .order-items {
    padding: $spacing-md $spacing-lg;
  }

  .order-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-sm 0;
    border-bottom: 1px solid $border-light;

    &:last-child {
      border-bottom: none;
    }

    .item-name {
      flex: 1;
    }

    .item-qty {
      margin: 0 $spacing-lg;
      color: $text-secondary;
    }

    .item-price {
      font-weight: 600;
      color: $primary-color;
    }
  }

  .order-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-md $spacing-lg;
    border-top: 1px solid $border-light;

    .order-total {
      font-size: $font-size-lg;
      font-weight: 700;
      color: $primary-color;
    }

    .order-actions {
      display: flex;
      gap: $spacing-sm;

      .btn-sm {
        padding: $spacing-xs $spacing-md;
        font-size: $font-size-sm;
      }
    }
  }
}
</style>
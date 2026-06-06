<template>
  <div class="order-detail-page">
    <div class="container">
      <router-link to="/orders" class="back-link">&larr; 返回订单列表</router-link>

      <div v-if="isLoading" class="loading">加载中...</div>

      <div v-else-if="order" class="order-detail">
        <div class="detail-header">
          <h1>订单详情</h1>
          <span :class="['order-status', getStatusClass(order.status)]">
            {{ getStatusText(order.status) }}
          </span>
        </div>

        <div class="detail-section">
          <h3>订单信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <label>订单号</label>
              <span>{{ order.orderNo }}</span>
            </div>
            <div class="info-item">
              <label>下单时间</label>
              <span>{{ formatDate(order.createdAt) }}</span>
            </div>
            <div class="info-item">
              <label>订单金额</label>
              <span class="price">¥{{ order.totalAmount.toFixed(2) }}</span>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <h3>商品列表</h3>
          <div class="items-list">
            <div v-for="(item, index) in getOrderItems()" :key="index" class="item-card">
              <div class="item-image">
                <img :src="item.imageUrl" :alt="item.productName" />
              </div>
              <div class="item-info">
                <h4>{{ item.productName }}</h4>
                <p class="item-price">¥{{ item.price.toFixed(2) }}</p>
              </div>
              <div class="item-qty">x{{ item.quantity }}</div>
              <div class="item-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
            </div>
          </div>
        </div>

        <div v-if="order.status === 0 || order.status === 2" class="detail-actions">
          <el-button v-if="order.status === 0" type="primary" @click="handlePay">
            立即支付
          </el-button>
          <el-button v-if="order.status === 2" type="success" @click="handleConfirmReceipt">
            确认收货
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { orderAPI, type Order } from '@/api/order'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const order = ref<Order | null>(null)
const isLoading = ref(false)

const getStatusText = (status: number) => {
  const statusMap: Record<number, string> = {
    0: '待支付',
    1: '待发货',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusClass = (status: number) => {
  const classMap: Record<number, string> = {
    0: 'status-pending',
    1: 'status-paid',
    2: 'status-shipped',
    3: 'status-completed',
    4: 'status-cancelled'
  }
  return classMap[status] || ''
}

const formatDate = (dateStr: string) => {
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getOrderItems = () => {
  if (!order.value) return []
  const items = order.value.items
  if (typeof items === 'string') {
    return JSON.parse(items)
  }
  return items || []
}

const loadOrder = async () => {
  isLoading.value = true
  try {
    const data = await orderAPI.getOrderDetail(route.params.orderNo as string)
    order.value = data
  } catch (error) {
    console.error('Failed to load order:', error)
  } finally {
    isLoading.value = false
  }
}

const handlePay = async () => {
  try {
    await orderAPI.payOrder(order.value!.orderNo)
    ElMessage.success('支付成功！')
    await userStore.fetchUser()
    await loadOrder()
  } catch (error) {
    ElMessage.error('支付失败：' + (error as Error).message)
  }
}

const handleConfirmReceipt = async () => {
  try {
    await orderAPI.confirmReceipt(order.value!.orderNo)
    ElMessage.success('确认收货成功！')
    await loadOrder()
  } catch (error) {
    ElMessage.error('确认收货失败：' + (error as Error).message)
  }
}

onMounted(() => {
  loadOrder()
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.order-detail-page {
  padding: $spacing-lg 0;

  .back-link {
    display: inline-block;
    margin-bottom: $spacing-lg;
    color: $primary-color;
    font-weight: 500;

    &:hover {
      text-decoration: underline;
    }
  }

  .loading {
    text-align: center;
    padding: $spacing-xl * 2;
  }

  .order-detail {
    background: white;
    border-radius: $border-radius-md;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  }

  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-lg;
    border-bottom: 1px solid $border-color;

    h1 {
      font-size: $font-size-xl;
      font-weight: 700;
    }

    .order-status {
      padding: 6px 16px;
      border-radius: $border-radius-sm;
      font-weight: 600;

      &.status-pending {
        background: rgba($primary-color, 0.1);
        color: $primary-color;
      }

      &.status-paid {
        background: rgba($accent-color, 0.1);
        color: $accent-color;
      }

      &.status-cancelled {
        background: rgba($text-secondary, 0.1);
        color: $text-secondary;
      }
    }
  }

  .detail-section {
    padding: $spacing-lg;
    border-bottom: 1px solid $border-color;

    h3 {
      font-size: $font-size-lg;
      font-weight: 600;
      margin-bottom: $spacing-md;
    }

    &:last-of-type {
      border-bottom: none;
    }
  }

  .info-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: $spacing-lg;

    @media (max-width: $breakpoint-md) {
      grid-template-columns: 1fr;
    }

    .info-item {
      label {
        display: block;
        font-size: $font-size-sm;
        color: $text-secondary;
        margin-bottom: $spacing-xs;
      }

      span {
        font-weight: 500;
      }

      .price {
        font-size: $font-size-lg;
        font-weight: 700;
        color: $primary-color;
      }
    }
  }

  .items-list {
    display: flex;
    flex-direction: column;
    gap: $spacing-md;
  }

  .item-card {
    display: grid;
    grid-template-columns: 80px 1fr auto auto;
    gap: $spacing-md;
    align-items: center;
    padding: $spacing-md;
    background: $background-color;
    border-radius: $border-radius-sm;

    .item-image {
      width: 80px;
      height: 80px;
      border-radius: $border-radius-sm;
      overflow: hidden;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .item-info {
      h4 {
        font-weight: 600;
        margin-bottom: $spacing-xs;
      }

      .item-price {
        color: $text-secondary;
      }
    }

    .item-qty {
      color: $text-secondary;
    }

    .item-subtotal {
      font-weight: 700;
      color: $primary-color;
    }
  }

  .detail-actions {
    padding: $spacing-lg;
    border-top: 1px solid $border-color;
    text-align: right;

    .btn {
      min-width: 200px;
    }
  }
}
</style>

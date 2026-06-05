<template>
  <div class="browse-history-page">
    <div class="container">
      <div class="page-header">
        <h1 class="page-title">浏览记录</h1>
        <el-button 
          v-if="authStore.isAuthenticated && browseHistory.length > 0" 
          type="text" 
          class="clear-btn"
          @click="handleClearHistory"
        >
          清空记录
        </el-button>
      </div>

      <el-empty v-if="!authStore.isAuthenticated" description="请先登录">
        <router-link to="/login" class="btn btn-primary">去登录</router-link>
      </el-empty>

      <el-card v-else-if="isLoading" class="loading-card">
        <el-spin :size="36" />
      </el-card>

      <el-empty 
        v-else-if="browseHistory.length === 0" 
        description="暂无浏览记录"
      >
        <router-link to="/products" class="btn btn-primary">去逛逛</router-link>
      </el-empty>

      <div v-else class="history-grid">
        <router-link 
          v-for="item in browseHistory" 
          :key="item.id"
          :to="`/products/${item.productId}`"
          class="history-card"
        >
          <div class="history-image">
            <img :src="getProductImage(item.productId)" :alt="getProductName(item.productId)" />
          </div>
          <div class="history-info">
            <h3 class="history-name">{{ getProductName(item.productId) }}</h3>
            <p class="history-price">¥{{ getProductPrice(item.productId).toFixed(2) }}</p>
            <span class="history-time">{{ formatTime(item.createdAt) }}</span>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useBrowseHistoryStore } from '@/stores/browseHistory'
import { useProductStore } from '@/stores/product'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'
import type { Product } from '@/api/product'

const browseHistoryStore = useBrowseHistoryStore()
const productStore = useProductStore()
const authStore = useAuthStore()

const isLoading = ref(true)
const browseHistory = ref<any[]>([])

const products = computed(() => productStore.products)

const getProductName = (productId: number) => {
  const product = products.value.find((p: Product) => p.id === productId)
  return product?.name || '未知商品'
}

const getProductPrice = (productId: number) => {
  const product = products.value.find((p: Product) => p.id === productId)
  return product?.price || 0
}

const getProductImage = (productId: number) => {
  const product = products.value.find((p: Product) => p.id === productId)
  return product?.imageUrl || 'https://images.unsplash.com/photo-1568702846914-96165c5e56e4?w=400'
}

const formatTime = (timestamp: string) => {
  const date = new Date(timestamp)
  const now = new Date()
  const diff = now.getTime() - date.getTime()
  
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 604800000) return `${Math.floor(diff / 86400000)}天前`
  
  return `${date.getMonth() + 1}/${date.getDate()}`
}

const handleClearHistory = async () => {
  try {
    await browseHistoryStore.clearHistory()
    browseHistory.value = []
    ElMessage.success('浏览记录已清空')
  } catch (error) {
    ElMessage.error('清空失败：' + (error as Error).message)
  }
}

const loadData = async () => {
  isLoading.value = true
  try {
    await browseHistoryStore.loadHistory()
    browseHistory.value = browseHistoryStore.history
    await productStore.loadProducts()
  } catch (error) {
    console.error('Failed to load browse history:', error)
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.browse-history-page {
  padding: $spacing-lg 0;
  min-height: calc(100vh - 160px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: $spacing-xl;

    .page-title {
      font-size: $font-size-xxl;
      font-weight: 700;
    }

    .clear-btn {
      color: $text-secondary;
      font-size: $font-size-sm;

      &:hover {
        color: $danger-color;
      }
    }
  }

  .loading-card {
    display: flex;
    justify-content: center;
    padding: $spacing-2xl;
  }

  .history-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: $spacing-lg;

    @media (max-width: $breakpoint-xl) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: repeat(2, 1fr);
      gap: $spacing-md;
    }
  }

  .history-card {
    display: flex;
    gap: $spacing-md;
    padding: $spacing-md;
    background: $background-card;
    border-radius: $border-radius-lg;
    text-decoration: none;
    transition: all $transition-fast;
    border: 1px solid $border-light;

    &:hover {
      transform: translateY(-2px);
      box-shadow: $shadow-md;
      border-color: $primary-color;
    }

    .history-image {
      width: 100px;
      height: 100px;
      flex-shrink: 0;
      border-radius: $border-radius-md;
      overflow: hidden;
      background: $background-grey;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }
    }

    .history-info {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      min-width: 0;

      .history-name {
        font-size: $font-size-base;
        font-weight: 600;
        color: $text-primary;
        margin: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .history-price {
        font-size: $font-size-lg;
        font-weight: 700;
        color: $primary-color;
        margin: $spacing-xs 0;
      }

      .history-time {
        font-size: $font-size-xs;
        color: $text-muted;
      }
    }
  }
}
</style>
<template>
  <div class="product-detail-page">
    <div class="container">
      <div class="back-button" v-if="!isLoading && product">
        <el-button @click="handleBack" class="back-btn">
          <span class="back-icon">←</span>
          返回
        </el-button>
      </div>

      <el-card v-if="isLoading" class="loading-card">
        <el-spin :size="36" />
      </el-card>

      <el-card v-else-if="product" class="product-detail-card" shadow="hover">
        <div class="product-main">
          <div class="product-image">
            <img :src="getProductImage(product.imageUrl)" :alt="product.name" />
          </div>
          <div class="product-info">
            <div class="product-header">
              <h1 class="product-name">{{ product.name }}</h1>
              <el-button 
                class="favorite-btn"
                :class="{ active: isFavorite }"
                @click="handleFavorite"
                :disabled="!authStore.isAuthenticated"
              >
                {{ isFavorite ? '❤️' : '🤍' }}
                {{ isFavorite ? '已收藏' : '收藏' }}
              </el-button>
            </div>
            <p class="product-desc">{{ product.description }}</p>
            <div class="product-price-section">
              <span class="price-label">价格</span>
              <span class="price-value">¥{{ product.price.toFixed(2) }}</span>
            </div>
            <el-row class="product-stats">
              <el-col :span="8">
                <div class="stat-item">
                  <span class="stat-label">库存</span>
                  <span class="stat-value">{{ product.stock }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <span class="stat-label">销量</span>
                  <span class="stat-value">{{ product.salesCount }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="stat-item">
                  <span class="stat-label">浏览</span>
                  <span class="stat-value">{{ product.viewCount }}</span>
                </div>
              </el-col>
            </el-row>
            <div class="product-actions">
              <div class="quantity-selector">
                <el-button 
                  size="small" 
                  @click="quantity > 1 && quantity--"
                  :disabled="quantity <= 1"
                >
                  -
                </el-button>
                <span class="quantity-value">{{ quantity }}</span>
                <el-button 
                  size="small" 
                  type="primary"
                  @click="quantity < product.stock && quantity++"
                  :disabled="quantity >= product.stock"
                >
                  +
                </el-button>
              </div>
              <el-button 
                type="primary" 
                size="large"
                class="add-cart-btn"
                :disabled="!authStore.isAuthenticated"
                @click="handleAddToCart"
              >
                {{ authStore.isAuthenticated ? '加入购物车' : '请先登录' }}
              </el-button>
            </div>
          </div>
        </div>
      </el-card>

      <!-- Reviews Section -->
      <el-card v-if="product" class="reviews-card" shadow="hover">
        <template #header>
          <div class="reviews-header">
            <span class="reviews-title">商品评价</span>
            <span class="reviews-summary" v-if="reviews.length > 0">
              <el-rate :model-value="averageRating" disabled show-score text-color="#ff9900" score-template="{value} 分" />
              <span class="review-count">共 {{ reviews.length }} 条评价</span>
            </span>
          </div>
        </template>

        <div v-if="reviews.length === 0" class="no-reviews">
          <el-empty description="暂无评价" :image-size="80" />
        </div>

        <div v-else class="reviews-list">
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-meta">
              <span class="review-username">{{ review.username }}</span>
              <el-rate :model-value="review.rating" disabled :size="'small'" />
              <span class="review-date">{{ formatDate(review.createdAt) }}</span>
            </div>
            <p class="review-content" v-if="review.content">{{ review.content }}</p>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { productAPI, type Product } from '@/api/product'
import { browseHistoryAPI } from '@/api/browseHistory'
import { reviewAPI, type Review } from '@/api/review'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { useFavoriteStore } from '@/stores/favorite'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const cartStore = useCartStore()
const favoriteStore = useFavoriteStore()

const product = ref<Product | null>(null)
const isLoading = ref(false)
const quantity = ref(1)
const isFavorite = ref(false)

// Reviews
const reviews = ref<Review[]>([])
const averageRating = computed(() => {
  if (reviews.value.length === 0) return 0
  const sum = reviews.value.reduce((acc, r) => acc + r.rating, 0)
  return Math.round((sum / reviews.value.length) * 10) / 10
})

const formatDate = (dateStr: string) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

const loadReviews = async () => {
  if (!product.value) return
  try {
    reviews.value = await reviewAPI.getProductReviews(product.value.id)
  } catch (error) {
    console.error('Failed to load reviews:', error)
  }
}

const getProductImage = (imageUrl?: string) => {
  if (!imageUrl || imageUrl === '') {
    return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20clean%20white%20background&image_size=square_hd`
  }
  return imageUrl
}

const loadProduct = async () => {
  isLoading.value = true
  try {
    const data = await productAPI.getDetail(Number(route.params.id))
    product.value = data
    if (authStore.isAuthenticated) {
      await favoriteStore.loadFavorites()
      isFavorite.value = favoriteStore.favorites.some(f => f.productId === product.value?.id)
    }
    await addBrowseHistory()
    await loadReviews()
  } catch (error) {
    console.error('Failed to load product:', error)
  } finally {
    isLoading.value = false
  }
}

const handleBack = () => {
  router.back()
}

const handleAddToCart = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    return
  }
  const success = await cartStore.addToCart(product.value!.id, quantity.value)
  if (success) {
    ElMessage.success('添加成功')
  }
}

const handleFavorite = async () => {
  if (!authStore.isAuthenticated) {
    ElMessage.warning('请先登录')
    return
  }
  
  if (isFavorite.value) {
    await favoriteStore.removeFavorite(product.value!.id)
    isFavorite.value = false
    ElMessage.success('已取消收藏')
  } else {
    await favoriteStore.addFavorite(product.value!.id)
    isFavorite.value = true
    ElMessage.success('收藏成功')
  }
}

const addBrowseHistory = async () => {
  if (authStore.isAuthenticated && product.value) {
    try {
      await browseHistoryAPI.addHistory(product.value.id)
    } catch (error) {
      console.error('Failed to add browse history:', error)
    }
  }
}

onMounted(() => {
  loadProduct()
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.product-detail-page {
  padding: $spacing-lg 0;

  .loading-card {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: $spacing-xl * 2;
  }

  .product-detail-card {
    overflow: hidden;
  }

  .product-main {
    display: grid;
    grid-template-columns: 400px 1fr;
    gap: $spacing-xl;

    @media (max-width: $breakpoint-lg) {
      grid-template-columns: 1fr;
    }
  }

  .product-image {
    width: 100%;
    height: 400px;
    border-radius: $border-radius-md;
    overflow: hidden;
    background: $background-color;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .product-info {
    display: flex;
    flex-direction: column;
    gap: $spacing-md;
  }

  .product-name {
    font-size: $font-size-xxl;
    font-weight: 700;
    color: $text-primary;
  }

  .product-desc {
    font-size: $font-size-base;
    color: $text-secondary;
    line-height: 1.8;
  }

  .product-price-section {
    padding: $spacing-lg;
    background: $background-color;
    border-radius: $border-radius-md;

    .price-label {
      color: $text-secondary;
      margin-right: $spacing-md;
    }

    .price-value {
      font-size: 36px;
      font-weight: 700;
      color: $primary-color;
    }
  }

  .product-stats {
    .stat-item {
      text-align: center;
      padding: $spacing-md;

      .stat-label {
        display: block;
        font-size: $font-size-sm;
        color: $text-secondary;
        margin-bottom: $spacing-xs;
      }

      .stat-value {
        font-size: $font-size-lg;
        font-weight: 600;
        color: $text-primary;
      }
    }
  }

  .product-actions {
    display: flex;
    gap: $spacing-md;
    margin-top: $spacing-lg;
  }

  .quantity-selector {
    display: flex;
    align-items: center;
    border: 2px solid $border-light;
    border-radius: $border-radius-sm;

    .quantity-value {
      padding: 0 $spacing-lg;
      font-size: $font-size-base;
    }
  }

  .add-cart-btn {
    flex: 1;
    height: 48px;
    font-size: $font-size-lg;
  }

  .reviews-card {
    margin-top: $spacing-lg;
  }

  .reviews-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .reviews-title {
      font-size: $font-size-lg;
      font-weight: 600;
    }

    .reviews-summary {
      display: flex;
      align-items: center;
      gap: $spacing-sm;

      .review-count {
        color: $text-secondary;
        font-size: $font-size-sm;
      }
    }
  }

  .no-reviews {
    padding: $spacing-lg 0;
  }

  .reviews-list {
    .review-item {
      padding: $spacing-md 0;
      border-bottom: 1px solid $border-light;

      &:last-child {
        border-bottom: none;
      }

      .review-meta {
        display: flex;
        align-items: center;
        gap: $spacing-md;
        margin-bottom: $spacing-xs;

        .review-username {
          font-weight: 600;
          color: $text-primary;
        }

        .review-date {
          color: $text-secondary;
          font-size: $font-size-sm;
        }
      }

      .review-content {
        color: $text-primary;
        line-height: 1.6;
        margin: $spacing-xs 0 0 0;
      }
    }
  }
}
</style>
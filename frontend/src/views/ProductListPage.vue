<template>
  <div class="product-list-page">
    <div class="container">
      <div class="page-header">
        <div class="header-top">
          <h1 class="page-title">{{ currentCategory || '全部商品' }}</h1>
          <span class="product-count">共 {{ products.length }} 件商品</span>
        </div>
        
        <el-card class="filter-card" shadow="hover">
          <div class="filter-section">
            <div class="search-box">
              <el-input 
                v-model="searchKeyword" 
                placeholder="搜索商品" 
                class="search-input"
                @keyup.enter="handleSearch"
              >
                <template #append>
                  <el-button @click="handleSearch" type="primary" class="search-btn">
                    <span>搜索</span>
                  </el-button>
                </template>
              </el-input>
            </div>
            
            <div class="filter-row">
              <div class="filter-item">
                <el-select 
                  v-model="selectedCategory" 
                  placeholder="分类"
                  class="filter-select"
                  @change="handleFilter"
                >
                  <el-option label="全部" value="" />
                  <el-option 
                    v-for="cat in categories" 
                    :key="cat" 
                    :label="cat" 
                    :value="cat" 
                  />
                </el-select>
              </div>
              
              <div class="filter-item">
                <el-select 
                  v-model="selectedSort" 
                  placeholder="排序"
                  class="filter-select"
                  @change="handleFilter"
                >
                  <el-option label="最新上架" value="created_at" />
                  <el-option label="价格从低到高" value="price_asc" />
                  <el-option label="价格从高到低" value="price_desc" />
                  <el-option label="销量优先" value="sales_count" />
                  <el-option label="热度优先" value="view_count" />
                </el-select>
              </div>
              
              <div class="filter-item">
                <el-select 
                  v-model="priceRange" 
                  placeholder="价格区间"
                  class="filter-select"
                  @change="handleFilter"
                >
                  <el-option label="全部" value="" />
                  <el-option label="0-100元" value="0-100" />
                  <el-option label="100-500元" value="100-500" />
                  <el-option label="500-1000元" value="500-1000" />
                  <el-option label="1000元以上" value="1000+" />
                </el-select>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <div v-if="isLoading" class="skeleton-grid">
        <el-card v-for="i in 8" :key="i" class="skeleton-card" shadow="hover">
          <el-skeleton animated>
            <template #template>
              <el-skeleton-item variant="image" style="width: 100%; height: 180px;" />
              <div style="padding: 14px 0;">
                <el-skeleton-item variant="h3" style="width: 80%;" />
                <el-skeleton-item variant="text" style="width: 60%; margin-top: 8px;" />
                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 12px;">
                  <el-skeleton-item variant="text" style="width: 30%;" />
                  <el-skeleton-item variant="text" style="width: 20%;" />
                </div>
              </div>
            </template>
          </el-skeleton>
        </el-card>
      </div>
      
      <el-empty 
        v-else-if="products.length === 0" 
        description="暂无商品"
        class="empty-container"
      >
        <router-link to="/" class="btn btn-primary">返回首页</router-link>
      </el-empty>
      
      <div v-else class="products-grid">
        <ProductCard
          v-for="product in products"
          :key="product.id"
          :product="product"
          @added="handleProductAdded"
        />
      </div>

      <Transition name="toast">
        <div v-if="showToast" class="toast-notification">
          ✓ 已添加到购物车
        </div>
      </Transition>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { productAPI, type Product } from '@/api/product'
import ProductCard from '@/components/product/ProductCard.vue'

const route = useRoute()
const products = ref<Product[]>([])
const isLoading = ref(false)
const selectedCategory = ref('')
const selectedSort = ref('')
const priceRange = ref('')
const searchKeyword = ref('')
const showToast = ref(false)

const categories = ['电子产品', '服装', '食品', '家居', '美妆', '图书', '生活', '玩具', '户外']

const currentCategory = computed(() => {
  if (selectedCategory.value) {
    return selectedCategory.value
  }
  return ''
})

const loadProducts = async () => {
  isLoading.value = true
  try {
    const params: any = {}

    if (selectedSort.value) {
      params.sort = selectedSort.value
    }

    if (selectedCategory.value) {
      params.category = selectedCategory.value
    }

    if (priceRange.value) {
      const range = priceRange.value.split('-')
      if (range[0]) params.minPrice = Number(range[0])
      if (range[1] && range[1] !== '+') params.maxPrice = Number(range[1])
    }

    if (searchKeyword.value) {
      params.keyword = searchKeyword.value
    }

    const response = await productAPI.getList(params)
    products.value = response || []
  } catch (error) {
    console.error('Failed to load products:', error)
  } finally {
    isLoading.value = false
  }
}

const handleFilter = () => {
  loadProducts()
}

const handleSearch = () => {
  loadProducts()
}

const handleProductAdded = () => {
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 2000)
}

watch(
  () => route.query,
  (query) => {
    if (query.category) {
      selectedCategory.value = decodeURIComponent(query.category as string)
    }
    if (query.keyword) {
      searchKeyword.value = decodeURIComponent(query.keyword as string)
    }
    loadProducts()
  },
  { immediate: true }
)

onMounted(() => {
  loadProducts()
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.product-list-page {
  padding: $spacing-lg 0;
  min-height: calc(100vh - 160px);

  .page-header {
    margin-bottom: $spacing-xl;

    .header-top {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $spacing-lg;

      .page-title {
        font-size: $font-size-xxl;
        font-weight: 700;
        color: $text-primary;
      }

      .product-count {
        font-size: $font-size-sm;
        color: $text-secondary;
        padding: $spacing-xs $spacing-md;
        background: $background-grey;
        border-radius: $border-radius-full;
      }
    }

    .filter-card {
      border-radius: $border-radius-lg;
      padding: $spacing-lg;

      .filter-section {
        .search-box {
          margin-bottom: $spacing-lg;

          .search-input {
            max-width: 400px;
          }

          .search-btn {
            padding: 0 $spacing-lg;
          }
        }

        .filter-row {
          display: flex;
          gap: $spacing-lg;
          flex-wrap: wrap;

          .filter-item {
            .filter-select {
              min-width: 160px;
            }
          }
        }
      }
    }
  }

  .skeleton-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: $spacing-lg;

    .skeleton-card {
      border-radius: $border-radius-lg;
    }

    @media (max-width: $breakpoint-2xl) {
      grid-template-columns: repeat(4, 1fr);
    }

    @media (max-width: $breakpoint-xl) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: repeat(2, 1fr);
      gap: $spacing-md;
    }

    @media (max-width: $breakpoint-sm) {
      grid-template-columns: 1fr;
    }
  }

  .empty-container {
    padding: $spacing-2xl;
  }

  .products-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: $spacing-lg;

    @media (max-width: $breakpoint-2xl) {
      grid-template-columns: repeat(4, 1fr);
    }

    @media (max-width: $breakpoint-xl) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: repeat(2, 1fr);
      gap: $spacing-md;
    }

    @media (max-width: $breakpoint-sm) {
      grid-template-columns: 1fr;
    }
  }

  .toast-notification {
    position: fixed;
    bottom: $spacing-xl;
    left: 50%;
    transform: translateX(-50%);
    background: $success-color;
    color: $text-white;
    padding: $spacing-sm $spacing-lg;
    border-radius: $border-radius-full;
    font-weight: 600;
    box-shadow: $shadow-lg;
    z-index: $z-index-toast;
  }

  .toast-enter-active,
  .toast-leave-active {
    transition: all $transition-normal;
  }

  .toast-enter-from,
  .toast-leave-to {
    opacity: 0;
    transform: translateX(-50%) translateY(20px);
  }
}
</style>
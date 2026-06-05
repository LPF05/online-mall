<template>
  <div class="home-page">
    <section class="hero-section">
      <div class="hero-carousel" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
        <div 
          v-for="(banner, index) in banners" 
          :key="index" 
          class="hero-slide"
          :style="{ background: banner.bg }"
        >
          <div class="container hero-content">
            <div class="hero-text">
              <span class="hero-tag">{{ banner.tag }}</span>
              <h1>{{ banner.title }}</h1>
              <p>{{ banner.desc }}</p>
              <router-link :to="banner.link" class="btn btn-primary hero-btn">
                {{ banner.btn }}
                <span class="arrow">→</span>
              </router-link>
            </div>
            <div class="hero-visual">
              <span class="hero-icon">{{ banner.icon }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="carousel-indicators">
        <button 
          v-for="(_, index) in banners" 
          :key="index"
          :class="{ active: index === currentSlide }"
          @click="goToSlide(index)"
        ></button>
      </div>
    </section>

    <section class="section categories-section">
      <div class="container">
        <div class="section-header">
          <div class="title-wrap">
            <span class="section-badge">热门分类</span>
            <h2>发现好物</h2>
          </div>
          <router-link to="/products" class="view-all">查看全部 →</router-link>
        </div>
        <div class="categories-grid">
          <router-link
            v-for="cat in categories"
            :key="cat.name"
            :to="`/products?category=${encodeURIComponent(cat.name)}`"
            class="category-card"
          >
            <span class="cat-icon">{{ cat.icon }}</span>
            <span class="cat-name">{{ cat.name }}</span>
          </router-link>
        </div>
      </div>
    </section>

    <section class="section products-section">
      <div class="container">
        <div class="section-header">
          <div class="title-wrap">
            <span class="section-badge">热销推荐</span>
            <h2>人气爆款</h2>
          </div>
          <router-link to="/products" class="view-all">查看更多 →</router-link>
        </div>
        <div v-if="hotProducts.length === 0 && !isLoading" class="empty-state">
          <span class="empty-icon">🛒</span>
          <p>暂无热销商品</p>
        </div>
        <div v-else-if="isLoading" class="products-skeleton">
          <div v-for="i in 4" :key="i" class="skeleton-card"></div>
        </div>
        <div v-else class="products-grid">
          <ProductCard 
            v-for="product in hotProducts" 
            :key="product.id" 
            :product="product"
            @added="handleProductAdded"
          />
        </div>
      </div>
    </section>

    <section class="section products-section">
      <div class="container">
        <div class="section-header">
          <div class="title-wrap">
            <span class="section-badge sale-badge">HOT SALE</span>
            <h2>限时特卖</h2>
          </div>
          <router-link to="/products" class="view-all">查看更多 →</router-link>
        </div>
        <div v-if="saleProducts.length === 0" class="empty-state">
          <span class="empty-icon">🔥</span>
          <p>暂无特卖商品</p>
        </div>
        <div v-else class="products-grid sale-grid">
          <router-link
            v-for="product in saleProducts"
            :key="product.id"
            :to="`/products/${product.id}`"
            class="sale-card"
          >
            <div class="sale-card-image">
              <img
                :src="product.imageUrl || `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20clean%20white%20background&image_size=square_hd`"
                :alt="product.name"
                class="product-img"
                loading="lazy"
              />
              <div class="discount-tag">{{ getDiscountLabel(product) }}</div>
            </div>
            <div class="sale-card-content">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-desc">{{ product.description?.length > 36 ? product.description.slice(0, 36) + '...' : product.description }}</p>
              <div class="sale-price-row">
                <span class="sale-price">¥{{ product.price?.toFixed(2) }}</span>
                <span class="original-price">¥{{ product.originalPrice?.toFixed(2) }}</span>
              </div>
              <div class="sale-stats">
                <span>🛒 {{ product.salesCount }} 已售</span>
              </div>
            </div>
          </router-link>
        </div>
      </div>
    </section>

    <section class="section products-section">
      <div class="container">
        <div class="section-header">
          <div class="title-wrap">
            <span class="section-badge">新品上架</span>
            <h2>最新好物</h2>
          </div>
          <router-link to="/products" class="view-all">查看更多 →</router-link>
        </div>
        <div v-if="latestProducts.length === 0" class="empty-state">
          <span class="empty-icon">🎁</span>
          <p>暂无新品</p>
        </div>
        <div v-else class="products-grid">
          <ProductCard
            v-for="product in latestProducts"
            :key="product.id"
            :product="product"
            @added="handleProductAdded"
          />
        </div>
      </div>
    </section>

    <section class="section promo-section">
      <div class="container">
        <div class="promo-card">
          <div class="promo-content">
            <h2>限时特惠</h2>
            <p>精选好物，限时5折起</p>
            <router-link to="/products" class="btn btn-outline">立即抢购</router-link>
          </div>
        </div>
      </div>
    </section>

    <Transition name="toast">
      <div v-if="showToast" class="toast-notification">
        ✓ 已添加到购物车
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { productAPI, type Product } from '@/api/product'
import ProductCard from '@/components/product/ProductCard.vue'

const currentSlide = ref(0)
const hotProducts = ref<Product[]>([])
const newProducts = ref<Product[]>([])
const saleProducts = ref<Product[]>([])
const latestProducts = ref<Product[]>([])
const isLoading = ref(false)
const showToast = ref(false)

const banners = [
  {
    tag: '限时特惠',
    title: '夏日狂欢',
    desc: '精选商品低至5折，错过等一年',
    btn: '立即抢购',
    link: '/products',
    bg: 'linear-gradient(135deg, #FF6B35 0%, #FF8E53 100%)',
    icon: '🏖️'
  },
  {
    tag: '新品首发',
    title: '数码盛宴',
    desc: '前沿科技，抢先体验',
    btn: '探索新品',
    link: '/products',
    bg: 'linear-gradient(135deg, #00B894 0%, #00CEC9 100%)',
    icon: '🚀'
  },
  {
    tag: '品质生活',
    title: '居家好物',
    desc: '品质生活，从这里开始',
    btn: '进入专区',
    link: '/products?category=家居',
    bg: 'linear-gradient(135deg, #6C5CE7 0%, #A29BFE 100%)',
    icon: '🏠'
  }
]

const categories = [
  { name: '电子产品', icon: '📱' },
  { name: '服装', icon: '👕' },
  { name: '食品', icon: '🍔' },
  { name: '家居', icon: '🏠' },
  { name: '美妆', icon: '💄' },
  { name: '图书', icon: '📚' }
]

let carouselTimer: number | undefined

const loadProducts = async () => {
  isLoading.value = true
  try {
    const [hotRes, newRes] = await Promise.all([
      productAPI.getHot('sales_count'),
      productAPI.getList()
    ])
    hotProducts.value = hotRes.slice(0, 4)
    newProducts.value = newRes.slice(0, 4)
  } catch (error) {
    console.error('Failed to load products:', error)
  } finally {
    isLoading.value = false
  }
}

const loadSaleProducts = async () => {
  try {
    saleProducts.value = await productAPI.getSaleProducts(8)
  } catch (error) {
    console.error('Failed to load sale products:', error)
  }
}

const loadLatestProducts = async () => {
  try {
    latestProducts.value = await productAPI.getLatestProducts(8)
  } catch (error) {
    console.error('Failed to load latest products:', error)
  }
}

const getDiscountLabel = (product: Product) => {
  if (product.originalPrice && product.originalPrice > 0) {
    const discount = Math.round((product.price / product.originalPrice) * 10)
    return discount + '折'
  }
  return ''
}

const goToSlide = (index: number) => {
  currentSlide.value = index
}

const handleProductAdded = () => {
  showToast.value = true
  setTimeout(() => {
    showToast.value = false
  }, 2000)
}

onMounted(() => {
  loadProducts()
  loadSaleProducts()
  loadLatestProducts()
  carouselTimer = window.setInterval(() => {
    currentSlide.value = (currentSlide.value + 1) % banners.length
  }, 5000)
})

onUnmounted(() => {
  if (carouselTimer) {
    clearInterval(carouselTimer)
  }
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.home-page {
  width: 100%;
  min-height: 100vh;
  background: $background-grey;
}

.hero-section {
  position: relative;
  height: 500px;
  overflow: hidden;

  .hero-carousel {
    display: flex;
    height: 100%;
    transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);
  }

  .hero-slide {
    min-width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    position: relative;

    .hero-content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 100%;
      padding: $spacing-xl 0;

      @media (max-width: $breakpoint-md) {
        flex-direction: column;
        text-align: center;
        padding: $spacing-lg;
      }
    }

    .hero-text {
      flex: 1;
      max-width: 550px;
      color: $text-white;

      .hero-tag {
        display: inline-block;
        padding: 6px 16px;
        background: rgba(255, 255, 255, 0.2);
        backdrop-filter: blur(10px);
        border-radius: $border-radius-full;
        font-size: $font-size-sm;
        font-weight: 600;
        margin-bottom: $spacing-lg;
      }

      h1 {
        font-size: $font-size-3xl;
        font-weight: 800;
        line-height: 1.15;
        margin-bottom: $spacing-md;
        letter-spacing: -0.5px;

        @media (max-width: $breakpoint-md) {
          font-size: $font-size-2xl;
        }
      }

      p {
        font-size: $font-size-lg;
        opacity: 0.95;
        margin-bottom: $spacing-xl;
        line-height: 1.6;
      }

      .hero-btn {
        padding: $spacing-md $spacing-xl;
        font-size: $font-size-base;
        box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);

        .arrow {
          transition: transform $transition-fast;
        }

        &:hover .arrow {
          transform: translateX(4px);
        }
      }
    }

    .hero-visual {
      flex: 1;
      display: flex;
      justify-content: flex-end;
      padding-left: $spacing-xl;

      .hero-icon {
        font-size: 200px;
        opacity: 0.4;
        animation: float 3s ease-in-out infinite;

        @media (max-width: $breakpoint-md) {
          font-size: 120px;
          margin-top: $spacing-lg;
        }
      }
    }
  }

  .carousel-indicators {
    position: absolute;
    bottom: $spacing-lg;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: $spacing-sm;
    z-index: 10;

    button {
      width: 10px;
      height: 10px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.4);
      border: none;
      cursor: pointer;
      transition: all $transition-fast;

      &.active {
        width: 32px;
        border-radius: $border-radius-full;
        background: $text-white;
      }

      &:hover {
        background: $text-white;
      }
    }
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

.categories-section {
  background: $background-color;

  .categories-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    gap: $spacing-lg;

    @media (max-width: $breakpoint-lg) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: repeat(2, 1fr);
      gap: $spacing-md;
    }

    .category-card {
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: $spacing-md;
      padding: $spacing-xl $spacing-md;
      background: $background-grey;
      border-radius: $border-radius-lg;
      transition: all $transition-normal;
      text-decoration: none;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-6px);
        box-shadow: $shadow-md;
        background: $background-color;
        border-color: rgba($primary-color, 0.3);
      }

      .cat-icon {
        font-size: 48px;
        transition: transform $transition-fast;
      }

      &:hover .cat-icon {
        transform: scale(1.15) rotate(5deg);
      }

      .cat-name {
        font-size: $font-size-sm;
        font-weight: 600;
        color: $text-primary;
        transition: color $transition-fast;
      }

      &:hover .cat-name {
        color: $primary-color;
      }
    }
  }
}

.products-section {
  .sale-badge {
    background: linear-gradient(135deg, #FF4757 0%, #FF6B81 100%);
  }

  .sale-grid {
    .sale-card {
      display: flex;
      flex-direction: column;
      background: $background-card;
      border-radius: $border-radius-lg;
      overflow: hidden;
      text-decoration: none;
      transition: all $transition-normal;
      box-shadow: $shadow-sm;
      border: 2px solid transparent;

      &:hover {
        transform: translateY(-6px);
        box-shadow: $shadow-lg;
        border-color: #FF4757;

        .product-img {
          transform: scale(1.05);
        }
      }

      .sale-card-image {
        position: relative;
        width: 100%;
        padding-top: 100%;
        overflow: hidden;
        background: linear-gradient(135deg, #fff5f5 0%, #ffe8e8 100%);

        .product-img {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
        }

        .discount-tag {
          position: absolute;
          top: $spacing-sm;
          right: $spacing-sm;
          padding: 4px 12px;
          background: linear-gradient(135deg, #FF4757 0%, #FF6B81 100%);
          color: $text-white;
          font-size: $font-size-xs;
          font-weight: 700;
          border-radius: $border-radius-full;
          z-index: 2;
          box-shadow: 0 2px 8px rgba(255, 71, 87, 0.4);
        }
      }

      .sale-card-content {
        padding: $spacing-md;
        display: flex;
        flex-direction: column;
        flex: 1;

        .product-name {
          font-size: $font-size-base;
          font-weight: 700;
          color: $text-primary;
          margin: 0 0 $spacing-xs 0;
          line-height: 1.3;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .product-desc {
          font-size: $font-size-sm;
          color: $text-secondary;
          margin: 0 0 $spacing-sm 0;
          line-height: 1.4;
          flex: 1;
        }

        .sale-price-row {
          display: flex;
          align-items: baseline;
          gap: $spacing-sm;
          flex-wrap: wrap;
          margin-bottom: $spacing-xs;

          .sale-price {
            font-size: $font-size-xl;
            font-weight: 800;
            color: #FF4757;
            letter-spacing: -0.5px;
          }

          .original-price {
            font-size: $font-size-sm;
            color: $text-muted;
            text-decoration: line-through;
          }
        }

        .sale-stats {
          font-size: $font-size-xs;
          color: $text-muted;
        }
      }
    }
  }

  .empty-state {
    text-align: center;
    padding: $spacing-2xl;

    .empty-icon {
      font-size: 48px;
      margin-bottom: $spacing-md;
    }

    p {
      color: $text-muted;
      font-size: $font-size-base;
    }
  }

  .products-grid {
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

  .products-skeleton {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: $spacing-lg;

    .skeleton-card {
      height: 360px;
      border-radius: $border-radius-lg;
    }

    @media (max-width: $breakpoint-xl) {
      grid-template-columns: repeat(3, 1fr);
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}

.promo-section {
  .promo-card {
    padding: $spacing-2xl;
    border-radius: $border-radius-xl;
    background: linear-gradient(135deg, $secondary-color 0%, #1A1D21 100%);
    color: $text-white;
    text-align: center;
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle, rgba(255, 107, 53, 0.15) 0%, transparent 70%);
    }

    .promo-content {
      position: relative;
      z-index: 1;

      h2 {
        font-size: $font-size-2xl;
        font-weight: 700;
        margin-bottom: $spacing-sm;

        @media (max-width: $breakpoint-md) {
          font-size: $font-size-xl;
        }
      }

      p {
        font-size: $font-size-base;
        opacity: 0.8;
        margin-bottom: $spacing-xl;
      }

      .btn-outline {
        border-color: rgba(255, 255, 255, 0.4);
        color: $text-white;

        &:hover {
          background: $text-white;
          color: $secondary-color;
        }
      }
    }
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
</style>
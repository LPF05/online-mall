<template>
  <router-link :to="`/products/${product.id}`" class="product-card">
    <div class="card-image">
      <img 
        :src="getProductImage(product)" 
        :alt="product.name"
        class="product-img"
        loading="lazy"
      />
      <div v-if="product.salesCount > 100" class="hot-tag">🔥 热销</div>
      <button class="add-cart-btn" @click.stop="handleAddToCart">
        <span>+</span>
      </button>
    </div>
    <div class="card-content">
      <span class="category">{{ product.category }}</span>
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-desc">{{ truncateDesc(product.description) }}</p>
      <div class="card-footer">
        <div class="price-row">
          <div class="price-section">
            <div class="price">
              <span class="currency" :class="{ 'sale-currency': product.isOnSale === 1 && product.originalPrice && product.originalPrice > product.price }">¥</span>
              <span class="amount" :class="{ 'sale-amount': product.isOnSale === 1 && product.originalPrice && product.originalPrice > product.price }">{{ formatPrice(product.price) }}</span>
            </div>
          </div>
          <div class="stats">
            <span class="stat-item">👁 {{ product.viewCount }}</span>
            <span class="stat-item">🛒 {{ product.salesCount }}</span>
          </div>
        </div>
        <div v-if="product.isOnSale === 1 && product.originalPrice && product.originalPrice > product.price" class="original-price-line">
          <span class="original-price">¥{{ formatPrice(product.originalPrice) }}</span>
          <span class="discount-badge">{{ getDiscount(product) }}折</span>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import type { Product } from '@/api/product'

const props = defineProps<{
  product: Product
}>()

const emit = defineEmits<{
  (e: 'added'): void
}>()

const authStore = useAuthStore()
const cartStore = useCartStore()

const getProductImage = (product: Product) => {
  if (!product.imageUrl || product.imageUrl === '') {
    return `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=product%20photo%20clean%20white%20background&image_size=square_hd`
  }
  return product.imageUrl
}

const truncateDesc = (text: string) => {
  return text.length > 36 ? text.slice(0, 36) + '...' : text
}

const formatPrice = (price: number) => {
  return price.toFixed(2)
}

const getDiscount = (product: Product) => {
  if (!product.originalPrice || product.originalPrice === 0) return ''
  const discount = Math.round((product.price / product.originalPrice) * 10 * 10) / 10
  return discount
}

const handleAddToCart = async () => {
  if (!authStore.isAuthenticated) {
    window.location.href = '/login'
    return
  }
  const success = await cartStore.addToCart(props.product.id, 1)
  if (success) {
    emit('added')
  }
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.product-card {
  display: flex;
  flex-direction: column;
  background: $background-card;
  border-radius: $border-radius-lg;
  overflow: hidden;
  text-decoration: none;
  transition: all $transition-normal;
  box-shadow: $shadow-sm;

  &:hover {
    transform: translateY(-6px);
    box-shadow: $shadow-lg;

    .add-cart-btn {
      opacity: 1;
      transform: translateY(0);
    }

    .product-img {
      transform: scale(1.05);
    }
  }

  .card-image {
    position: relative;
    width: 100%;
    padding-top: 100%;
    overflow: hidden;
    background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%);

    .product-img {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
    }

    .hot-tag {
      position: absolute;
      top: $spacing-sm;
      left: $spacing-sm;
      padding: 4px 12px;
      background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
      color: $text-white;
      font-size: $font-size-xs;
      font-weight: 600;
      border-radius: $border-radius-full;
      z-index: 2;
    }

    .add-cart-btn {
      position: absolute;
      bottom: $spacing-sm;
      left: $spacing-sm;
      right: $spacing-sm;
      padding: $spacing-sm;
      background: linear-gradient(135deg, $primary-color 0%, $primary-dark 100%);
      color: $text-white;
      border: none;
      border-radius: $border-radius-md;
      font-size: $font-size-xl;
      font-weight: 300;
      cursor: pointer;
      opacity: 0;
      transform: translateY(8px);
      transition: all $transition-fast;
      box-shadow: 0 4px 16px rgba(255, 107, 53, 0.4);

      &:hover {
        transform: translateY(0) scale(1.02);
        box-shadow: 0 6px 24px rgba(255, 107, 53, 0.5);
      }
    }
  }

  .card-content {
    padding: $spacing-md;
    display: flex;
    flex-direction: column;
    flex: 1;

    .category {
      font-size: $font-size-xs;
      color: $primary-color;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.5px;
      margin-bottom: $spacing-xs;
    }

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
      margin: 0 0 $spacing-md 0;
      line-height: 1.4;
      flex: 1;
    }

    .card-footer {
      display: flex;
      flex-direction: column;
      gap: 6px;
      padding-top: $spacing-sm;
      border-top: 1px solid $border-light;

      .price-row {
        display: flex;
        justify-content: space-between;
        align-items: flex-end;

        .price-section {
          flex: 1;
          min-width: 0;

          .price {
            display: flex;
            align-items: baseline;
            gap: 2px;

            .currency {
              font-size: $font-size-sm;
              font-weight: 600;

              &.sale-currency {
                color: $danger-color;
              }
            }

            .amount {
              font-size: $font-size-xl;
              font-weight: 800;
              color: $primary-color;
              letter-spacing: -0.5px;

              &.sale-amount {
                color: $danger-color;
              }
            }
          }
        }

        .stats {
          display: flex;
          gap: $spacing-sm;
          font-size: $font-size-xs;
          color: $text-muted;
          flex-shrink: 0;
          margin-left: $spacing-sm;

          .stat-item {
            display: flex;
            align-items: center;
            gap: 2px;
            white-space: nowrap;
          }
        }
      }

      .original-price-line {
        display: flex;
        align-items: center;
        gap: 6px;

        .original-price {
          font-size: $font-size-xs;
          color: $text-muted;
          text-decoration: line-through;
        }

        .discount-badge {
          font-size: 10px;
          color: $danger-color;
          background: rgba($danger-color, 0.1);
          padding: 1px 6px;
          border-radius: $border-radius-full;
          font-weight: 600;
          white-space: nowrap;
        }
      }
    }
  }
}
</style>
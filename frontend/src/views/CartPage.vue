<template>
  <div class="cart-page">
    <div class="container">
      <h1 class="page-title">购物车</h1>

      <el-empty v-if="!authStore.isAuthenticated" description="请先登录后再查看购物车">
        <router-link to="/login" class="btn btn-primary">去登录</router-link>
      </el-empty>

      <el-card v-else-if="cartStore.isLoading" class="loading-card">
        <el-spin :size="36" />
      </el-card>

      <el-empty v-else-if="cartStore.items.length === 0" description="购物车是空的">
        <router-link to="/products" class="btn btn-primary">去购物</router-link>
      </el-empty>

      <div v-else class="cart-content">
        <el-card class="cart-items-card" shadow="hover">
          <div 
            v-for="item in cartStore.items" 
            :key="item.productId" 
            class="cart-item"
          >
            <div class="item-image">
              <img :src="item.imageUrl" :alt="item.productName" />
            </div>
            <div class="item-info">
              <h3>{{ item.productName }}</h3>
              <p class="item-price">¥{{ item.price.toFixed(2) }}</p>
            </div>
            <div class="item-quantity">
              <el-button 
                size="small" 
                @click="handleUpdateQuantity(item.productId, item.quantity - 1)" 
                :disabled="item.quantity <= 1"
              >
                -
              </el-button>
              <span class="quantity-value">{{ item.quantity }}</span>
              <el-button 
                size="small" 
                type="primary" 
                @click="handleUpdateQuantity(item.productId, item.quantity + 1)"
              >
                +
              </el-button>
            </div>
            <div class="item-subtotal">
              ¥{{ (item.price * item.quantity).toFixed(2) }}
            </div>
            <el-button 
              size="small" 
              type="text" 
              text-color="#f56c6c"
              @click="handleRemoveItem(item.productId)"
            >
              删除
            </el-button>
          </div>
        </el-card>

        <el-card class="cart-summary-card" shadow="hover">
          <div class="summary-info">
            <p>共 {{ cartStore.itemCount }} 件商品</p>
            <p class="total-price">总计：¥{{ cartStore.totalPrice.toFixed(2) }}</p>
          </div>
          <el-button 
            type="primary" 
            size="large"
            class="checkout-btn"
            @click="handleCheckout"
          >
            去结算
          </el-button>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useCartStore } from '@/stores/cart'
import { orderAPI } from '@/api/order'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const cartStore = useCartStore()

const handleUpdateQuantity = async (productId: number, quantity: number) => {
  if (quantity < 1) return
  await cartStore.updateQuantity(productId, quantity)
}

const handleRemoveItem = async (productId: number) => {
  await cartStore.removeItem(productId)
  ElMessage.success('删除成功')
}

const handleCheckout = async () => {
  if (cartStore.items.length === 0) {
    ElMessage.warning('购物车为空')
    return
  }
  try {
    const data = await orderAPI.createOrder()
    ElMessage.success('订单创建成功！')
    cartStore.clearCart()
    router.push(`/orders/${data.orderNo}`)
  } catch (error) {
    ElMessage.error('创建订单失败：' + (error as Error).message)
  }
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    cartStore.loadCart()
  }
})
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.cart-page {
  padding: $spacing-lg 0;
  min-height: 60vh;

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

  .cart-content {
    display: flex;
    flex-direction: column;
    gap: $spacing-lg;
  }

  .cart-items-card {
    padding: $spacing-lg;
  }

  .cart-item {
    display: grid;
    grid-template-columns: 100px 1fr auto auto auto;
    gap: $spacing-lg;
    align-items: center;
    padding: $spacing-md 0;
    border-bottom: 1px solid $border-light;

    &:last-child {
      border-bottom: none;
    }

    @media (max-width: $breakpoint-md) {
      grid-template-columns: 80px 1fr;
      gap: $spacing-md;
    }
  }

  .item-image {
    width: 100px;
    height: 100px;
    border-radius: $border-radius-sm;
    overflow: hidden;
    background: $background-color;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }
  }

  .item-info {
    h3 {
      font-size: $font-size-base;
      font-weight: 600;
      margin-bottom: $spacing-xs;
    }

    .item-price {
      color: $text-secondary;
    }
  }

  .item-quantity {
    display: flex;
    align-items: center;
    gap: 0;

    .quantity-value {
      padding: 0 $spacing-md;
    }
  }

  .item-subtotal {
    font-size: $font-size-lg;
    font-weight: 700;
    color: $primary-color;
  }

  .cart-summary-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: $spacing-lg;

    .summary-info {
      p {
        color: $text-secondary;
        margin-bottom: $spacing-xs;
      }

      .total-price {
        font-size: $font-size-xl;
        font-weight: 700;
        color: $primary-color;
      }
    }

    .checkout-btn {
      padding: $spacing-md $spacing-xl;
      font-size: $font-size-lg;
    }
  }
}
</style>
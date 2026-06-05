<template>
  <header class="app-header">
    <div class="header-container">
      <div class="logo">
        <router-link to="/"><span class="logo-icon">🛍️</span> Online Mall</router-link>
      </div>
      
      <nav class="nav-links">
        <router-link to="/" class="nav-item">首页</router-link>
        <router-link to="/products" class="nav-item">商品</router-link>
        <router-link to="/cart" class="nav-item">
          <span>购物车</span>
          <Transition name="badge">
            <span v-if="cartStore.itemCount > 0" class="cart-badge">
              {{ cartStore.itemCount }}
            </span>
          </Transition>
        </router-link>
        <template v-if="authStore.isAuthenticated">
          <router-link to="/profile" class="nav-item">
            <span>👤</span>
            <span>个人中心</span>
          </router-link>
          <router-link v-if="userStore.user?.role === 'ADMIN'" to="/admin" class="nav-item">
            <span>🛠️</span>
            <span>管理员</span>
          </router-link>
          <button @click="handleLogout" class="nav-item logout-btn">
            <span>退出</span>
          </button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-item">登录</router-link>
          <router-link to="/register" class="nav-item register-btn">注册</router-link>
        </template>
      </nav>

      <button class="mobile-menu-btn" @click="isMobileMenuOpen = !isMobileMenuOpen">
        <span class="menu-icon">{{ isMobileMenuOpen ? '✕' : '☰' }}</span>
      </button>
    </div>

    <Transition name="slide-down">
      <div v-if="isMobileMenuOpen" class="mobile-menu">
        <router-link to="/" class="mobile-nav-item" @click="isMobileMenuOpen = false">首页</router-link>
        <router-link to="/products" class="mobile-nav-item" @click="isMobileMenuOpen = false">商品</router-link>
        <router-link to="/cart" class="mobile-nav-item" @click="isMobileMenuOpen = false">购物车</router-link>
        <template v-if="authStore.isAuthenticated">
          <router-link to="/profile" class="mobile-nav-item" @click="isMobileMenuOpen = false">个人中心</router-link>
          <router-link v-if="userStore.user?.role === 'ADMIN'" to="/admin" class="mobile-nav-item" @click="isMobileMenuOpen = false">管理员</router-link>
          <button @click="handleLogoutMobile" class="mobile-nav-item">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="mobile-nav-item" @click="isMobileMenuOpen = false">登录</router-link>
          <router-link to="/register" class="mobile-nav-item" @click="isMobileMenuOpen = false">注册</router-link>
        </template>
      </div>
    </Transition>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'

const router = useRouter()
const authStore = useAuthStore()
const userStore = useUserStore()
const cartStore = useCartStore()

const isMobileMenuOpen = ref(false)

const handleLogout = () => {
  authStore.logout()
  userStore.logout()
}

const handleLogoutMobile = () => {
  isMobileMenuOpen.value = false
  handleLogout()
}

onMounted(() => {
  if (authStore.isAuthenticated) {
    userStore.fetchUser()
    cartStore.loadCart()
  }
})
</script>

<style scoped>
.app-header {
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.logo a {
  color: #409eff;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 6px;
}

.logo-icon {
  font-size: 28px;
  display: inline-block;
  animation: logo-bounce 2s ease-in-out infinite;
}

@keyframes logo-bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

.nav-links {
  display: flex;
  gap: 24px;
  align-items: center;
}

.nav-item {
  color: #333;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s ease, border-bottom 0.3s ease;
  display: flex;
  align-items: center;
  gap: 4px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  position: relative;
  border-bottom: 2px solid transparent;
}

.nav-item:hover {
  color: #409eff;
  background: #f5f7fa;
  border-bottom-color: rgba(64, 158, 255, 0.3);
}

.nav-item.active,
.nav-item.router-link-active,
.nav-item.router-link-exact-active {
  color: #409eff;
  font-weight: 600;
  background: rgba(64, 158, 255, 0.1);
  border-bottom: 2px solid #409eff;
  animation: border-slide-in 0.3s ease;
}

@keyframes border-slide-in {
  from { border-bottom-width: 0; }
  to { border-bottom-width: 2px; }
}

.register-btn {
  background: #409eff;
  color: white;
}

.register-btn:hover {
  background: #66b1ff;
  color: white;
}

.logout-btn {
  color: #f56c6c;
}

.logout-btn:hover {
  color: #f78989;
  background: #fef0f0;
}

.cart-badge {
  background: #f56c6c;
  color: white;
  font-size: 12px;
  padding: 2px 8px;
  border-radius: 10px;
  margin-left: 4px;
  animation: badge-pulse 1.5s ease-in-out infinite;
}

@keyframes badge-pulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.15); }
}

.badge-enter-active,
.badge-leave-active {
  transition: all 0.3s ease;
}

.badge-enter-from,
.badge-leave-to {
  opacity: 0;
  transform: scale(0);
}

.mobile-menu-btn {
  display: none;
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.menu-icon {
  display: block;
}

.mobile-menu {
  background: white;
  border-top: 1px solid #eee;
}

.mobile-nav-item {
  display: block;
  padding: 12px 20px;
  color: #333;
  text-decoration: none;
  border-bottom: 1px solid #f5f5f5;
  background: none;
  border: none;
  width: 100%;
  text-align: left;
  font-size: 14px;
}

.mobile-nav-item:hover {
  background: #f5f7fa;
}

.slide-down-enter-active,
.slide-down-leave-active {
  transition: all 0.3s ease;
}

.slide-down-enter-from,
.slide-down-leave-to {
  opacity: 0;
  max-height: 0;
}

@media (max-width: 768px) {
  .nav-links {
    display: none;
  }
  
  .mobile-menu-btn {
    display: block;
  }
}
</style>

<template>
  <div class="login-page">
    <div class="login-container">
      <el-card class="login-card" shadow="none">
        <div class="login-header">
          <div class="logo">
            <span class="logo-icon">🛒</span>
            <span class="logo-text">在线商城</span>
          </div>
          <h1>欢迎回来</h1>
          <p>登录您的账户，开始购物之旅</p>
        </div>

        <el-form :model="form" :rules="rules" ref="loginForm" class="login-form">
          <el-form-item prop="email">
            <el-input 
              v-model="form.email" 
              type="email" 
              placeholder="请输入邮箱地址" 
              prefix-icon="📧"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input 
              v-model="form.password" 
              :type="showPassword ? 'text' : 'password'" 
              placeholder="请输入密码" 
              prefix-icon="🔐"
              :suffix-icon="showPassword ? '🙈' : '👁️'"
              @click:suffix-icon="showPassword = !showPassword"
            />
          </el-form-item>

          <el-form-item>
            <div class="form-options">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <a href="#" class="forgot-password">忘记密码?</a>
            </div>
          </el-form-item>

          <el-form-item>
            <el-button 
              type="primary" 
              class="login-btn" 
              :loading="isLoading"
              @click="handleSubmit"
            >
              {{ isLoading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="login-footer">
          <p>还没有账户? <router-link to="/register">立即注册</router-link></p>
        </div>
      </el-card>

      <div class="login-visual">
        <div class="visual-content">
          <span class="visual-icon">🎁</span>
          <h2>新用户专享</h2>
          <p>注册即送100元优惠券</p>
          <div class="feature-list">
            <span>✅ 正品保证</span>
            <span>✅ 7天无理由退换</span>
            <span>✅ 极速发货</span>
            <span>✅ 专属客服</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()
const loginForm = ref()

const form = reactive({
  email: '',
  password: ''
})

const rules = {
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ]
}

const showPassword = ref(false)
const rememberMe = ref(false)
const isLoading = ref(false)

const handleSubmit = async () => {
  if (!loginForm.value) return
  
  loginForm.value.validate((valid: boolean) => {
    if (!valid) return

    isLoading.value = true
    authStore.login(form.email, form.password)
      .then(() => {
        ElMessage.success('登录成功！')
      })
      .catch((error) => {
        ElMessage.error((error as Error).message || '登录失败')
      })
      .finally(() => {
        isLoading.value = false
      })
  })
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, $primary-color 0%, $primary-dark 50%, $secondary-color 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-xl;
}

.login-container {
  display: flex;
  width: 100%;
  max-width: 900px;
  background: $background-color;
  border-radius: $border-radius-xl;
  box-shadow: $shadow-lg;
  overflow: hidden;

  @media (max-width: $breakpoint-md) {
    flex-direction: column;
    max-width: 400px;
  }
}

.login-card {
  flex: 1;
  padding: $spacing-2xl;
  border: none;

  .login-header {
    text-align: center;
    margin-bottom: $spacing-2xl;

    .logo {
      display: flex;
      align-items: center;
      justify-content: center;
      gap: $spacing-sm;
      font-size: $font-size-xl;
      font-weight: 700;
      color: $primary-color;
      margin-bottom: $spacing-lg;
    }

    .logo-icon {
      font-size: 40px;
    }

    h1 {
      font-size: $font-size-xxl;
      font-weight: 700;
      color: $text-primary;
      margin-bottom: $spacing-xs;
    }

    p {
      color: $text-secondary;
      font-size: $font-size-base;
    }
  }

  .login-form {
    .login-btn {
      width: 100%;
      height: 48px;
      font-size: $font-size-base;
    }
  }

  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .forgot-password {
      color: $primary-color;
      font-size: $font-size-sm;
    }
  }

  .login-footer {
    text-align: center;
    margin-top: $spacing-xl;
    padding-top: $spacing-xl;
    border-top: 1px solid $border-light;

    p {
      color: $text-secondary;
      font-size: $font-size-sm;

      a {
        color: $primary-color;
        font-weight: 600;
      }
    }
  }
}

.login-visual {
  flex: 1;
  background: linear-gradient(135deg, $secondary-color 0%, #1A1D21 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-2xl;

  .visual-content {
    text-align: center;
    color: $text-white;

    .visual-icon {
      font-size: 80px;
      margin-bottom: $spacing-lg;
      animation: float 3s ease-in-out infinite;
    }

    h2 {
      font-size: $font-size-2xl;
      font-weight: 700;
      margin-bottom: $spacing-sm;
    }

    p {
      font-size: $font-size-base;
      opacity: 0.9;
      margin-bottom: $spacing-xl;
    }

    .feature-list {
      display: flex;
      flex-direction: column;
      gap: $spacing-md;

      span {
        font-size: $font-size-sm;
        opacity: 0.95;
      }
    }
  }

  @media (max-width: $breakpoint-md) {
    display: none;
  }
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
</style>
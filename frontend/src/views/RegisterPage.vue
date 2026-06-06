<template>
  <div class="register-page">
    <div class="register-container">
      <el-card class="register-card" shadow="hover">
        <div class="register-header">
          <div class="logo">
            <span class="logo-icon">🛒</span>
            <span class="logo-text">在线商城</span>
          </div>
          <h1 class="register-title">注册账户</h1>
          <p>创建账户，开启购物之旅</p>
        </div>

        <el-form :model="form" :rules="rules" ref="registerForm" class="register-form">
          <el-form-item prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名" 
              prefix-icon="👤"
            />
          </el-form-item>

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
              type="password" 
              placeholder="请输入密码（至少6位）" 
              prefix-icon="🔐"
            />
          </el-form-item>

          <el-form-item prop="confirmPassword">
            <el-input 
              v-model="form.confirmPassword" 
              type="password" 
              placeholder="请再次输入密码" 
              prefix-icon="🔑"
            />
          </el-form-item>

          <el-form-item>
            <el-button 
              type="primary" 
              class="register-btn" 
              :loading="isLoading"
              @click="handleRegister"
            >
              {{ isLoading ? '注册中...' : '注册' }}
            </el-button>
          </el-form-item>
        </el-form>

        <div class="register-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const registerForm = ref()

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度在2到20个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少为6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { 
      validator: (_rule: any, value: string, callback: any) => {
        if (value !== form.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const isLoading = ref(false)

const handleRegister = async () => {
  if (!registerForm.value) return
  
  registerForm.value.validate((valid: boolean) => {
    if (!valid) return

    isLoading.value = true
    authStore.register(form.username, form.email, form.password)
      .then(() => {
        ElMessage.success('注册成功！请登录')
        form.username = ''
        form.email = ''
        form.password = ''
        form.confirmPassword = ''
      })
      .catch((error) => {
        ElMessage.error((error as Error).message || '注册失败')
      })
      .finally(() => {
        isLoading.value = false
      })
  })
}
</script>

<style lang="scss" scoped>
@import '@/assets/styles/variables.scss';

.register-page {
  min-height: calc(100vh - 160px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: $spacing-xl;
  background: linear-gradient(135deg, $background-grey 0%, rgba($primary-color, 0.05) 100%);
}

.register-container {
  width: 100%;
  max-width: 450px;
}

.register-card {
  padding: $spacing-2xl;
  border-radius: $border-radius-lg;

  .register-header {
    text-align: center;
    margin-bottom: $spacing-xl;

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

    .register-title {
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

  .register-form {
    .register-btn {
      width: 100%;
      height: 48px;
      font-size: $font-size-base;
    }
  }

  .register-footer {
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
</style>
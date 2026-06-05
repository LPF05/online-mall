import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const isAuthenticated = computed(() => !!token.value)

  const login = async (email: string, password: string) => {
    try {
      const data = await authAPI.login({ email, password })
      const receivedToken = data.token
      token.value = receivedToken
      localStorage.setItem('token', receivedToken)
      // Fetch user info after login to populate role for route guard
      const userStore = useUserStore()
      await userStore.fetchUser()
      router.push('/')
      return true
    } catch (error) {
      console.error('Login failed:', error)
      return false
    }
  }

  const register = async (username: string, email: string, password: string) => {
    try {
      await authAPI.register({ username, email, password })
      router.push('/login')
      return true
    } catch (error) {
      console.error('Register failed:', error)
      return false
    }
  }

  const logout = () => {
    token.value = null
    localStorage.removeItem('token')
    router.push('/login')
  }

  return {
    token,
    isAuthenticated,
    login,
    register,
    logout
  }
})

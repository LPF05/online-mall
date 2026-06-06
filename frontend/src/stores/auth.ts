import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authAPI } from '@/api/auth'
import { useUserStore } from '@/stores/user'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string | null>(localStorage.getItem('token'))
  const isAuthenticated = computed(() => !!token.value)

  const login = async (email: string, password: string) => {
    const data = await authAPI.login({ email, password })
    const receivedToken = data.token
    token.value = receivedToken
    localStorage.setItem('token', receivedToken)
    // Fetch user info after login to populate role for route guard
    const userStore = useUserStore()
    await userStore.fetchUser()
    router.push('/')
  }

  const register = async (username: string, email: string, password: string) => {
    await authAPI.register({ username, email, password })
    router.push('/login')
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

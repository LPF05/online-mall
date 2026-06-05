import { defineStore } from 'pinia'
import { ref } from 'vue'
import userApi from '@/api/user'

interface UserInfo {
  id: number
  username: string
  email: string
  phone?: string
  balance: number
  role: string
  createdAt: string
}

export const useUserStore = defineStore('user', () => {
  const user = ref<UserInfo | null>(null)
  const isLoading = ref(false)

  const fetchUser = async () => {
    isLoading.value = true
    try {
      const data = await userApi.getProfile()
      user.value = data
      localStorage.setItem('user', JSON.stringify(data))
    } catch (error) {
      console.error('Failed to fetch user info:', error)
      user.value = null
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    window.location.href = '/login'
  }

  return {
    user,
    isLoading,
    fetchUser,
    logout
  }
})

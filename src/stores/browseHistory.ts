import { defineStore } from 'pinia'
import { ref } from 'vue'
import { browseHistoryAPI } from '@/api/browseHistory'

export interface BrowseHistoryItem {
  id: number
  userId: number
  productId: number
  productName: string
  price: number
  imageUrl: string
  createdAt: string
}

export const useBrowseHistoryStore = defineStore('browseHistory', () => {
  const history = ref<BrowseHistoryItem[]>([])
  const isLoading = ref(false)

  const loadHistory = async () => {
    isLoading.value = true
    try {
      const data = await browseHistoryAPI.getHistory()
      history.value = data || []
    } catch (error) {
      console.error('Failed to load browse history:', error)
      history.value = []
    } finally {
      isLoading.value = false
    }
  }

  const addHistory = async (productId: number) => {
    try {
      await browseHistoryAPI.addHistory(productId)
    } catch (error) {
      console.error('Failed to add browse history:', error)
    }
  }

  const clearHistory = async () => {
    try {
      await browseHistoryAPI.clearHistory()
      history.value = []
      return true
    } catch (error) {
      console.error('Failed to clear browse history:', error)
      return false
    }
  }

  return {
    history,
    isLoading,
    loadHistory,
    addHistory,
    clearHistory
  }
})
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { favoriteAPI, type FavoriteItem } from '@/api/favorite'

export const useFavoriteStore = defineStore('favorite', () => {
  const favorites = ref<FavoriteItem[]>([])
  const isLoading = ref(false)

  const loadFavorites = async () => {
    isLoading.value = true
    try {
      const data = await favoriteAPI.getFavorites()
      favorites.value = data || []
    } catch (error) {
      console.error('Failed to load favorites:', error)
      favorites.value = []
    } finally {
      isLoading.value = false
    }
  }

  const addFavorite = async (productId: number) => {
    try {
      await favoriteAPI.addFavorite(productId)
      await loadFavorites()
      return true
    } catch (error) {
      console.error('Failed to add favorite:', error)
      return false
    }
  }

  const removeFavorite = async (productId: number) => {
    try {
      await favoriteAPI.removeFavorite(productId)
      await loadFavorites()
      return true
    } catch (error) {
      console.error('Failed to remove favorite:', error)
      return false
    }
  }

  const isFavorite = (productId: number) => {
    return favorites.value.some(f => f.productId === productId)
  }

  return {
    favorites,
    isLoading,
    loadFavorites,
    addFavorite,
    removeFavorite,
    isFavorite
  }
})
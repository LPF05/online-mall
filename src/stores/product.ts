import { defineStore } from 'pinia'
import { ref } from 'vue'
import { productAPI, type Product } from '@/api/product'

export const useProductStore = defineStore('product', () => {
  const products = ref<Product[]>([])
  const isLoading = ref(false)

  const loadProducts = async () => {
    isLoading.value = true
    try {
      const response = await productAPI.getList()
      products.value = response || []
    } catch (error) {
      console.error('Failed to load products:', error)
      products.value = []
    } finally {
      isLoading.value = false
    }
  }

  return {
    products,
    isLoading,
    loadProducts
  }
})
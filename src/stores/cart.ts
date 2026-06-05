import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { cartAPI, type CartItem } from '@/api/cart'

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])
  const isLoading = ref(false)

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  const itemCount = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const loadCart = async () => {
    isLoading.value = true
    try {
      const data = await cartAPI.getCart()
      if (data && data.items) {
        const itemsStr = typeof data.items === 'string' ? data.items : JSON.stringify(data.items)
        items.value = JSON.parse(itemsStr)
      } else {
        items.value = []
      }
    } catch (error) {
      console.error('Failed to load cart:', error)
      items.value = []
    } finally {
      isLoading.value = false
    }
  }

  const addToCart = async (productId: number, quantity: number = 1) => {
    try {
      await cartAPI.addItem(productId, quantity)
      await loadCart()
      return true
    } catch (error) {
      console.error('Failed to add to cart:', error)
      return false
    }
  }

  const updateQuantity = async (productId: number, quantity: number) => {
    try {
      await cartAPI.updateQuantity(productId, quantity)
      await loadCart()
      return true
    } catch (error) {
      console.error('Failed to update quantity:', error)
      return false
    }
  }

  const removeItem = async (productId: number) => {
    try {
      await cartAPI.removeItem(productId)
      await loadCart()
      return true
    } catch (error) {
      console.error('Failed to remove item:', error)
      return false
    }
  }

  const clearCart = () => {
    items.value = []
  }

  return {
    items,
    isLoading,
    totalPrice,
    itemCount,
    loadCart,
    addToCart,
    updateQuantity,
    removeItem,
    clearCart
  }
})

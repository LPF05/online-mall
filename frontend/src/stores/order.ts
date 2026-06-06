import { defineStore } from 'pinia'
import { ref } from 'vue'
import { orderAPI, type Order } from '@/api/order'

export const useOrderStore = defineStore('order', () => {
  const orders = ref<Order[]>([])
  const isLoading = ref(false)

  const loadOrders = async (status?: number) => {
    isLoading.value = true
    try {
      const data = await orderAPI.getOrderList(status)
      orders.value = data || []
    } catch (error) {
      console.error('Failed to load orders:', error)
      orders.value = []
    } finally {
      isLoading.value = false
    }
  }

  const createOrder = async () => {
    try {
      const data = await orderAPI.createOrder()
      await loadOrders()
      return data
    } catch (error) {
      console.error('Failed to create order:', error)
      throw error
    }
  }

  const payOrder = async (orderNo: string) => {
    try {
      await orderAPI.payOrder(orderNo)
      await loadOrders()
      return true
    } catch (error) {
      console.error('Failed to pay order:', error)
      return false
    }
  }

  const confirmReceipt = async (orderNo: string) => {
    try {
      await orderAPI.confirmReceipt(orderNo)
      await loadOrders()
      return true
    } catch (error) {
      console.error('Failed to confirm receipt:', error)
      return false
    }
  }

  return {
    orders,
    isLoading,
    loadOrders,
    createOrder,
    payOrder,
    confirmReceipt
  }
})
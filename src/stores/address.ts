import { defineStore } from 'pinia'
import { ref } from 'vue'
import { addressAPI, type Address } from '@/api/address'

export const useAddressStore = defineStore('address', () => {
  const addresses = ref<Address[]>([])
  const isLoading = ref(false)

  const loadAddresses = async () => {
    isLoading.value = true
    try {
      const data = await addressAPI.getList()
      addresses.value = data || []
    } catch (error) {
      console.error('Failed to load addresses:', error)
    } finally {
      isLoading.value = false
    }
  }

  const addAddress = async (address: Partial<Address>) => {
    const data = await addressAPI.add(address)
    await loadAddresses()
    return data
  }

  const updateAddress = async (id: number, address: Partial<Address>) => {
    const data = await addressAPI.update(id, address)
    await loadAddresses()
    return data
  }

  const deleteAddress = async (id: number) => {
    await addressAPI.delete(id)
    await loadAddresses()
  }

  const setDefault = async (id: number) => {
    await addressAPI.setDefault(id)
    await loadAddresses()
  }

  return { addresses, isLoading, loadAddresses, addAddress, updateAddress, deleteAddress, setDefault }
})

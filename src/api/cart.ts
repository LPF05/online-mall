import request from '@/utils/request'

export interface CartItem {
  productId: number
  productName: string
  price: number
  quantity: number
  imageUrl: string
}

export interface Cart {
  id: number
  userId: number
  items: CartItem[] | string
}

export const cartAPI = {
  getCart() {
    return request.get<Cart>('/cart') as unknown as Promise<Cart>
  },
  addItem(productId: number, quantity: number) {
    return request.post('/cart/add', { productId, quantity })
  },
  updateQuantity(productId: number, quantity: number) {
    return request.put('/cart/update', { productId, quantity })
  },
  removeItem(productId: number) {
    return request.delete('/cart/remove', { params: { productId } })
  }
}

import request from '@/utils/request'
import type { CartItem } from './cart'

export interface Order {
  id: number
  orderNo: string
  userId: number
  items: CartItem[]
  totalAmount: number
  status: number
  createdAt: string
}

export const orderAPI = {
  createOrder() {
    return request.post<Order>('/orders') as unknown as Promise<Order>
  },
  getOrderList(status?: number) {
    return request.get<Order[]>('/orders', { params: { status } }) as unknown as Promise<Order[]>
  },
  getOrderDetail(orderNo: string) {
    return request.get<Order>(`/orders/${orderNo}`) as unknown as Promise<Order>
  },
  payOrder(orderNo: string) {
    return request.post(`/orders/${orderNo}/pay`)
  },
  cancelOrder(orderNo: string) {
    return request.post(`/orders/${orderNo}/cancel`)
  },
  confirmReceipt(orderNo: string) {
    return request.post(`/orders/${orderNo}/confirm`)
  },
  getAllOrders() {
    return request.get<Order[]>('/orders/admin/all') as unknown as Promise<Order[]>
  },
  updateOrderStatus(orderNo: string, status: number) {
    return request.post(`/orders/admin/${orderNo}/status`, { status })
  },
  shipOrder(orderNo: string) {
    return request.post(`/orders/admin/${orderNo}/ship`)
  }
}

export default orderAPI

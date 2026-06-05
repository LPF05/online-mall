import request from '@/utils/request'

export interface Review {
  id: number
  userId: number
  productId: number
  orderNo: string
  rating: number
  content: string
  createdAt: string
  username?: string
  productName?: string
}

export const reviewAPI = {
  addReview(data: { productId: number; orderNo: string; rating: number; content: string }) {
    return request.post<Review>('/reviews', data) as unknown as Promise<Review>
  },
  getProductReviews(productId: number) {
    return request.get<Review[]>(`/reviews/product/${productId}`) as unknown as Promise<Review[]>
  },
  getMyReviews() {
    return request.get<Review[]>('/reviews/my') as unknown as Promise<Review[]>
  }
}

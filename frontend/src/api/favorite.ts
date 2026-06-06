import request from '@/utils/request'

export interface FavoriteItem {
  id: number
  userId: number
  productId: number
  productName: string
  price: number
  imageUrl: string
  createdAt: string
}

export const favoriteAPI = {
  getFavorites() {
    return request.get<FavoriteItem[]>('/favorites') as unknown as Promise<FavoriteItem[]>
  },
  addFavorite(productId: number) {
    return request.post('/favorites', { productId })
  },
  removeFavorite(productId: number) {
    return request.delete(`/favorites/${productId}`)
  }
}

import request from '@/utils/request'

export interface BrowseHistoryItem {
  id: number
  userId: number
  productId: number
  productName: string
  price: number
  imageUrl: string
  createdAt: string
}

export const browseHistoryAPI = {
  getHistory() {
    return request.get<BrowseHistoryItem[]>('/browse') as unknown as Promise<BrowseHistoryItem[]>
  },
  addHistory(productId: number) {
    return request.post('/browse', { productId })
  },
  clearHistory() {
    return request.delete('/browse')
  }
}

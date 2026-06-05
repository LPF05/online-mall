import request from '@/utils/request'

export interface Product {
  id: number
  name: string
  description: string
  price: number
  originalPrice: number
  stock: number
  category: string
  imageUrl: string
  isOnSale: number
  saleStart: string
  saleEnd: string
  viewCount: number
  salesCount: number
  createdAt: string
}

export const productAPI = {
  getList(params?: { category?: string; sort?: string; keyword?: string; minPrice?: number; maxPrice?: number }) {
    return request.get<Product[]>('/products', { params }) as unknown as Promise<Product[]>
  },
  getDetail(id: number) {
    return request.get<Product>(`/products/${id}`) as unknown as Promise<Product>
  },
  search(keyword: string) {
    return request.get<Product[]>('/products/search', { params: { keyword } }) as unknown as Promise<Product[]>
  },
  getHot(sortBy: string = 'view_count') {
    return request.get<Product[]>('/products/hot', { params: { sortBy } }) as unknown as Promise<Product[]>
  },
  getCategories() {
    return request.get<string[]>('/products/categories') as unknown as Promise<string[]>
  },
  getSaleProducts(limit: number = 10) {
    return request.get<Product[]>('/products/sale', { params: { limit } }) as unknown as Promise<Product[]>
  },
  getLatestProducts(limit: number = 10) {
    return request.get<Product[]>('/products/latest', { params: { limit } }) as unknown as Promise<Product[]>
  },
  getAllProducts() {
    return request.get<Product[]>('/products/admin/all') as unknown as Promise<Product[]>
  },
  createProduct(data: any) {
    return request.post<Product>('/products/admin', data)
  },
  updateProduct(id: number, data: any) {
    return request.put<Product>(`/products/admin/${id}`, data)
  },
  deleteProduct(id: number) {
    return request.delete(`/products/admin/${id}`)
  }
}

export default productAPI

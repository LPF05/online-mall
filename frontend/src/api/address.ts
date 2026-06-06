import request from '@/utils/request'

export interface Address {
  id: number
  userId: number
  receiverName: string
  phone: string
  province: string
  city: string
  district: string
  detailAddress: string
  isDefault: number
  createdAt: string
  updatedAt: string
}

export const addressAPI = {
  getList() {
    return request.get<Address[]>('/addresses') as unknown as Promise<Address[]>
  },
  getDetail(id: number) {
    return request.get<Address>(`/addresses/${id}`) as unknown as Promise<Address>
  },
  add(data: Partial<Address>) {
    return request.post<Address>('/addresses', data) as unknown as Promise<Address>
  },
  update(id: number, data: Partial<Address>) {
    return request.put<Address>(`/addresses/${id}`, data) as unknown as Promise<Address>
  },
  delete(id: number) {
    return request.delete(`/addresses/${id}`)
  },
  setDefault(id: number) {
    return request.put(`/addresses/${id}/default`)
  }
}

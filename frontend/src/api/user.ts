import request from '@/utils/request'

export interface User {
  id: number
  username: string
  email: string
  phone?: string
  balance: number
  points?: number
  role: string
  createdAt: string
}

export const userAPI = {
  getProfile() {
    return request.get<User>('/user/info') as unknown as Promise<User>
  },
  recharge(amount: number) {
    return request.post('/user/recharge', { amount })
  },
  updateProfile(data: { username?: string; phone?: string }) {
    return request.put('/user/info', data)
  },
  changePassword(data: { oldPassword: string; newPassword: string }) {
    return request.post('/user/change-password', data)
  },
  getAllUsers() {
    return request.get<User[]>('/user/users') as unknown as Promise<User[]>
  }
}

export default userAPI

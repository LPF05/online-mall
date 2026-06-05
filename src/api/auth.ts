import request from '@/utils/request'

export interface LoginData {
  email: string
  password: string
}

export interface RegisterData {
  username: string
  email: string
  password: string
}

export interface LoginResult {
  token: string
}

export const authAPI = {
  login(data: LoginData) {
    return request.post<LoginResult>('/auth/login', data) as unknown as Promise<LoginResult>
  },
  register(data: RegisterData) {
    return request.post('/auth/register', data)
  }
}

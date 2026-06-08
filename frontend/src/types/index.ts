export interface AuthUser {
  id: number
  username: string
}

export interface LoginPayload {
  username: string
  password: string
}

export interface CreatePostPayload {
  title: string
  content: string
}

export interface UpdatePostPayload {
  title: string
  content: string
}

export interface PostSummary {
  id: number
  title: string
  excerpt: string
  authorName: string
  createdAt: string
}

export interface PostDetail {
  id: number
  title: string
  content: string
  authorName: string
  createdAt: string
}

export interface PageResponse<T> {
  items: T[]
  total: number
  page: number
  size: number
}

export interface ApiError {
  code: string
  message: string
  traceId: string
}

export interface TokenResponse {
  tokenType: string
  accessToken: string
  expiresIn: number
  refreshToken: string
}

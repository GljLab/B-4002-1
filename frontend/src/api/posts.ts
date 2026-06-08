import { http } from './http'
import type {
  CreatePostPayload,
  PageResponse,
  PostDetail,
  PostSummary,
  UpdatePostPayload,
} from '../types'

export async function getPublicPosts(page = 1, size = 10): Promise<PageResponse<PostSummary>> {
  const { data } = await http.get<PageResponse<PostSummary>>('/posts', {
    params: { page, size },
  })
  return data
}

export async function getPostDetail(id: number): Promise<PostDetail> {
  const { data } = await http.get<PostDetail>(`/posts/${id}`)
  return data
}

export async function getMyPosts(): Promise<PostSummary[]> {
  const { data } = await http.get<PostSummary[]>('/admin/posts/mine')
  return data
}

export async function createPost(payload: CreatePostPayload): Promise<PostDetail> {
  const { data } = await http.post<PostDetail>('/admin/posts', payload)
  return data
}

export async function updatePost(id: number, payload: UpdatePostPayload): Promise<PostDetail> {
  const { data } = await http.put<PostDetail>(`/admin/posts/${id}`, payload)
  return data
}

export async function deletePost(id: number): Promise<void> {
  await http.delete(`/admin/posts/${id}`)
}

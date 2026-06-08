<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getPostDetail } from '../api/posts'
import type { PostDetail } from '../types'

const route = useRoute()
const loading = ref(false)
const post = ref<PostDetail | null>(null)

const postId = computed(() => Number(route.params.id))

async function loadPost() {
  if (!Number.isFinite(postId.value) || postId.value <= 0) {
    ElMessage.error('文章参数错误')
    return
  }

  loading.value = true
  try {
    post.value = await getPostDetail(postId.value)
  } catch {
    ElMessage.error('文章不存在或已删除')
  } finally {
    loading.value = false
  }
}

onMounted(loadPost)
</script>

<template>
  <div class="view-shell detail-shell">
    <el-card v-loading="loading" class="detail-card">
      <template v-if="post">
        <header class="detail-header">
          <h1 class="detail-title">{{ post.title }}</h1>
          <p class="meta">作者：{{ post.authorName }} · {{ dayjs(post.createdAt).format('YYYY-MM-DD HH:mm:ss') }}</p>
        </header>
        <el-divider />
        <div class="content-text detail-content">{{ post.content }}</div>
      </template>
      <template v-else>
        <el-empty class="detail-empty" description="未找到该文章" />
      </template>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'
import { getPublicPosts } from '../api/posts'
import type { PostSummary } from '../types'

const posts = ref<PostSummary[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)

async function loadPosts() {
  loading.value = true
  try {
    const data = await getPublicPosts(page.value, size.value)
    posts.value = data.items
    total.value = data.total
  } catch {
    ElMessage.error('文章列表加载失败')
  } finally {
    loading.value = false
  }
}

async function handlePageChange(nextPage: number) {
  page.value = nextPage
  await loadPosts()
}

onMounted(loadPosts)
</script>

<template>
  <div class="view-shell home-shell">
    <section class="home-hero">
      <p class="hero-kicker">BLOG</p>
      <h1>最新文章</h1>
      <p class="hero-desc">浏览最新发布内容，快速进入文章详情。</p>
    </section>

    <el-skeleton v-if="loading" class="skeleton-panel" :rows="6" animated />

    <template v-else>
      <el-empty v-if="posts.length === 0" class="home-empty" description="暂无文章" />
      <div v-else class="card-list">
        <el-card
          v-for="(post, index) in posts"
          :key="post.id"
          class="post-card"
          shadow="hover"
          :style="{ '--stagger-index': index }"
        >
          <template #header>
            <router-link :to="`/posts/${post.id}`" class="title-link">
              {{ post.title }}
            </router-link>
          </template>
          <p class="meta">作者：{{ post.authorName }} · {{ dayjs(post.createdAt).format('YYYY-MM-DD HH:mm:ss') }}</p>
          <p class="excerpt">{{ post.excerpt }}</p>
        </el-card>
      </div>
      <div class="pagination-row">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="size"
          :current-page="page"
          @current-change="handlePageChange"
        />
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import dayjs from 'dayjs'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createPost, deletePost, getMyPosts } from '../api/posts'
import type { PostSummary } from '../types'
import PostEditor from '../components/PostEditor.vue'

const router = useRouter()
const posts = ref<PostSummary[]>([])
const loading = ref(false)
const submitting = ref(false)

const form = reactive({
  title: '',
  content: '',
})

async function loadMine() {
  loading.value = true
  try {
    posts.value = await getMyPosts()
  } catch {
    ElMessage.error('获取我的文章失败')
  } finally {
    loading.value = false
  }
}

async function submitPost() {
  if (!form.title.trim() || !form.content.trim()) {
    ElMessage.warning('标题和内容不能为空')
    return
  }

  submitting.value = true
  try {
    await createPost({ title: form.title.trim(), content: form.content.trim() })
    form.title = ''
    form.content = ''
    ElMessage.success('文章发布成功')
    await loadMine()
  } catch {
    ElMessage.error('文章发布失败')
  } finally {
    submitting.value = false
  }
}

async function handleDelete(id: number) {
  try {
    await ElMessageBox.confirm('确定删除这篇文章吗？', '确认', {
      type: 'warning',
      confirmButtonText: '删除',
      cancelButtonText: '取消',
    })

    await deletePost(id)
    ElMessage.success('删除成功')
    await loadMine()
  } catch {
    // 用户取消或删除失败
  }
}

function handleEdit(id: number) {
  router.push(`/admin/posts/${id}/edit`)
}

onMounted(loadMine)
</script>

<template>
  <div class="view-shell admin-shell">
    <header class="admin-header">
      <h1>后台管理</h1>
      <p class="admin-subtitle">发布新文章并管理你的内容。</p>
    </header>

    <section class="admin-compose-section">
      <el-card class="panel-card panel-editor panel-editor-full">
        <template #header>
          <div class="panel-title-wrap">
            <strong class="panel-title">发布新文章</strong>
            <span class="panel-meta">全宽编辑区，专注写作</span>
          </div>
        </template>
        <PostEditor
          v-model:title="form.title"
          v-model:content="form.content"
          :loading="submitting"
          :textarea-rows="16"
          submit-text="发布文章"
          @submit="submitPost"
        />
      </el-card>
    </section>

    <section class="admin-list-section">
      <el-card class="panel-card panel-table">
        <template #header>
          <div class="panel-title-wrap">
            <strong class="panel-title">我的文章</strong>
          </div>
        </template>

        <el-table class="admin-table" v-loading="loading" :data="posts" stripe>
          <el-table-column prop="title" label="标题" min-width="180" />
          <el-table-column label="发布时间" min-width="180">
            <template #default="scope">
              {{ dayjs(scope.row.createdAt).format('YYYY-MM-DD HH:mm:ss') }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button class="action-btn" type="primary" link @click="handleEdit(scope.row.id)">编辑</el-button>
              <el-button class="action-btn" type="danger" link @click="handleDelete(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </section>
  </div>
</template>

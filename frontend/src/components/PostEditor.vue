<script setup lang="ts">
defineProps<{
  title: string
  content: string
  textareaRows?: number
  loading?: boolean
  submitText?: string
}>()

defineEmits<{
  'update:title': [value: string]
  'update:content': [value: string]
  submit: []
}>()
</script>

<template>
  <el-form class="editor-form" label-position="top" @submit.prevent="$emit('submit')">
    <el-form-item class="editor-form-item" label="标题">
      <el-input
        class="editor-input"
        :model-value="title"
        maxlength="200"
        show-word-limit
        placeholder="请输入文章标题"
        @update:model-value="$emit('update:title', $event)"
      />
    </el-form-item>

    <el-form-item class="editor-form-item" label="正文">
      <el-input
        class="editor-textarea"
        :model-value="content"
        type="textarea"
        :rows="textareaRows ?? 10"
        placeholder="请输入正文内容"
        @update:model-value="$emit('update:content', $event)"
      />
    </el-form-item>

    <el-button class="editor-submit-btn" type="primary" :loading="loading" @click="$emit('submit')">
      {{ submitText || '提交' }}
    </el-button>
  </el-form>
</template>

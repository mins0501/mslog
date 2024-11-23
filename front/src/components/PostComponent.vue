<script setup lang="ts">
import type Post from '@/entity/post/Post'

const props = defineProps<{
  post: Post
}>()
</script>

<template>
  <div class="post-item">

    <el-row :gutter="20">
      <el-col :span="4">
        <div class="grid-content ep-bg-purple">
          <router-link :to="{ name: 'post', params: { postId: props.post.id } }">
            {{ props.post.id }}
          </router-link>
        </div>
      </el-col>
      <el-col :span="13">
        <div class="grid-content ep-bg-purple">
          <router-link :to="{ name: 'post', params: { postId: props.post.id } }">
            {{ props.post.title }}
          </router-link>
        </div>
      </el-col>
      <el-col :span="7">
        <div class="grid-content ep-bg-purple">
          {{ props.post.getDisplayRegDate() }}
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<style scoped lang="scss">
.post-item {
  margin: 30px 0;
}
.el-row {
  margin-bottom: 20px;
}
.el-row:last-child {
  margin-bottom: 0;
}
.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  height: 30px;

  a {
    display: inline-block;
    min-width: 100px;
    max-width: 340px;
    min-height: 0;    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
}

</style>

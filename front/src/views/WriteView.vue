<script setup lang="ts">
import { reactive } from 'vue'
import PostWrite from '@/entity/post/PostWrite'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import { ElMessage } from 'element-plus'
import type HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'

const state = reactive({
  postWrite: new PostWrite(),
})

const POST_REPOSITORY = container.resolve(PostRepository)

const router = useRouter()

function write() {
  POST_REPOSITORY.write(state.postWrite)
    .then(() => {
      ElMessage({ type: 'success', message: '글 등록이 완료되었습니다.' })
      router.replace('/')
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}

function goBack() {
  router.go(-1);
}

</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input v-model="state.postWrite.title" size="large" placeholder="제목을 입력해주세요" />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="state.postWrite.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>


    <div class="footer">
      <div class="write" @click="write()">등록완료</div>
      <div class="back" @click="goBack()">뒤로가기</div>
    </div>

  </el-form>
</template>

<style>

.footer {
  margin-top: 1rem;
  display: flex;
  font-size: 0.78rem;
  justify-content: flex-end;
  gap: 0.8rem;

  .write:hover {
    cursor: pointer;
  }

  .back:hover {
    cursor: pointer;
  }
}

</style>

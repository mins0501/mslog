<script setup lang="ts">
import type Post from '@/entity/post/Post'
import { onMounted, reactive } from 'vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import Paging from '@/entity/data/Paging'
import PostComponent from '@/components/PostComponent.vue'

const POST_REPOSITORY = container.resolve(PostRepository)

type StateType = {
  postList: Paging<Post>
}

const state = reactive<StateType>({
  postList: new Paging<Post>(),
})

function getList(page = 1) {
  POST_REPOSITORY.getList(page).then((postList) => {
    state.postList = postList
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="contents-wrap">
    <div class="contents">

      <span class="total-count">게시글 수: {{ state.postList.totalCount }}</span>

      <div class="posts">
        <div class="post-header">
          <el-row :gutter="20">
            <el-col :span="4">
              <div class="grid-content ep-bg-purple">
                글 번호
              </div>
            </el-col>
            <el-col :span="13">
              <div class="grid-content ep-bg-purple">
                글 제목
              </div>
            </el-col>
            <el-col :span="7">
              <div class="grid-content ep-bg-purple">
                작성일자
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="part-line"></div>
        <div v-for="post in state.postList.items" :key="post.id">
          <PostComponent :post="post" />
        </div>
      </div>

      <div class="post-page">
        <el-pagination
          :background="true"
          layout="prev, pager, next"
          v-model:current-page="state.postList.page"
          :total="state.postList.totalCount"
          :default-page-size="5"
          @current-change="(page: number) => getList(page)"
        />
      </div>

    </div>
  </div>
</template>

<style scoped lang="scss">
.contents-wrap {
  margin: 0 auto;
  overflow: hidden;

  .contents {
    padding: 10px;
    box-sizing: border-box;
    position: relative;
    height: 600px;

    .total-count {
      position: absolute;
      top: 50px;
      left: 10px;
    }

    .posts {
      width: 100%;
      position: absolute;
      top: 105px;
      left: 20px;
      padding: 10px;
      box-sizing: border-box;

      .post-header {
        width: 100%;
        font-weight: 700;
      }

      .part-line {
        width: 100%;
        margin: 20px 0;
        border-bottom: 5px double #000;
      }

    }

    .post-page {
      position: absolute;
      bottom: 30px;
      left: 50%;
      transform: translateX(-50%);
      display: flex;
    }

  }
}

</style>

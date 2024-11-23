<script setup lang="ts">
import {defineProps, ref} from "vue";
import axios from 'axios';
import {useRouter} from "vue-router";


const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

axios.get(`/api/post/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/post/${props.postId}`, post.value).then((response) => {
    router.replace( { name: "home" } )
  });
}

function goBack() {
  router.go(-1);
}

function goList() {
  router.go(-2);
}
</script>

<template>
  <div class="edit-wrap">
    <el-form label-position="top">

      <el-form-item label="제목">
        <el-input v-model="post.title" size="large" />
      </el-form-item>

      <el-form-item label="내용">
        <el-input v-model="post.content" type="textarea" rows="15" />
      </el-form-item>

      <div class="footer">
        <div class="edit" @click="edit()">수정완료</div>
        <div class="back" @click="goBack()">뒤로가기</div>
        <div class="list" @click="goList()">목록보기</div>
      </div>

    </el-form>
  </div>
</template>

<style>
.edit-wrap {
  margin-top: 70px;
}

.footer {
  margin-top: 1rem;
  display: flex;
  font-size: 0.78rem;
  justify-content: flex-end;
  gap: 0.8rem;

  .edit:hover {
    cursor: pointer;
  }

  .back:hover {
    cursor: pointer;
  }

  .list:hover {
    cursor: pointer;
  }
}

</style>

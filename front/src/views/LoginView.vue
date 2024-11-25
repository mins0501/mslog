<script setup lang="ts">
import { reactive } from 'vue'
import Login from '@/entity/user/Login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'
import UserRepository from '@/repository/UserRepository'
import { container } from 'tsyringe'

const state = reactive({
  login: new Login(),
})

const USER_REPOSITORY = container.resolve(UserRepository)

const router = useRouter()

function doLogin() {
  USER_REPOSITORY.login(state.login)
    .then(() => {
      ElMessage({ type: 'success', message: '환영합니다 :)' })
      location.href = '/'
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
  <div class="login-wrap">
    <div class="sub-wrap">
      <div class="sub-title">로 그 인</div>
    </div>
    <el-row>
      <el-col :span="10" :offset="7">
        <el-form label-position="top">

          <el-form-item label="이메일">
            <el-input v-model="state.login.email"></el-input>
          </el-form-item>

          <el-form-item label="비밀번호">
            <el-input type="password" v-model="state.login.password"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="doLogin()">로그인</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" style="width: 100%" @click="goBack()">취소</el-button>
          </el-form-item>

        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.login-wrap {
  margin-top: 70px;
}

.sub-wrap {
  width: 100%;
  margin: 30px auto;

  .sub-title {
    text-align: center;
    font-weight: 700;
    font-size: 30px;
    color: #493D47FF;
  }
}
</style>

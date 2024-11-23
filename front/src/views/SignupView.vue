<script setup lang="ts">
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'
import { container } from 'tsyringe'
import Signup from "@/entity/user/Signup";
import AuthRepository from "@/repository/AuthRepository";

const state = reactive({
  signup: new Signup(),
})

const AUTH_REPOSITORY = container.resolve(AuthRepository)

const router = useRouter()

function signup() {
  AUTH_REPOSITORY.signup(state.signup)
    .then(() => {
      ElMessage({ type: 'success', message: '회원가입이 완료되었습니다.' })
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
  <div class="signup-wrap">
    <el-row>
      <el-col :span="10" :offset="7">
        <el-form label-position="top">

          <el-form-item label="이름">
            <el-input v-model="state.signup.name"></el-input>
          </el-form-item>

          <el-form-item label="이메일">
            <el-input v-model="state.signup.email"></el-input>
          </el-form-item>

          <el-form-item label="비밀번호">
            <el-input type="password" v-model="state.signup.password"></el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" style="width: 100%" @click="signup()">회원가입</el-button>
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
.signup-wrap {
  margin-top: 70px;
}
</style>

<script setup lang="ts">

import { onBeforeMount, reactive } from 'vue'
import UserRepository from '@/repository/UserRepository'
import { container } from 'tsyringe'
import ProfileRepository from '@/repository/ProfileRepository'
import type UserProfile from '@/entity/user/UserProfile'
import { ArrowDown } from '@element-plus/icons-vue'

const USER_REPOSITORY = container.resolve(UserRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

type StateType = {
  profile: UserProfile | null
}

const state = reactive<StateType>({
  profile: null,
})

onBeforeMount(() => {
  USER_REPOSITORY.getProfile().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile)
    state.profile = profile
  })
})

function logout() {
  PROFILE_REPOSITORY.clear()
  location.href = '/api/logout'
}

</script>

<template>
  <div class="inner-wrap">
    <div class="inner">

      <div class="logo-wrap">
        <router-link to="/"><img src="/images/ms_logo.png" alt="logo" class="logo"/></router-link>
        <div class="title"><router-link to="/">msKym Blog</router-link></div>
        <br/>
      </div>

      <div class="sub-menu">
        <ul class="menu">
          <li v-if="state.profile === null">
            <router-link to="/signup">회원가입</router-link>
          </li>
          <li v-if="state.profile === null">
            <router-link to="/login">로그인</router-link>
          </li>
<!--          <li v-else>-->
<!--            <a href="#" @click="logout()">({{state.profile!.name}}) 로그아웃</a>-->
<!--          </li>-->
          <li v-else>
            <el-dropdown>
            <span class="el-dropdown-link">
              {{state.profile!.name}}님 반갑습니다.
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <a href="#" @click="logout()">로그아웃</a>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </li>
        </ul>
      </div>

      <ul class="main-menu">
        <li class="item" v-if="state.profile !== null">
          <router-link to="/write">글 작성</router-link>
        </li>
      </ul>

    </div>
  </div>
</template>

<style scoped lang="scss">
.inner-wrap {
  width: 100%;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9;
  background-color: #F6F5F0;
  border-bottom: 1px solid #c8c8c8;

  .inner {
    width: 90%;
    height: 120px;
    margin: 0 auto;
    position: relative;

    .logo-wrap {
      height: 120px;

      .logo {
        height: 75px;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 20px;
        margin: auto;
      }

      .title {
        height: 75px;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 120px;
        font-size: 40px;
        line-height: 3;

        a{
          color: #592B18;
        }
      }
    }

    .sub-menu {
      position: absolute;
      top: 10px;
      right: 0;
      display: flex;

      ul.menu {
        font-family: Arial, sans-serif;
        display: flex;

        li {
          position: relative;

          &::before {
            content: "";
            width: 1px;
            height: 12px;
            background-color: #e5e5e5;
            position: absolute;
            top: 0;
            bottom: 0;
            margin: auto;
          }

          &:first-child::before {
            display: none;
          }

          a {
            padding: 11px 16px;
            display: block;
            font-size: 13px;
            color: #592B18;

            &:hover {
              color: #000;
            }
          }
        }
      }
    }

    .main-menu {
      position: absolute;
      bottom: 0;
      right: 0;
      z-index: 1;
      display: flex;

      .item {
        padding: 10px 20px 34px 20px;
        font-family: Arial, sans-serif;
        font-size: 13px;

        a {
          color: #000;

          &:hover {
            text-decoration: underline;
          }
        }
      }
    }

  }

}

.example-showcase .el-dropdown-link {
  cursor: pointer;
  color: var(--el-color-primary);
  display: flex;
  align-items: center;
}
</style>

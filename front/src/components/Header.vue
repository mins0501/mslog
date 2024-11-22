<template>
  <div class="header">
    <router-link to="/"><img src="/images/ms_logo.png" alt="logo" class="logo"/></router-link>
    <div class="title"><router-link to="/">msKym Blog</router-link></div>
    <br/>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
a {
  text-decoration: none;
}

.header {
  height: 60px;
  margin: 1rem 2rem;
  display: flex;
  justify-content: left;
  align-items: center;
}

.logo {
  width: 52px;
  object-fit: cover;
}

.title {
  font-size: 2rem;
  font-weight: 300;
  margin-left: 5px;
}
</style>

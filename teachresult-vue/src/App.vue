<template>
  <div id="app" class="fillcontain">
    <!--<img src="./assets/logo.png">-->
      <router-view ></router-view>
  </div>
</template>

<script>
export default {
  provide () {
    return {
      reload: this.reload
    }
  },
  data () {
    return {
      isRouterAlive: true
    }
  },
  methods: {
    // 页面重新载入函数
    reload () {
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })
    }
  }
}
</script>

<style lang="less">
@import './style/common';
</style>

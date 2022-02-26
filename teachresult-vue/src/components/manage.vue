<template>
  <div class="manage_page fillcontain" style="height: 100%">
    <el-row style="height: 100%;">
      <el-col :span="3" style="min-height: 100%;">
        <el-menu :default-active="defaultActive" mode="horizontal"
                 background-color="#0f5035"
                 active-text-color="yellow"
                 text-color="white"
                 @select="handleselect"  style="min-height: 100%;" theme="dark" router>
          <!--<el-menu-item index="manage"></el-menu-item>
          <el-submenu index="2">
            <template slot="title" ><i class="el-icon-document"></i>教学成果评价</template>
            <el-menu-item index="index" >推荐项目评审</el-menu-item>
            <el-menu-item index="check" v-if="checkAuth" >评分情况查询</el-menu-item>
            <el-menu-item index="show" v-if="checkAuth" >评分情况展示</el-menu-item>
          </el-submenu>-->
          <el-menu-item index="manage">首页</el-menu-item>
          <el-submenu index="2">
            <template slot="title">教学成果评价</template>
            <el-menu-item index="index" >推荐项目评审</el-menu-item>
            <el-menu-item index="check" v-if="checkAuth">评分情况查询</el-menu-item>
            <el-menu-item index="show" v-if="checkAuth">评分情况展示</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-col>
      <el-col :span="21" style="height: 90%;">
        <keep-alive>
          <transition name="fade" mode="out-in">
            <router-view v-if="isRouterAlive"></router-view>
          </transition>
        </keep-alive>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isRouterAlive: true
    }
  },
  computed: {
    defaultActive: function () {
      return this.$route.path.replace('/', '')
    },
    checkAuth () {
      // 简易权限判断
      if (sessionStorage.getItem('username') === 'admin') {
        return true
      } else {
        return false
      }
    }
  },
  inject: ['reload'], // 注入重载的功能（注入依赖）
  // 监视
  watch: {
    // 检测路由参数发生改变时，刷新当前页面 调用
    '$route': function () {
      // this.reload()
    }
  },
  methods: {
  }
}
</script>
<style lang="less" scoped>
  @import '../style/mixin';

  .manage_page {
  }
</style>

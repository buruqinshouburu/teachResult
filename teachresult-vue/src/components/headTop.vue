<template>
  <div style="height: 90%">
   <el-row style="height: 10%" >
     <div class="header_container">
       <el-breadcrumb separator="/" align="right" >
         <el-menu :default-active="defaultActive" mode="horizontal"
                  background-color="#0f5035"
                  active-text-color="yellow"
                  text-color="white"
                  @select="handleselect"  style="min-height: 100%;" theme="dark" router>
           <el-menu-item index="manage">首页</el-menu-item>
           <el-submenu index="2">
             <template slot="title">教学成果评价</template>
             <el-menu-item index="index" >推荐项目评审</el-menu-item>
             <el-menu-item index="check" v-if="checkAuth">评分情况查询</el-menu-item>
             <el-menu-item index="show" v-if="checkAuth">评分情况展示</el-menu-item>
           </el-submenu>
           <el-submenu index="3">
             <template slot="title">资料下载</template>
             <el-menu-item index="file" >下载资料目录</el-menu-item>
           </el-submenu>
         </el-menu>
       </el-breadcrumb>
       <el-dropdown @command="handleCommand" style="width: 5%" menu-align='start'>
         <span style="font-size: 20px;color: white"> {{showUsername}}</span>
         <el-dropdown-menu slot="dropdown" style="width: 5%">
           <el-dropdown-item command="home">首页</el-dropdown-item>
           <el-dropdown-item command="signout">退出</el-dropdown-item>
         </el-dropdown-menu>
       </el-dropdown>
     </div>
  </el-row>
   <el-row style="height: 90%">
    <keep-alive>
      <transition name="fade" mode="out-in">
        <router-view v-if="isRouterAlive"></router-view>
      </transition>
    </keep-alive>
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
    },
    showUsername () {
      return sessionStorage.getItem('username')
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
    async handleCommand (command) {
      if (command === 'home') {
        this.$router.push('/manage')
      } else if (command === 'signout') {
        sessionStorage.setItem('username', '')
        sessionStorage.setItem('userid', '')
        this.$message({
          type: 'success',
          message: '退出成功'
        })
        this.$router.push('/')
      }
    },
    handleselect: function (a, b) {
      this.isRouterAlive = false
      this.$nextTick(function () {
        this.isRouterAlive = true
      })// 点击侧边栏重新载入页面
    }
  }
}
</script>

<style lang="less">
  @import '../style/mixin';

  .header_container {
    background-position: center;
    width: 100%;
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .avator {
    .wh(36px, 36px);
    border-radius: 50%;
    margin-right: 37px;
  }

  .el-dropdown-menu__item {
    text-align: center;
  }
</style>

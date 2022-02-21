<template>
  <div class="header_container">
    <el-breadcrumb separator="/">
    </el-breadcrumb>
    <el-dropdown @command="handleCommand" style="width: 10%" menu-align='start'>
      <span> {{showUsername}}</span>
      <el-dropdown-menu slot="dropdown" style="width: 10%">
        <el-dropdown-item command="home">首页</el-dropdown-item>
        <el-dropdown-item command="signout">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  computed: {
    showUsername () {
      return sessionStorage.getItem('username')
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
    }
  }
}
</script>

<style lang="less">
  @import '../style/mixin';

  .header_container {
    background-color: #EFF2F7;
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

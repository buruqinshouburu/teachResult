<template>
  <div class="manage_page fillcontain" style="height: 100%">
    <el-row style="height: 100%;">
      <el-col :span="4" style="min-height: 100%; ">
        <el-menu :default-active="defaultActive"  style="min-height: 100%;" theme="dark" router>
          <el-submenu index="2">
            <template slot="title"><i class="el-icon-document"></i>教学成果评价</template>
            <el-menu-item index="manage">首页</el-menu-item>
            <el-menu-item index="index" >教学成果评分</el-menu-item>
            <el-menu-item index="check" >评分情况查询</el-menu-item>
          </el-submenu>
        </el-menu>
      </el-col>
      <el-col :span="20" style="height: 100%;overflow: auto;">
        <keep-alive>
          <router-view></router-view>
        </keep-alive>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  computed: {
    defaultActive: function () {
      return this.$route.path.replace('/', '')
    }
  }
}
</script>
<style lang="less" scoped>
  @import '../style/mixin';

  .manage_page {

  }
</style>

<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;">
      <el-col>
        <span>欢迎</span>
      </el-col>
    </el-row>
  </div>
</template>
<style>
  .el-dialog {
    display: flex;
    flex-direction: column;
    margin: 0 !important;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    /*height:600px;*/
    max-height: calc(100% - 30px);
    max-width: calc(100% - 30px);
  }

  .el-dialog .el-dialog__body {
    flex: 1;
    overflow: auto;
  }
</style>

<script>
import headTop from '../headTop'
export default {
  components: {
    headTop
  }
}
</script>

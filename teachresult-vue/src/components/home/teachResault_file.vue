<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;height: 100%">
      <el-col style="height: 100%;width: 100%">
        <el-header style="text-align: left;background-color:white">
          <el-row style="height: 100%;">
            <el-col style="width: 7%">
              <el-button type="primary" style="width: 200px" v-if="checkAuth" @click="dialogFormVisible = true" round>资料上传<i
                class="el-icon-upload el-icon&#45;&#45;right"></i>
              </el-button>
            </el-col>
            <el-col style="width: 7%">
              <!--<div class="searchWord">
                <div style="display: inline-block"> 搜索：</div>
                <el-input v-model="search" style="display: inline-block;width: 1300px"
                          placeholder="请输入搜索内容">
                </el-input>
              </div>-->
            </el-col>
          </el-row>
        </el-header>
        <el-table :data="tableData"
                  style="width: 100%;background: transparent;overflow:auto;font-size: 19px"
                  ref="multipleTable"
                  v-loading="tableload"
                  element-loading-text="正在下载,请稍后"
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0f, 50, 35, 0.5)"
                  tooltip-effect="dark"
                  max-height="800"
                  @selection-change="handleSelectionChange"
                  height="100%">
          <el-table-column
            type="selection"
            width="50">
          </el-table-column>
          <el-table-column prop="index" label="序号" type="index" width="60" fixed align="center">
            <el-input prop="fileid" type="hidden" autocomplete="off"></el-input>
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="filename" label="资料名称" min-width="320" align="center">
          </el-table-column>
          <el-table-column prop="cjsj" label="上传时间" min-width="320" align="center">
          </el-table-column>
          <el-table-column prop="" label="操作" min-width="240" align="center">
            <template scope="scope">
              <el-button  type="primary" @click="expFile(scope.row.fileid,scope.row.filename)"
                         icon="el-icon-download" round>下载</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog title="资料数据导入" :visible.sync="dialogFormVisible" style="text-align: left;">
          <el-row style="margin-left: 20px">
            <el-upload
              class="upload-demo"
              action="#"
              :auto-upload="true"
              :limit="1"
              v-loading="loading"
              element-loading-text="正在上传,请稍后"
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0f, 50, 35, 0.5)"
              :on-exceed="handleExceed"
              :on-success="handleSuccess"
              :on-error="handleError"
              :http-request="handleRequest"
              accept=".xls,.rar,.xlsx,.zip,.pdf,.doc,.docx,.ppt"
              :file-list="fileList">
              <el-button v-if="checkAuth" size="large" type="primary">资料上传</el-button>
              <div slot="tip" class="el-upload__tip" style="font-size: 20px">支持.xls,.rar,.xlsx,.zip,.pdf,.doc,.docx,.ppt格式上传,文件最大5G</div>
            </el-upload>
          </el-row>
        </el-dialog>
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
    width: 35%;
    max-height: calc(100% - 30px);
    max-width: calc(100% - 30px);
  }

  .el-dialog .el-dialog__body {
    flex: 1;
    overflow: auto;
  }
  .el-tooltip__popper {
    max-width: 400px;
    line-height: 180%;
  }
</style>

<script>
// import headTop from '../headTop'

export default {
  data () {
    return {
      tableData: [],
      tjflag: false,
      items: [],
      fileList: [],
      multipleSelection: [],
      dialogFormVisible: false,
      formLabelWidth: '120px',
      loading: false,
      tableload: false
    }
  },
  /* ,
  components: {
  }, */
  computed: {
    checkAuth () {
      // 简易权限判断
      if (sessionStorage.getItem('username') === 'admin') {
        return true
      } else {
        return false
      }
    }
  },
  created () {
    this.initData()
  },
  methods: {
    async initData () {
      this.$axios.post('/imp/checkFile')
        .then(res => {
          if (res.data.length === 0) {
          } else {
            this.getTRData(res.data)
          }
        })
        .catch(failResponse => {
          console.log('获取数据失败', failResponse)
        })
    },
    handleRequest (param) {
      // 相当于input里取得files
      let fileObj = param.file
      // FormData对象
      let fd = new FormData()
      const size = fileObj.size / 1024 / 1024 / 1024
      if (size > 5) {
        this.$notify.warning({
          title: '警告',
          message: '大小必须小于5G'
        })
        return
      }
      this.loading = true
      fd.append('file', fileObj) // 文件对象
      this.$axios.post('/imp/uploadFile', fd, {responseType: 'utf-8'})
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$alert(successResponse.data.msg)
            this.dialogFormVisible = false
            this.loading = false
            this.initData()
          } else if (successResponse.data.code === 400) {
            this.$alert('文件不存在')
            this.tableload = false
          }
        })
        .catch(failResponse => {
        })
    },
    handleExceed (files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    handleSuccess (successResponse, files, fileList) {
      if (successResponse.data.code === 200) {
        this.$alert(successResponse.data.msg)
        this.init()
      } else if (successResponse.data.code === 400) {
        this.$alert('文件不存在')
      }
    },
    handleError (file, fileList) {
      this.$alert('导入失败，请检查文件是否正确')
    },
    async getTRData (data) {
      try {
        this.tableData = []
        console.log(data)
        data.forEach(file => {
          const tableItem = {
            fileid: file.fileid,
            filename: file.filename,
            cjsj: file.cjsj
          }
          this.tableData.push(tableItem)
        })
      } catch (err) {
        console.log('获取数据失败', err)
      }
    },
    expFile (fileid, filename) {
      let fd = new FormData()
      fd.append('fileid', fileid)
      this.tableload = true
      this.$axios.post('/imp/downloadFile', fd, {responseType: 'blob'})
        .then(successResponse => {
          if (successResponse) {
            var fileDownload = require('js-file-download')
            fileDownload(successResponse.data, filename)
            this.tableload = false
          }
        })
        .catch(failResponse => {
          this.$alert('下载失败,文件不存在或已经损坏')
          console.log('文件下载失败')
          this.tableload = false
        })
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    delAll () {
      console.log(this.multipleSelection)
      let val = this.multipleSelection
      if (val) {
        let tritemids = ''
        val.forEach((val, index) => {
          tritemids += val.TRItemid + ','
        })
        this.$confirm('是否确定删除所选中数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let fd = new FormData()
          fd.append('tritemids', tritemids)
          this.$axios.post('/imp/delAllTRItem', fd, {responseType: 'utf-8'}).then(successResponse => {
            if (successResponse.data.code === 200) {
              this.$alert(successResponse.data.msg)
              this.tableData = []
              this.initData()
            } else {
              this.$alert('删除失败,请检查网络是否可用')
            }
          })
            .catch(failResponse => {
              console.log('')
            })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消提交'
          })
        })
      } else {
        this.$alert('请选择要删除的教学成果项目')
      }
    }
  }
}
</script>

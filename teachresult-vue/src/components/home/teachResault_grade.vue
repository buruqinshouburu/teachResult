<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;height: 100%">
      <el-col style="height: 100%">
        <el-header style="text-align: left;background-color:white">
          <el-button type="primary" v-if="!checkAuth&visible" @click="toSend" round>提交</el-button>
          <el-button type="primary" v-if="checkAuth" @click="dialogFormVisible = true">上传<i
            class="el-icon-upload el-icon&#45;&#45;right"></i>
          </el-button>
        </el-header>
        <el-table :data="tableData" style="width: 100%;background: transparent;overflow:auto;"
                  height="100%">
          <el-table-column prop="index" label="序号" type="index" width="80" align="center">
            <el-input prop="TRItemid" type="hidden" autocomplete="off"></el-input>
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
            <el-input prop="TRItemid" type="hidden" autocomplete="off"></el-input>
          </el-table-column>
          <el-table-column prop="cgmc" label="成果名称" width="350" align="center">
          </el-table-column>
          <el-table-column prop="zywcr" label="主要完成人" width="200" align="center">
          </el-table-column>
          <el-table-column prop="cgnrlb" label="成果内容类别" width="120" align="center">
          </el-table-column>
          <el-table-column prop="dw" label="单位" width="100" align="center">
          </el-table-column>
          <el-table-column prop="xypx" label="学院排序" width="80" align="center">
          </el-table-column>
          <el-table-column prop="pf" label="评分" width="80" align="center">
          </el-table-column>
          <el-table-column prop="xgyj" label="修改意见" align="center">
          </el-table-column>
          <el-table-column prop="" label="评分" width="120" align="center">
            <template scope="scope">
              <el-button v-if="!checkAuth&visible" type="primary" @click="startPf(scope.$index,scope.row)"
                         icon="el-icon-edit" circle></el-button>
              <el-button v-if="checkAuth" type="danger" @click="deleteItem(scope.$index,scope.row)"
                         icon="el-icon-delete" circle></el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog title="教学成果评分" v-if="dialogPfVisible" :visible.sync="dialogPfVisible" style="text-align: left;"
                   :before-close="closePfWindow">
          <el-form :model="formData" :rules="rules" ref="formData">
            <el-input v-model="formData.TRItemid" type="hidden" autocomplete="off"></el-input>
            <el-form-item label="教学成果名称" :label-width="formLabelWidth" prop="cgmc">
              {{formData.cgmc}}
            </el-form-item>
            <el-form-item label="主要完成人" :label-width="formLabelWidth" prop="zywcr">
              {{formData.zywcr}}
            </el-form-item>
            <el-form-item label="评分" :label-width="formLabelWidth" prop="pf">
              <el-input v-model="formData.pf" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="修改意见" :label-width="formLabelWidth" prop="xgyj">
              <el-input type="textarea" :rows="3" maxlength="1000" :autosize="{ minRows: 2, maxRows: 5}"
                        @input="timeEffectInput" class="el-input__count"
                        placeholder="请输入内容" v-model="formData.xgyj" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="closePfWindow">取 消</el-button>
            <el-button type="primary" @click="submitPf('formData')">保 存</el-button>
          </div>
        </el-dialog>
        <el-dialog title="教学成果评价数据导入" :visible.sync="dialogFormVisible" style="text-align: left;">
          <el-header style="text-align: left">
            <el-button size="large" type="primary" @click="expTemplate">模板下载</el-button>
          </el-header>
          <el-row style="margin-left: 20px">
            <el-upload
              class="upload-demo"
              action="#"
              :auto-upload="true"
              :limit="1"
              :on-exceed="handleExceed"
              :on-success="handleSuccess"
              :on-error="handleError"
              :http-request="handleRequest"
              accept="xls"
              :file-list="fileList">
              <el-button v-if="checkAuth" size="large" type="primary">上传文件</el-button>
              <div slot="tip" class="el-upload__tip" style="font-size: 20px">只可使用下载的模板进行导入</div>
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
    width: 30%;
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
  data () {
    var validatePf = (rule, value, callback) => {
      if (parseInt(value) > 100) {
        callback(new Error('分数不能大于100分'))
      } else {
        callback()
      }
    }
    return {
      tableData: [],
      visible: false,
      items: [],
      dialogFormVisible: false,
      dialogPfVisible: false,
      fileList: [],
      formData: {
        pf: '',
        xgyj: '',
        TRItemid: '',
        cgmc: ''
      },
      rules: {
        pf: [{required: true, message: '请输入成果评分', trigger: 'blur'},
          {pattern: /^([6-9]\d{1,2}(\.[5]{1})?)$|^100/, message: '打分区间限制在60-100分,最小单位为0.5分', trigger: 'blur'},
          {validator: validatePf, trigger: 'blur'}
        ]
      },
      formLabelWidth: '120px'
    }
  },
  components: {
    headTop
  },
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
      let fd = new FormData()
      fd.append('userid', sessionStorage.getItem('userid'))
      this.$axios.post('/imp/checkItem', fd)
        .then(res => {
          if (res.data.length === 0) {
          } else {
            this.visible = true
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
      fd.append('file', fileObj) // 文件对象
      this.$axios.post('/imp/impTRData', fd, {responseType: 'utf-8'})
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert(successResponse.data.msg)
            this.dialogFormVisible = false
            this.initData()
          } else if (successResponse.data.code === 400) {
            alert('文件不存在')
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
        alert(successResponse.data.msg)
        this.init()
      } else if (successResponse.data.code === 400) {
        alert('文件不存在')
      }
    },
    handleError (file, fileList) {
      alert('导入失败，请检查文件是否正确')
    },
    expTemplate () {
      this.$axios.post('/imp/downloadTemplate', this.formData, {responseType: 'blob'})
        .then(successResponse => {
          if (successResponse) {
            var fileDownload = require('js-file-download')
            fileDownload(successResponse.data, '待评审项目导入模板.xls')
          }
        })
        .catch(failResponse => {
          console.log('文件下载失败')
        })
    },
    async getTRData (data) {
      try {
        this.tableData = []
        data.forEach(item => {
          const tableItem = {
            TRItemid: item.id,
            cgmc: item.cgmc,
            zywcr: item.zywcr,
            cgnrlb: item.cgnrlb,
            dw: item.dw,
            xypx: item.xypx,
            pf: item.pf,
            xgyj: item.xgyj
          }
          this.tableData.push(tableItem)
          if (item.istj === '1') {
            this.visible = false
          }
        })
      } catch (err) {
        console.log('获取数据失败', err)
      }
    },
    startPf (id, row) {
      this.formData = row
      this.dialogPfVisible = true
    },
    deleteItem (id, row) {
      this.$confirm('是否确定删除该条数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let fd = new FormData()
        fd.append('TRItemid', row.TRItemid)
        this.$axios.post('/imp/delTRItem', fd, {responseType: 'utf-8'}).then(successResponse => {
          if (successResponse.data.code === 200) {
            alert(successResponse.data.msg)
            this.initData()
          } else {
            alert('删除失败,请检查网络是否可用')
          }
        })
          .catch(failResponse => {
            console.log('')
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    timeEffectInput () {
      const length = this.formData.xgyj.length
      if (length >= 1000) {
        this.$el.getElementsByClassName('el-input__count')[1].style = 'color:red !important'
        this.formData.xgyj = this.formData.xgyj.substr(0, 1000)
      } else {
        this.$el.getElementsByClassName('el-input__count')[1].style = 'color:#000 !important'
      }
    },
    closePfWindow () {
      this.formData.pf = ''
      this.formData.xgyj = ''
      this.dialogPfVisible = false
    },
    submitPf (formData) {
      this.$refs[formData].validate(
        async (valid) => {
          if (valid) {
            let fd = new FormData()
            fd.append('pf', this.formData.pf)
            if (this.formData.xgyj) {
              fd.append('xgyj', this.formData.xgyj)
            }
            fd.append('TRItemid', this.formData.TRItemid)
            fd.append('psr', sessionStorage.getItem('userid'))
            this.$axios.post('/imp/saveScore', fd, {responseType: 'utf-8'}).then(successResponse => {
              if (successResponse.data.code === 200) {
                this.dialogPfVisible = false
                this.initData()
              } else {
                alert('评分保存失败,请检查网络是否可用')
              }
            })
              .catch(failResponse => {
                console.log('')
              })
          } else {
            this.$notify.error({
              title: '错误',
              message: '请检查输入是否正确',
              offset: 100
            })
            return false
          }
        }
      )
    },
    toSend () {
      this.$confirm('提交后将不可再修改,是否确定提交?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let fd = new FormData()
        fd.append('psr', sessionStorage.getItem('userid'))
        this.$axios.post('/imp/toSend', fd, {responseType: 'utf-8'}).then(successResponse => {
          if (successResponse.data.code === 200) {
            alert(successResponse.data.msg)
            this.initData()
          } else {
            alert('删除失败,请检查网络是否可用')
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
    }
  }
}
</script>

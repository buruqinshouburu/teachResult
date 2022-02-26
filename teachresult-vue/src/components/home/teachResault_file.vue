<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;height: 100%">
      <el-col style="height: 100%;width: 100%">
        <el-header style="text-align: left;background-color:white">
          <el-row style="height: 100%;">
            <el-col style="width: 7%">
              <el-button type="primary" v-if="!checkAuth&visible" @click="toSend" round>提交</el-button>
              <el-button type="primary" v-if="!checkAuth&tjflag" @click="toCancelSend" round>提交撤回</el-button>
              <el-button type="primary" style="width: 104px" v-if="checkAuth" @click="dialogFormVisible = true" round>上传<i
                class="el-icon-upload el-icon&#45;&#45;right"></i>
              </el-button>
            </el-col>
            <el-col style="width: 7%">
              <el-button type="primary" v-if="checkAuth" @click="delAll" round>批量删除</el-button>
            </el-col>
            <el-col style="width: 7%">
              <el-button type="primary" v-if="checkAuth" @click="openTime" round>提交控制</el-button>
            </el-col>
            <el-col align="right" style="width: 80%" v-if="!checkAuth">
              <font style="font-size: 25px;color: black;" >要求评分总和:</font><font style="font-size: 25px;color: red;">{{checkYqzf}}</font>
              <font style="font-size: 25px;color: black;">  当前评分总和:</font><font style="font-size: 25px;color: red;">{{checkPjzf}}</font>
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
                  tooltip-effect="dark"
                  max-height="800"
                  @selection-change="handleSelectionChange"
                  height="100%">
          <el-table-column
            type="selection"
            width="50">
          </el-table-column>
          <el-table-column prop="index" label="序号" type="index" width="60" fixed align="center">
            <el-input prop="TRItemid" type="hidden" autocomplete="off"></el-input>
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="cgmc" label="成果名称" width="320" align="center">
          </el-table-column>
          <el-table-column prop="zywcr" label="主要完成人" width="300" align="center">
          </el-table-column>
          <el-table-column prop="cgnrlb" label="类别代码" width="120" align="center">
          </el-table-column>
          <el-table-column prop="dw" label="申报单位" width="160" align="center">
          </el-table-column>
          <el-table-column prop="xypx" label="单位排序/上报项目" width="200" align="center">
          </el-table-column>
          <el-table-column prop="pf" sortable :sort-orders="['ascending','descending']"  label="评分" width="120" align="center">
          </el-table-column>
          <el-table-column prop="xgyj" show-overflow-tooltip label="修改意见" align="center">
          </el-table-column>
          <el-table-column prop="" label="操作" width="120" align="center">
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
                        :placeholder="xgyjPlaceHolder" v-model="formData.xgyj" autocomplete="off"></el-input>
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
        <el-dialog title="评分时间控制" :visible.sync="dialogTimeVisible" style="text-align: left;">
            <el-form :model="timeFormData" :rules="rules2"  ref="timeFormData">
              <el-form-item label="评价可提交时间" label-width="200px" prop="sj">
                <div class="block">
                  <span class="demonstration"></span>
                  <el-date-picker
                    v-model="timeFormData.sj"
                    type="datetimerange"
                    format="yyyy-MM-dd HH:mm:ss"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                  </el-date-picker>
                </div>
              </el-form-item>
              <el-form-item label="评价总分" label-width="200px" prop="pjzf">
                <el-input v-model="timeFormData.pjzf" style="width: 400px" autocomplete="off"></el-input>
              </el-form-item>
              <el-form-item label="需填写修改意见项目数" label-width="200px" prop="xms">
                <el-input v-model="timeFormData.xms" style="width: 400px"  autocomplete="off"></el-input>
              </el-form-item>
            </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogTimeVisible=false">取 消</el-button>
            <el-button type="primary" @click="submitTime('timeFormData')">保 存</el-button>
          </div>
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
    var validatePf = (rule, value, callback) => {
      if (parseInt(value) > 100) {
        callback(new Error('分数不能大于100分'))
      } else if (parseInt(value) < 75) {
        callback(new Error('分数不能小于75分'))
      } else {
        callback()
      }
    }
    return {
      tableData: [],
      visible: false,
      tjflag: false,
      items: [],
      dialogFormVisible: false,
      dialogPfVisible: false,
      dialogTimeVisible: false,
      fileList: [],
      multipleSelection: [],
      sumScore: '0',
      yqScore: '0',
      xms: '0',
      formData: {
        pf: '',
        xgyj: '',
        TRItemid: '',
        cgmc: ''
      },
      timeFormData: {
        tjkssj: '',
        tjjssj: '',
        sj: '',
        pjzf: '',
        xms: ''
      },
      rules: {
        pf: [{required: true, message: '请输入成果评分', trigger: 'blur'},
          {pattern: /^([6-9]\d{1,2}(\.[5]{1})?)$|^100/, message: '评分区间限制在75-100分,最小单位为0.5分', trigger: 'blur'},
          {validator: validatePf, trigger: 'blur'}
        ]
      },
      rules2: {
        pjzf: [{required: true, message: '请输入评价总分', trigger: 'blur'},
          {pattern: /^[1-9]\d*$/, message: '请输入正整数', trigger: 'blur'}
        ],
        xms: [{required: true, message: '请输入需填写修改意见项目数', trigger: 'blur'},
          {pattern: /^[1-9]\d*$/, message: '请输入正整数', trigger: 'blur'}
        ]
      },
      formLabelWidth: '120px'
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
    },
    checkPjzf () {
      if (this.sumScore) {
        return this.sumScore
      } else {
        return 0
      }
    },
    checkYqzf () {
      if (this.yqScore) {
        return this.yqScore
      } else {
        return 0
      }
    },
    xgyjPlaceHolder () {
      if (!this.xms) {
        return '评分排名前0名的项目须填写修改意见'
      } else {
        return '评分排名前' + this.xms + '名的项目须填写修改意见'
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
            this.$alert(successResponse.data.msg)
            this.dialogFormVisible = false
            this.initData()
          } else if (successResponse.data.code === 400) {
            this.$alert('文件不存在')
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
        this.sumScore = ''
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
          this.yqScore = item.yqscore
          this.xms = item.xms
          if (item.pf) {
            if (!this.sumScore) {
              this.sumScore = 0
            }
            this.sumScore = parseFloat(item.pf) + parseFloat(this.sumScore)
          }
          if (item.istj === '1') {
            this.visible = false
            this.tjflag = true
          }
        })
        console.log('sumScore=' + this.sumScore)
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
            this.$alert(successResponse.data.msg)
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
      this.initData()
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
                this.$alert('评分保存失败,请检查网络是否可用')
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
            this.$alert(successResponse.data.msg)
            this.initData()
          } else {
            this.$alert('提交失败,请检查网络是否可用')
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
    },
    submitTime (formData) {
      this.$refs[formData].validate(
        async (valid) => {
          if (valid) {
            let sj = this.timeFormData.sj
            let fd = new FormData()
            fd.append('tjkssj', sj[0])
            fd.append('tjjssj', sj[1])
            fd.append('pjzf', this.timeFormData.pjzf)
            fd.append('xms', this.timeFormData.xms)
            this.$axios.post('/imp/saveControlTime', fd, {responseType: 'utf-8'}).then(successResponse => {
              if (successResponse.data.code === 200) {
                this.$alert(successResponse.data.msg)
                this.dialogTimeVisible = false
              } else {
                this.$alert('时间控制设置失败')
              }
            })
              .catch(failResponse => {
                console.log('')
              })
          }
        }
      )
    },
    openTime () {
      this.timeFormData.sj = ['2022-2-22 00:00:00', '2022-2-23 00:00:00']
      this.$axios.post('/imp/checkItemKz', {responseType: 'utf-8'}).then(successResponse => {
        if (successResponse.data.code === 200) {
          let parse = JSON.parse(successResponse.data.msg)
          console.log(parse)
          this.timeFormData.sj = [parse.tjkssj, parse.tjjssj]
          this.timeFormData.pjzf = parse.pjzf
          this.timeFormData.xms = parse.xms
          this.dialogTimeVisible = true
        } else {
          this.$alert('网络异常请重试')
        }
      })
        .catch(failResponse => {
          console.log('')
        })
    },
    toCancelSend () {
      this.$confirm('是否确定撤回提交?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let fd = new FormData()
        fd.append('psr', sessionStorage.getItem('userid'))
        this.$axios.post('/imp/toCancelSend', fd, {responseType: 'utf-8'}).then(successResponse => {
          if (successResponse.data.code === 200) {
            this.$alert(successResponse.data.msg)
            this.tjflag = false
            this.initData()
          } else {
            this.$alert('提交失败,请检查网络是否可用')
          }
        })
          .catch(failResponse => {
            console.log('')
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已放弃取消提交'
        })
      })
    }
  }
}
</script>

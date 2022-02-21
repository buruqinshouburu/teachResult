<template>
  <div>
    <el-form align='center'>
      <div>
        <el-table :data="tableData" id='buttonTable' style="width: 100%" cellpadding='0' border='1'>
          <tr>
            <td>
              <input type='button' onclick='impTRitem()' value='导入教学成果项目'/>
            </td>
            <td>
              <input type='button' onclick='' value='导入评分参照指标'/>
            </td>
            <td>
              <input type='button' onclick='' value='评分指标查看'/>
            </td>
          </tr>
          <tr>
            <td>
                <input type='file' value='' ref='file' id='file'  @change='getFile($event)' >
                <input type='button' style='width: 40px;height: 20px' v-on:click='impTRData' value='导 入 '/>
            </td>
            <td>
              <input type='button' style='width: 80px;height: 20px' v-on:click='expTemplate' value='模板下载 '/>
            </td>
          </tr>
        </el-table>
      </div>
      </el-form>
        <table id='formTable' border='1' cellpadding='0'>
          <colgroup width='5%'></colgroup>
          <colgroup width='20%'></colgroup>
          <colgroup width='20%'></colgroup>
          <colgroup width='10%'></colgroup>
          <colgroup width='10%'></colgroup>
          <colgroup width='5%'></colgroup>
          <colgroup width='10%'></colgroup>
          <colgroup width='20%'></colgroup>
          <tr>
            <td>序号</td>
            <td>成果名称</td>
            <td>主要完成人</td>
            <td>成果内容类别</td>
            <td>单位</td>
            <td>学院排序</td>
            <td>评分</td>
            <td>修改意见</td>
          </tr>
            <tr v-for="item in items" :key="item.id">
              <td>{{item.index}}</td>
              <td>{{item.cgmc}}</td>
              <td>{{item.zywcr}}</td>
              <td>{{item.cgnrlb}}</td>
              <td>{{item.dw}}</td>
              <td>{{item.xypx}}</td>
              <td>
                <input type="text" id="pf" name="pf"/>
              </td>
              <td>
                <input type="text" id="xgyj" name="xgyj"/>
              </td>
            </tr>
        </table>
  </div>
</template>

<script>
export default {
  name: 'AppIndex',
  data () {
    return {
      formData: new FormData(),
      items: [ ]
    }
  },
  mounted: function () {
    this.init()
  },
  methods: {
    impTRData (e) {
      if (!this.formData) {
        alert('请选择要导入的文件！')
        return
      }
      this.$axios.post('/imp/impTRData', this.formData, {responseType: 'utf-8'})
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert(successResponse.data.msg)
          } else if (successResponse.data.code === 400) {
            alert('文件不存在')
          }
        })
        .catch(failResponse => {
        })
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
    getFile (event) {
      let file = event.target.files[0]
      let fileName = file.name
      let index = fileName.lastIndexOf('.')
      if (index !== -1) {
        let suffix = fileName.substr(index + 1).toLowerCase()
        if (suffix === 'xls' || suffix === 'xlsx') {
          this.formData.append('file', file)
        } else {
          alert('文件只支持excel格式')
          this.$refs.file.value = ''
        }
      }
    },
    impTRData (e) {
      if (!this.formData) {
        alert('请选择要导入的文件！')
        return
      }
      this.$axios.post('/imp/impTRData', this.formData, {responseType: 'utf-8'})
        .then(successResponse => {
          if (successResponse.data.code === 200) {
            alert(successResponse.data.msg)
          } else if (successResponse.data.code === 400) {
            alert('文件不存在')
          }
        })
        .catch(failResponse => {
        })
    },
    init () {
      this.$axios.post('/imp/checkItem')
        .then(res => {
          this.items = res.data
        })
        .catch(failResponse => {
          console.log('')
        })
    }
  }
}

</script>
<style scoped></style>

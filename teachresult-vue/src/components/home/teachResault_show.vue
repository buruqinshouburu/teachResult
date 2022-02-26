<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;height: 100%">
      <el-col style="height: 100%">
        <el-header style="text-align: left;background-color:white">
          <el-button size="large" v-if="checkAuth" type="primary" @click="expTemplate">评分结果打印</el-button>
          <el-button size="large" v-if="checkAuth" type="primary" @click="staticScore">成绩统计</el-button>
          <el-button size="large" v-if="checkAuth" type="primary" @click="initData('1')">本科成果评分</el-button>
          <el-button size="large" v-if="checkAuth" type="primary" @click="initData('2')">研究生成果评分</el-button>
          <el-button size="large" v-if="checkAuth" type="primary" @click="initData()">全部成果评分</el-button>
        </el-header>
        <el-table :data="dataList" v-loading="loading"
                  style="width: 100%;background: transparent;overflow:auto;"
                  height="100%">
          <el-table-column prop="index" label="序号" type="index" min-width="80" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
            <el-input prop="TRItemid" type="hidden" autocomplete="off"></el-input>
          </el-table-column>
          <el-table-column prop="cgmc" label="成果名称"  min-width="400" align="center">
          </el-table-column>
          <el-table-column prop="dw" label="申报单位"  min-width="200" align="center">
          </el-table-column>
          <el-table-column prop="xypx" label="单位排序/上报项目" min-width="160" align="center">
          </el-table-column>
          <!-- 动态生成列 -->
          <el-table-column
            v-for="(item,index) in theadList"
            :key="index"
            label="***"
            width="150"
            prop="pf"
            align="center"
          >
            <template slot-scope="scope">
              <!--<div>{{scope.row.pf|showPf(item.psr)}}</div>-->
              <div>{{scope.row.pf|showPf(item.psr)}}</div>
            </template>
          </el-table-column>
          <!-- 动态生成列结束 -->
          <el-table-column v-if="checkAuth" fixed="right" min-width="100" sortable prop="avgScore" label="平均分"  align="center">
          </el-table-column>
          <el-table-column v-if="checkAuth"  fixed="right"  min-width="100" sortable prop="itemorder" label="推荐排序"  align="center">
          </el-table-column>
          <el-table-column v-if="checkAuth"  fixed="right"  min-width="120" sortable prop="absAvgScore" label="绝对平均分"  align="center">
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<style>

</style>

<script>
// import headTop from '../headTop'

export default {
  filters: {
    showPf (val, psr) {
      let dqpf = ''
      val.forEach(review => {
        if (review.psr === psr) {
          dqpf = review.pf
        }
      })
      return dqpf
    }
  },
  data () {
    return {
      theadList: [],
      dataList: [],
      loading: false
    }
  },
  /* components: {
    headTop
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
    async initData (type) {
      let url = '/imp/checkStatic'
      if (type) {
        url = '/imp/checkStatic?type=' + type
      }
      let fd = new FormData()
      fd.append('psr', sessionStorage.getItem('userid'))
      this.$axios.post(url, fd)
        .then(res => {
          if (res.data.length === 0) {
          } else {
            this.theadList = []
            this.dataList = []
            console.log(res.data)
            res.data.forEach(tritem => {
              if (this.theadList.length === 0) {
                this.theadList = tritem.reviewList
              }
              let tableItem = {
                cgmc: tritem.cgmc,
                dw: tritem.dw,
                xypx: tritem.xypx,
                avgScore: tritem.avgScore,
                absAvgScore: tritem.absAvgScore,
                itemorder: tritem.itemorder,
                TRItemid: tritem.id,
                pf: tritem.reviewList
              }
              this.dataList.push(tableItem)
            })
          }
        })
        .catch(failResponse => {
          console.log('获取数据失败', failResponse)
        })
    },
    expTemplate () {
      this.$axios.post('/imp/printItem?type=check', this.theadList, {responseType: 'blob'})
        .then(successResponse => {
          if (successResponse) {
            var fileDownload = require('js-file-download')
            fileDownload(successResponse.data, '教学成果评价统计.xls')
          }
        })
        .catch(failResponse => {
          console.log('文件下载失败')
        })
    },
    staticScore () {
      this.$confirm('统计只计算提交的数据,是否确认统计当前数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$axios.post('/imp/staticScore')
          .then(res => {
            if (res.data.code === 200) {
              this.$alert(res.data.msg)
              this.dataList = []
              this.initData()
              this.loading = false
            }
          })
          .catch(failResponse => {
            console.log('统计失败')
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
    /* async getTRData (data) {
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
    } */
  }
}
</script>

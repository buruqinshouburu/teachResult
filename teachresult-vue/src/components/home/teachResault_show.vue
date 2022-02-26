<template>
  <div style="width: 100%;height: 100%">
    <head-top></head-top>
    <el-row style="margin-top: 20px;height: 100%">
      <el-col style="height: 100%">
        <el-header style="text-align: left;background-color:white">
          <el-button size="large" v-if="checkAuth" type="primary" @click="expTemplate">评分结果打印</el-button>
          <el-button size="large" v-if="checkAuth" type="primary" @click="staticScore">成绩统计</el-button>
        </el-header>
        <el-table :data="dataList" v-loading="loading"
                  style="width: 100%;background: transparent;overflow:auto;"
                  height="100%">
          <el-table-column prop="index" label="序号" type="index" width="80" align="center">
            <template slot-scope="scope">
              <span>{{scope.$index + 1}}</span>
            </template>
            <el-input prop="TRItemid" type="hidden" autocomplete="off"></el-input>
          </el-table-column>
          <el-table-column prop="cgmc" label="成果名称"  width="400" align="center">
          </el-table-column>
          <el-table-column prop="xypx" label="学院排序" width="80" align="center">
          </el-table-column>
          <!-- 动态生成列 -->
          <el-table-column
            v-for="(item,index) in theadList"
            :key="index"
            :label="item.user.username"
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
          <el-table-column v-if="checkAuth" prop="avgScore" label="平均分"  align="center">
          </el-table-column>
          <el-table-column v-if="checkAuth" prop="itemorder" label="推荐排序"  align="center">
          </el-table-column>
          <el-table-column v-if="checkAuth" prop="absAvgScore" label="绝对平均分"  align="center">
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<style>
</style>

<script>
import headTop from '../headTop'

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
      fd.append('psr', sessionStorage.getItem('userid'))
      this.$axios.post('/imp/checkStatic', fd)
        .then(res => {
          if (res.data.length === 0) {
          } else {
            res.data.forEach(tritem => {
              if (this.theadList.length === 0) {
                this.theadList = tritem.reviewList
              }
              let tableItem = {
                cgmc: tritem.cgmc,
                xypx: tritem.xypx,
                avgScore: tritem.avgScore,
                absAvgScore: tritem.absAvgScore,
                itemorder: tritem.itemorder,
                TRItemid: tritem.id,
                pf: tritem.reviewList
              }
              this.dataList.push(tableItem)
            })
            /* this.theadList = res.dateArr
            // res.rows中每项的colprops属性中包启有所有表头[2019,2018,2017,2016]列对应，用filter后就可以将对应的值取出处理
            // 如res.rows[0]为一个{
            //    colprops: ['8%', '9.6%', '6.4%', '6%']，
            // }
            this.dataList = res.rows
            this.getTRData(res.data) */
          }
        })
        .catch(failResponse => {
          console.log('获取数据失败', failResponse)
        })
    },
    expTemplate () {
      this.$axios.post('/imp/printItem', this.theadList, {responseType: 'blob'})
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
              alert(res.data.msg)
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

<template>
  <div>
    <div class="historyLabel">
      <el-input v-model="serchForm.word" placeholder="标题/描述" style="display: inline-block;width: 300px;padding: 10px"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="getList">搜索</el-button>

      <span style="float: right;margin-right: 180px">
        <el-button type="primary" @click="refreshData">同步实时数据</el-button>
      </span>
      <div id="historyData" class="historyCanvas"/>
    </div>

    <el-dialog
      :title="this.viewingReq.name"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      @open="open"
      z-index="999999"
      center
    >
      <span id="viewingReqBar" class="viewingReqBar"/>
    </el-dialog>
  </div>
</template>

<script>
import echarts from 'echarts'
import {requestByClient} from '@/utils/HttpUtils'
import {supplierConsumer} from '@/utils/HttpUtils'
import {Loading} from 'element-ui'

export default {
  name: "ReqAnalysis",

  data() {
    return {
      serchForm: {
        year: new Date().getFullYear(),
        word: ''
      },
      dialogVisible: false,
      viewingReq: {},
      lastFiveYears: [],
      viewingList: []
    }
  },
  watch: {
    '$store.state.settings.editYear': 'getList'
  },
  mounted() {
    this.getList()
  },
  methods: {
    open() {
      this.$nextTick(() => {
        this.setViewingChart()
      })
    },
    setViewingChart() {
      let chartDom = document.getElementById('viewingReqBar');
      const myChart = echarts.init(chartDom)
      let option
      let targets = this.viewingReq.targets
      let colors = [
        '#199237',
        '#196292',
        '#c11a9d',
        '#e5da14',
        '#b89220',
        '#1c977a',
        '#9a5a2b',
      ]


      let names = targets.map(i => {
        return i.discribe
      })

      let sysScores = targets.map(i => {
        return (i.sysGrade * 100).toFixed(2)
      })

      let stuScores = targets.map(i => {
        return (i.stuGrade * 100).toFixed(2)
      })

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        dataZoom: [
          {
            id: 'dataZoomX',
            type: 'slider',
            xAxisIndex: [0],
            filterMode: 'filter'
          }
        ],
        calculable: true,
        title: {
          text: '支撑该毕业要求的指标点成绩',
          subtext: ''
        },
        xAxis: {
          type: 'category',
          data: names,
          axisLabel: {
            interval: 0,
            rotate: 30
          }
        },
        yAxis: {
          type: 'value',
          max: 100
        },
        series: [{
          name: '系统成绩',
          data: sysScores,
          type: 'bar',
          barGap: 0,
          itemStyle: {
            normal: {
              color: '#00216c'
            }
          },
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180,180,180,0.2)'
          }
        },{
          name: '学生评价',
          data: stuScores,
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#ba0028'
            }
          },
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180,180,180,0.2)'
          }
        }]
      }
      option && myChart.setOption(option)
    },
    refreshData() {
      const loadingInstance = Loading.service({
        background: 'rgba(0, 0, 0, 0.7)',
        text: '加载中，请稍后。。。',
        target: 'document.body',
        body: true
      })
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/summaryAll', {
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.$message({
            message: '数据已刷新',
            type: 'success'
          })
          this.getList()
          // 关闭加载动画
          this.$nextTick(() => {
            loadingInstance.close()
          })
        }
        this.loading = false
      })
    },
    getList() {
      this.dialogVisible = false
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/list', {
        year: this.$store.state.settings.editYear,
        word: this.serchForm.word
      }, res => {
        if (res.data.succ) {
          this.viewingList = res.data.data
          this.setChartData()
        }
        this.loading = false
      })
    },
    setChartData() {
      let vue = this
      let data = this.viewingList
      let reqs = data.map(i => {
        return i.name
      })
      let sysScores = data.map(i => {
        return (i.sysGrade * 100).toFixed(2)
      })
      let stuScores = data.map(i => {
        return (i.stuGrade * 100).toFixed(2)
      })
      const chartDom = document.getElementById('historyData')
      const myChart = echarts.init(chartDom)
      let option
      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            crossStyle: {
              color: '#999'
            }
          }
        },
        dataZoom: [
          {
            id: 'dataZoomX',
            type: 'slider',
            xAxisIndex: [0],
            filterMode: 'filter'
          }
        ],
        toolbox: {
          show: true,
          feature: {
            magicType: {show: true, type: ['line', 'bar']},
            restore: {show: true},
            saveAsImage: {show: true}
          }
        },
        calculable: true,
        title: {
          text: '毕业要求成绩统计',
          subtext: ''
        },
        xAxis: {
          type: 'category',
          data: reqs,
          axisLabel: {
            interval: 0,
            rotate: 40
          }
        },
        yAxis: {
          type: 'value',
          max: 100
        },
        series: [{
          name: '系统成绩',
          data: sysScores,
          type: 'bar',
          barGap: 0,
          itemStyle: {
            normal: {
              color: '#bb002b'
            }
          },
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180,180,180,0.2)'
          },
          markPoint: {
            data: [
              {type: 'max', name: '最大值'}
            ]
          },
          markLine: {
            data: [
              {type: 'average', name: '平均值'}
            ]
          }
        }, {
          name: '学生评价',
          data: stuScores,
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#00238d'
            }
          },
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180,180,180,0.2)'
          },
          markPoint: {
            data: [
              {type: 'max', name: '最大值'}
            ]
          },
          markLine: {
            data: [
              {type: 'average', name: '平均值'}
            ]
          }
        }
        ]
      }
      option && myChart.setOption(option)
      //点击事件
      myChart.on('click', function (params) {
        vue.selectOneReq(vue.viewingList[params.dataIndex].id)
      });
    },
    selectOneReq(id) {
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/getOne', {
        id: id
      }, res => {
        this.viewingReq = res.data.data
        this.dialogVisible = true
      })
    }
  }
}
</script>

<style scoped>
.historyLabel {
  padding: 20px;
  margin: 20px;
  width: 97%;
  height: 830px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .05)
}

.historyCanvas {
  width: 100%;
  height: 700px;
  padding: 0;
  margin: 0;
}
.viewingReqBar{
  width: 550px;
  height: 600px;
  padding: 10px;
  display: inline-block;
}
</style>

<template>
  <div>
    <div class="historyLabel">
      <el-select v-model="serchForm.reqId" placeholder="关联毕业要求" clearable filterable style="width: 300px;padding: 3px">
        <el-option v-for="(item,index) in reqs" :key="index" :label="item.name" :value="item.id" />
      </el-select>
      <el-input v-model="serchForm.word" placeholder="描述" style="display: inline-block;width: 300px;padding: 3px"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="getList">搜索</el-button>

      <span style="float: right;margin-right: 180px">
        <el-button type="primary" @click="refreshData">同步实时数据</el-button>
      </span>
      <div id="historyData" class="historyCanvas"/>
    </div>
    <el-dialog
      :title="targetChartForm.title"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="95%"
      top="50px"
      @open="open"
      z-index="999999"
      center
    >
      <span id="targetPie" class="targetPie"/>
      <span id="targetBar" class="targetBar"/>
    </el-dialog>

  </div>
</template>

<script>
import echarts from 'echarts'
import {requestByClient} from '@/utils/HttpUtils'
import {supplierConsumer} from '@/utils/HttpUtils'
import {Loading} from 'element-ui'

export default {
  name: "TargetsAnalysis",
  data() {
    return {
      serchForm: {
        id: '',
        word:'',
        reqId:''
      },
      options: [],
      dialogVisible: false,
      targetChartForm: {
        title: '',
        score: '',
        courseTargets: [],
        courseTasks: []
      },
      reqs:[],
      lastFiveYears: [],
      viewingTargetsList: []
    }
  },
  watch: {
    '$store.state.settings.editYear': 'getList'
  },
  mounted() {
    this.getList()
  },
  methods: {
    getReqList(){
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/list', {
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.reqs = res.data.data
        }
      })
    },
    getList() {
      this.dialogVisible = false
      this.getReqList()
      requestByClient(supplierConsumer, 'POST', 'target/list', {
        year: this.$store.state.settings.editYear,
        id: this.serchForm.id,
        word: this.serchForm.word,
        reqId: this.serchForm.reqId
      }, res => {
        if (res.data.succ) {
          this.viewingTargetsList = res.data.data
          this.setChartData()
        }
        this.loading = false
      })
    },
    setChartData() {
      let vue = this
      let data = this.viewingTargetsList
      let names = data.map(i => {
        return i.discribe
      })
      let stuGrades = data.map(i => {
        return (i.stuGrade*100).toFixed(2)
      })
      let sysGrades = data.map(i => {
        return (i.sysGrade*100).toFixed(2)
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
          text: '指标点成绩对比分析',
          subtext: ''
        },
        xAxis: {
          type: 'category',
          data: names,
          axisLabel: {
            interval: 0,
            rotate: 90
          }
        },
        yAxis: {
          type: 'value',
          max: 100
        },
        series: [{
          name: '系统成绩',
          data: sysGrades,
          type: 'bar',
          barGap: 0,
          itemStyle: {
            normal: {
              color: '#ba0028'
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
          data: stuGrades,
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#00216c'
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
        }]
      }
      option && myChart.setOption(option)
      //点击事件
      myChart.on('click', function (params) {
        vue.handleChange(vue.viewingTargetsList[params.dataIndex].id)
      });
    },
    open() {
      this.$nextTick(() => {
        this.setTargetChart()
        this.setTargetChartBar()
      })
    },
    handleChange(value) {
      requestByClient(supplierConsumer, 'POST', 'target/getOne', {
        id: value
      }, res => {
        if (res.data.succ) {
          if (res.data.data.sysGrade > 0) {
            this.targetChartForm.title = res.data.data.discribe + '(' + (res.data.data.sysGrade * 100).toFixed(2) + ')'
            this.targetChartForm.score = res.data.data.sysGrade
            requestByClient(supplierConsumer, 'POST', 'courseTarget/voList', {
              targetId: value
            }, res => {
              if (res.data.succ) {
                this.targetChartForm.courseTargets = res.data.data
                requestByClient(supplierConsumer, 'POST', 'courseTask/voList', {
                  targetId: value,
                }, res => {
                  if (res.data.succ) {
                    this.targetChartForm.courseTasks = res.data.data
                    this.dialogVisible = true
                  }
                })
              }
            })
          } else {
            this.$message({
              message: '该指标点暂无成绩',
              type: 'warning'
            })
            return false
          }
        }
      })

    },
    setTargetChartBar() {
      let chartDom = document.getElementById('targetBar');
      const myChart = echarts.init(chartDom)
      let option
      let courseTasks = this.targetChartForm.courseTasks
      let colors = [
        '#199237',
        '#196292',
        '#c11a9d',
        '#e5da14',
        '#b89220',
        '#1c977a',
        '#9a5a2b',
      ]
      let tasksName = courseTasks.map(i => {
        return i.course.name + ':' + i.describes
      })

      let tasksScores = courseTasks.map(i => {
        return (i.sysGrade * 100).toFixed(2)
      })

      let stuScores = courseTasks.map(i => {
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
          text: '其支撑课程目标',
          subtext: ''
        },
        xAxis: {
          type: 'category',
          data: tasksName,
          axisLabel: {
            interval: 0,
            rotate: 90
          }
        },
        yAxis: {
          type: 'value',
          max: 100
        },
        series: [{
          name: '系统成绩',
          data: tasksScores,
          type: 'bar',
          barGap: 0,
          itemStyle: {
            normal: {
              color: '#b60092'
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
              color: '#0551bf'
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
    setTargetChart() {
      let chartDom = document.getElementById('targetPie');
      let myChart = echarts.init(chartDom);
      let option;
      let courseTargets = this.targetChartForm.courseTargets
      let dtataNames = []
      let chartData = new Array(courseTargets.length)
      for (let courseTarget of courseTargets) {
        dtataNames.push(courseTarget.course.name)
        chartData.push({
          value: courseTarget.mix.toFixed(2),
          name: courseTarget.course.name
        })
      }
      let courseTasks = this.targetChartForm.courseTasks
      let tasksDta = new Array(courseTasks.length)
      for (let courseTask of courseTasks) {
        dtataNames.push(courseTask.describes)
        let courseTaskMix = 0
        for (let courseTarget of courseTargets) {
          if (courseTask.course.id === courseTarget.course.id) {
            courseTaskMix = courseTask.mix * courseTarget.mix
          }
        }
        tasksDta.push({
          value: courseTaskMix.toFixed(2),
          name: courseTask.describes
        })
      }
      option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} <br/>权重: {c} <br/>占比:({d}%)'
        },
        legend: {
          data: dtataNames,
          top: 'bottom'
        },
        series: [
          {
            name: '课程支撑权重',
            type: 'pie',
            selectedMode: 'single',
            radius: ['5%', '30%'],
            label: {
              position: 'inner',
              fontSize: 14,
            },
            labelLine: {
              show: false
            },
            data: chartData
          },
          {
            name: '课程目标',
            type: 'pie',
            radius: ['45%', '60%'],
            labelLine: {
              length: 30,
            },
            label: {
              formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
              backgroundColor: '#F6F8FC',
              borderColor: '#8C8D8E',
              borderWidth: 1,
              borderRadius: 4,

              rich: {
                a: {
                  color: '#6E7079',
                  lineHeight: 22,
                  align: 'center'
                },
                hr: {
                  borderColor: '#8C8D8E',
                  width: '100%',
                  borderWidth: 1,
                  height: 0
                },
                b: {
                  color: '#4C5058',
                  fontSize: 14,
                  fontWeight: 'bold',
                  lineHeight: 33
                },
                per: {
                  color: '#fff',
                  backgroundColor: '#4C5058',
                  padding: [3, 4],
                  borderRadius: 4
                }
              }
            },
            data: tasksDta
          }
        ]
      };

      option && myChart.setOption(option)
    },
    refreshData() {
      const loadingInstance = Loading.service({
        background: 'rgba(0, 0, 0, 0.7)',
        text: '加载中，请稍后。。。',
        target: 'document.body',
        body: true
      })
      requestByClient(supplierConsumer, 'POST', 'target/summaryAll', {
        year: 2021
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

.targetPie {
  width: 1300px;
  height: 750px;
  padding: 10px;
  display: inline-block;
}

.targetBar {
  margin-left: 50px;
  width: 400px;
  height: 750px;
  padding: 10px;
  display: inline-block;
}
</style>


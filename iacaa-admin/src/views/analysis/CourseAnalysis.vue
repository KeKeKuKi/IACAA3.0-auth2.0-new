<template>
  <div>
    <div class="historyLabel">
      <el-select v-model="serchForm.courseId" placeholder="关联课程" clearable filterable style="width: 200px;padding: 3px">
        <el-option v-for="(item,index) in this.allCourse" :key="index" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="serchForm.targetId" placeholder="关联指标点" clearable filterable style="width: 300px;padding: 3px">
        <el-option v-for="(item,index) in this.allTarget" :key="index" :label="item.discribe" :value="item.id" />
      </el-select>
      <el-input v-model="serchForm.word" placeholder="描述" style="display: inline-block;width: 300px;padding: 3px"></el-input>
      <el-button type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
      <span style="float: right;margin-right: 180px">
        <el-button type="primary" @click="refreshData">同步实时数据</el-button>
      </span>
      <div id="historyData" class="historyCanvas"/>
    </div>
    <el-dialog
      :title="'课程目标:' + viewingCourseTask.describes"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="75%"
      @open="open"
      center
    >
      <div class="elinkChart">
        <div id="stuTaskScoreBar" class="stuElinkScoreBar"></div>
        <div id="sysElinkMixPie" class="sysElinkMixPie"></div>
      </div>
      <div id="sysElinkScoreBar" class="sysElinkScoreBar"></div>
    </el-dialog>
  </div>
</template>

<script>
import echarts from 'echarts'
import {requestByClient, supplierConsumer} from '@/utils/HttpUtils'
import {Loading} from 'element-ui'

export default {
  name: "CourseAnalysis",
  data() {
    return {
      viewingCourseTask: {},
      lastFiveYear:[],
      dialogVisible: false,
      allCourse: [],
      allTarget: [],
      serchForm: {
        courseId: '',
        targetId: '',
        id: '',
        word: ''
      },
      viewingCourseTaskList: []
    }
  },
  mounted() {
    this.getList()
    this.getAllTarget()
    this.getAllCourse()
  },
  watch: {
    '$store.state.settings.editYear'(val, oldVal){
      this.getList()
      this.getAllTarget()
    }
  },
  methods: {
    getAllCourse(){
      requestByClient(supplierConsumer, 'POST', 'course/list', {}, res => {
        if (res.data.succ) {
          this.allCourse = res.data.data
        }
      })
    },
    getAllTarget(){
      requestByClient(supplierConsumer, 'POST', 'target/list', {
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.allTarget = res.data.data
        }
      })
    },
    refreshData() {
      const loadingInstance = Loading.service({
        background: 'rgba(0, 0, 0, 0.7)',
        text: '加载中，请稍后。。。',
        target: 'document.body',
        body: true
      })
      requestByClient(supplierConsumer, 'POST', 'courseTask/summaryCourseTask', {
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
      requestByClient(supplierConsumer, 'POST', 'courseTask/voList', {
        year: this.$store.state.settings.editYear,
        courseId: this.serchForm.courseId,
        targetId: this.serchForm.targetId,
        id: this.serchForm.id,
        word: this.serchForm.word
      }, res => {
        if (res.data.succ) {
          this.viewingCourseTaskList = res.data.data
          this.setChartData()
        }
        this.loading = false
      })
    },
    setChartData() {
      let vue = this
      let data = this.viewingCourseTaskList
      let courseTasksName = data.map(i => {
        return (i.course.name + ':' + i.describes)
      })
      let sysScores = data.map(i => {
        return i.sysGrade ? ((i.sysGrade) * 100).toFixed(2) : 0
      })
      let stuScores = data.map(i => {
        return i.stuGrade ? ((i.stuGrade) * 100).toFixed(2) : 0
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
          text: '课程目标成绩对比分析',
          subtext: ''
        },
        xAxis: {
          type: 'category',
          data: courseTasksName,
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
          data: sysScores,
          barGap: 0,
          type: 'bar',
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
          data: stuScores,
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
        }
        ]
      }
      option && myChart.setOption(option)
      //点击事件
      myChart.on('click', function (params) {
        vue.selectOneCourseTask(vue.viewingCourseTaskList[params.dataIndex].id)
      });
    },
    selectOneCourseTask(id) {
      requestByClient(supplierConsumer, 'POST', 'courseTask/getOne', {
        id: id
      }, res => {
        this.viewingCourseTask = res.data.data
        this.dialogVisible = true
      })
    },
    open() {
      this.$nextTick(() => {
        this.setStuTaskScoreBar()
        this.setsysElinkMixPie()
      })
    },
    setStuTaskScoreBar() {
      requestByClient(supplierConsumer, 'POST', 'stuEvaluation/statisticsByCourseTaskId', {
        id: this.viewingCourseTask.id
      }, res => {
        let data = res.data.data
        let options = ['很差','差','一般','好','很好']
        let nams = data.map(i => {
          return options[i.score - 1]
        })
        let counts = data.map(i => {
          return i.count
        })
        let chartDom1 = document.getElementById('stuTaskScoreBar');
        let myChart1 = echarts.init(chartDom1);
        let option1;
        option1 = {
          title: {
            text: '学生评价成绩分布',
            subtext: ''
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
            show: false
          },
          yAxis: {
            type: 'category',
            data: nams
          },
          series: [
            {
              name: '评价次数:',
              type: 'bar',
              data: counts,
              itemStyle: {
                normal: {
                  color: '#017180'
                }
              },
            }
          ]
        };
        option1 && myChart1.setOption(option1);
      })
    },
    setsysElinkMixPie() {
      requestByClient(supplierConsumer, 'POST', 'courseTaskCheckLink/voList', {
        courseTaskId: this.viewingCourseTask.id
      }, res => {
        let data = res.data.data
        let nams = data.map(i => {
          return i.checkLink.name
        })
        let counts = data.map(i => {
          return ((i.checkLink.averageScore / i.checkLink.targetScore) * 100).toFixed(2)
        })
        let chartDom1 = document.getElementById('sysElinkScoreBar');
        let myChart1 = echarts.init(chartDom1);
        let option1;
        option1 = {
          title: {
            text: '考核环节成绩分布',
            subtext: ''
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            data: ['2012年']
          },
          yAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
            max: 100
          },
          xAxis: {
            type: 'category',
            data: nams,
          },
          series: [
            {
              name: '',
              type: 'bar',
              data: counts,
              itemStyle: {
                normal: {
                  color: '#32006a'
                }
              },
            }
          ]
        };
        option1 && myChart1.setOption(option1);


        let chartDom2 = document.getElementById('sysElinkMixPie');
        let myChart2 = echarts.init(chartDom2);
        let option2;
        let pieData = data.map(i => {
          return {
            value: i.mix,
            name: i.checkLink.name
          }
        })

        option2 = {
          title: {
            text: '此课程目标各考核环节权重',
            subtext: '',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left',
          },
          series: [
            {
              name: '',
              type: 'pie',
              radius: '50%',
              data: pieData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };

        option2 && myChart2.setOption(option2);
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
  box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .05);
}

.historyCanvas {
  width: 100%;
  height: 90%;
}

.elinkChart {
  display: inline-block;
  width: 60%;
}

.stuElinkScoreBar {
  width: 100%;
  height: 240px;
}

.sysElinkMixPie {
  width: 100%;
  height: 370px;
}

.sysElinkScoreBar {
  display: inline-block;
  width: 40%;
  height: 600px;
}

</style>

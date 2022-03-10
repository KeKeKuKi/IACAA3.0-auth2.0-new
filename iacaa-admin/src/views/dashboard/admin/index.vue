<template>
  <div class="dashboard-editor-container">
    <el-card>
      <div slot="header" style="display: flex; justify-content: space-between;">
        <span>运行报表</span>
        <el-popover
          v-model="visible"
          placement="left-start"
        >
          <el-date-picker
            v-model="value2"
            type="datetimerange"
            :picker-options="pickerOptions"
            range-separator=" - "
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH:mm:ss"
            align="right"
            size="small"
            @change="dateChange"
          />
          <el-button slot="reference" type="primary" icon="el-icon-date" />
        </el-popover>
      </div>
      <panel-group :chart-data="counts" />

      <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
        <el-col :xs="24" :sm="24" :lg="16">
          <line-chart :chart-data="lineChartData" />
        </el-col>
        <el-col :xs="24" :sm="24" :lg="8">
          <div class="chart-wrapper">
            <pie-chart :chart-data="pieChart" />
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import PieChart from './components/PieChart'
// import { requestByClient } from '../../../utils/HttpUtils'
// import { Xxl } from '../../../utils/HttpUtils'

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart,
    PieChart
  },
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      value2: '',
      visible: false,
      pieChart: {
        legendData: [],
        values: []
      },
      counts: {
        groupCount: 0,
        jobCount: 0,
        triggerTimes: 0
      },
      lineChartData: {
        xAxis: {
          triggerDayList: [],
          triggerDayCountRunningList: [],
          triggerDayCountSucList: [],
          triggerDayCountFailList: []
        }
      }
    }
  },
  created() {
    this.checkChartData(0, 0)
  },
  methods: {
    dateChange(value) {
      const start = value[0]
      const end = value[1]
      this.checkChartData(new Date(start).getTime(), new Date(end).getTime())
    },
    checkChartData(start, end) {
      // let startDate
      // let endDate
      // if (start === 0 || end === 0) {
      //   const today = new Date()
      //   const recentlyWeek = new Date(today.getTime() - 6 * 24 * 60 * 60 * 1000)
      //   startDate = '' + recentlyWeek.getTime()
      //   endDate = '' + today.getTime()
      // } else {
      //   startDate = start
      //   endDate = end
      // }
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>

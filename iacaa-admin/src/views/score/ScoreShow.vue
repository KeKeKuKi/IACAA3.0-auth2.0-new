<template>
  <div style="padding: 20px">
    <el-form :inline="true" :model="serchForm" class="demo-form-inline" style="height: 50px">
      <el-form-item label="">
        <el-input v-model="serchForm.word" placeholder="课程名称" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getCourseList">查询</el-button>
      </el-form-item>
    </el-form>

    <el-table
      ref="multipleTable"
      :data="courses"
      style="width: 100%"
      height="750"
      tooltip-effect="dark"
    >
      <el-table-column
        prop="id"
        label="课程编号"
        width="100">
      </el-table-column>
      <el-table-column
        prop="name"
        label="名称"
        width="200">
      </el-table-column>
      <el-table-column
        prop="image"
        label="简介"
        width="400">
      </el-table-column>
      <el-table-column prop="courseTasks" type="expand" label="考核环节" width="1000">
        <template slot-scope="courseScope">
          <el-table :data="courseScope.row.checkLinks" stripe>
            <el-table-column
              prop="year"
              label="年份"
              width="200"
            />
            <el-table-column
              prop="name"
              label="考核环节"
              width="400"
            />
            <el-table-column
              prop="createdDate"
              label="创建时间"
              width="200"
            />
            <el-table-column
              prop="updateDate"
              label="最终更新时间"
              width="200"
            />
            <el-table-column label="操作" prop="courseTasks">
              <template slot-scope="checkLinkScope">
                <el-button :disabled="courseScope.row.editStatus === 0" v-if="checkLinkScope.row.year === new Date().getFullYear()"
                           type="primary" @click="handleCheckLinkEditForm(courseScope.row, checkLinkScope.row)" >查看成绩</el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
    </el-table>


  </div>
</template>

<script>
import {requestByClient, supplierConsumer} from "@/utils/HttpUtils";

export default {
  name: "ScoreShow",
  data(){
    return{
      dialogVisible: false,
      pageSize: 20,
      total: 0,
      currentPage: 1,
      tableData: [],
      courses: [],
      serchForm: {
        word: '',
        year: ''
      },
      ckeckLinkEditForm: {
        course: {},
        checkLink: {},
        stuScores: []
      }
    }
  },
  mounted() {
    this.getCourseList()
  },
  methods:{
    getCourseList() {
      requestByClient(supplierConsumer, 'POST', 'course/voList', {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        word: this.serchForm.word
      }, res => {
        if (res.data.succ) {
          this.courses = res.data.data.list
          this.total = res.data.data.total
          this.pageSize = res.data.data.pageSize
          this.currentPage = res.data.data.pageNum
        }
      })
    },
  }
}
</script>

<style scoped>

</style>

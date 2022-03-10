<template>
  <div style="padding: 20px">
    <el-form :inline="true" :model="serchForm" class="demo-form-inline" style="height: 50px">
      <el-form-item label="">
        <el-input v-model="serchForm.word" placeholder="课程名称" clearable/>
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
        width="200"
      />
      <el-table-column
        prop="name"
        label="课程名称"
        width="200"
      />
      <el-table-column
        prop="image"
        label="课程描述"
        width="500"
      />
      <el-table-column
        prop=""
        label="课程操作"
        width=""
      >
        <template slot-scope="courseCope">
          <el-button type="primary" icon="el-icon-edit" @click="courseTaskList(courseCope.row)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="课程目标列表"
      :visible.sync="dialogVisible1"
      :close-on-click-modal="false"
      width="60%"
      center>
      <el-table
        ref="multipleTable"
        :data="viewingCourseTasks"
        style="width: 100%"
        tooltip-effect="dark"
      >
        <el-table-column
          type="index"
          label="序号"
          width="200"
        />
        <el-table-column
          prop="year"
          label="年份"
          width="200"
        />
        <el-table-column
          prop="describes"
          label="课程目标描述"
          width="500"
        />
        <el-table-column
          prop=""
          label="课程操作"
          width=""
        >
          <template slot-scope="courseTaskScope">
            <el-button type="primary" icon="el-icon-edit" @click="handleCheckLinkEditForm(editingCourse,courseTaskScope.row)">编辑考核环节</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog
      title="关联课程目标"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center>
      <div>
        <el-form ref="ruleForm" :model="ckeckLinkEditForm" status-icon class="demo-ruleForm">
          <el-form-item label="课程" prop="name">
            <el-input v-model="ckeckLinkEditForm.courseName" disabled type="text" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="课程目标" prop="pass">
            <el-input v-model="ckeckLinkEditForm.courseTask.describes" disabled type="text" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="考核环节：" prop="pass">
            <el-button type="primary" round style="" @click="handleAddCheckLink">添加</el-button>
            <br>
            <el-table
              ref="multipleTable"
              style="width: 100%"
              height="50"
              tooltip-effect="dark">
              <el-table-column
                prop=""
                label="考核环节"
                width="230">
              </el-table-column>
              <el-table-column
                prop=""
                label="权重系数">
              </el-table-column>
            </el-table>
            <!--eslint-disable-next-line-->
            <span v-for="(item,index) in ckeckLinkEditForm.courseTaskCheckLinks" type="text" autocomplete="off">
              <el-select v-model="item.checkLinkId" placeholder="考核环节" clearable filterable
                         style="width: 50%;margin-top: 10px">
                <el-option v-for="(item1,index1) in ckeckLinkEditForm.ableCheckLinks" :label="item1.name"
                           :value="item1.id"/>
              </el-select>
              <el-input-number v-model="item.mix" :min="0.1" :max="1" step="0.1" label="权重系数"
                               style="width: 35%;margin-top: 10px"/>
              <el-button type="danger" icon="el-icon-delete" circle @click="deleteDiscribe(index)"
                         style="margin-left: 10px"/>
            </span>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCheckLinksForm">确 定</el-button>
      </div>
    </el-dialog>
    <el-pagination
      :current-page="currentPage"
      :page-sizes="[10, 15, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>

</template>

<script>
import {requestByClient, supplierConsumer} from "@/utils/HttpUtils";

export default {
  name: "CheckLinks",
  data() {
    return {
      year: 2021,
      dialogVisible: false,
      dialogVisible1: false,
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
        courseName: '',
        courseTask: {},
        ableCheckLinks: [],
        courseTaskCheckLinks: []
      },
      viewingCourseTasks: [],
      editingCourse: {}
    }
  },
  watch: {
    '$store.state.settings.editYear': 'getCourseList'
  },
  mounted() {
    this.getCourseList()
  },
  methods: {
    courseTaskList(courseCope){
      this.editingCourse = courseCope
      requestByClient(supplierConsumer, 'POST', 'courseTask/list', {
        courseId: courseCope.id,
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.dialogVisible1 = true
          this.viewingCourseTasks = res.data.data
        }
      })
    },
    getCourseList() {
      this.dialogVisible = false
      this.dialogVisible1 = false
      requestByClient(supplierConsumer, 'POST', 'course/authList', {
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
    handleCheckLinkEditForm(course, courseTask) {
      this.ckeckLinkEditForm.courseTask = courseTask
      this.ckeckLinkEditForm.courseName = course.name
      requestByClient(supplierConsumer, 'POST', 'checkLink/list', {
        courseId: course.id,
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.ckeckLinkEditForm.ableCheckLinks = res.data.data
        }
      })
      requestByClient(supplierConsumer, 'POST', 'courseTaskCheckLink/voList', {
        courseTaskId: courseTask.id
      }, res => {
        if (res.data.succ) {
          this.ckeckLinkEditForm.courseTaskCheckLinks = res.data.data
          this.dialogVisible = true
        }
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.getCourseList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getCourseList()
    },
    deleteDiscribe(index) {
      var check = this.ckeckLinkEditForm.courseTaskCheckLinks[index]
      if (check.id) {
        requestByClient(supplierConsumer, 'POST', 'courseTaskCheckLink/delete', {
          id: check.id
        }, res => {
          if (res.data.succ) {
            this.$message({
              message: '已删除',
              type: 'success'
            });
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }
      this.ckeckLinkEditForm.courseTaskCheckLinks.splice(index, 1)
    },
    handleAddCheckLink() {
      this.ckeckLinkEditForm.courseTaskCheckLinks.push({
        checkLinkId: '',
        mix: '',
        courseTaskId: this.ckeckLinkEditForm.courseTask.id
      })
    },
    submitCheckLinksForm() {
      let checkLinks = this.ckeckLinkEditForm.courseTaskCheckLinks
      for (let checkLink of checkLinks) {
        if (checkLink.checkLinkId === '') {
          this.$message({
            message: '考核环节不能为空',
            type: 'error'
          });
          return false
        }
        if (checkLink.mix === '' || checkLink.mix < 0) {
          this.$message({
            message: '权重不能为空切大于零',
            type: 'error'
          });
          return false
        }
      }
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'courseTaskCheckLink/saveOrUpdate', this.ckeckLinkEditForm.courseTaskCheckLinks, res => {
        if (res.data.succ) {
          this.dialogVisible = false;
          this.$message({
            message: '修改成功',
            type: 'success'
          });
        } else {
          this.$message.error(res.data.msg);
        }
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>

</style>

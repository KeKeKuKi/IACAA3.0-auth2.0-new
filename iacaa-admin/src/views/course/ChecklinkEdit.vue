<template>
  <div style="padding: 20px">
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 100%"
      height="750"
      tooltip-effect="dark">
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
        width="600">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" :disabled="scope.row.editStatus === 0" @click="handleEditForm(scope.row)">编辑课程考核环节</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="考核环节编辑"
      :close-on-click-modal="false"
      :visible.sync="dialogVisible"
      width="45%"
      center>
      <div>
        <el-form :model="editingForm" status-icon ref="ruleForm" class="demo-ruleForm">
          <el-form-item label="课程名称" prop="pass">
            <div style="font-size: 18px;color: #1a1a1a">{{ editingForm.editingCourse.name }}</div>
          </el-form-item>
          <el-form-item label="考核环节：" prop="pass">
            <el-button type="primary" round style="" @click="handleAddChecklink">添加</el-button>
            <br>
            <el-table
              ref="multipleTable"
              style="width: 100%"
              height="50"
              tooltip-effect="dark">
              <el-table-column
                prop=""
                label="考核环节"
                width="480">
              </el-table-column>
              <el-table-column
                prop=""
                label="目标分数">
              </el-table-column>
            </el-table>
            <span v-for="(item,index) in editingForm.checkLinks" type="text" autocomplete="off">
              <el-select v-model="item.name" placeholder="课程目标" clearable filterable style="width: 60%;margin-top: 10px">
                <el-option label="期末考试" value="期末考试" />
                <el-option label="期中考试" value="期中考试" />
                <el-option label="日常作业" value="日常作业" />
                <el-option label="课堂表现" value="课堂表现" />
                <el-option label="日常考勤" value="日常考勤" />
              </el-select>
              <el-input type="text" autocomplete="off" v-model="item.targetScore" style="width: 35%;margin-top: 10px"></el-input>
              <el-button type="danger" icon="el-icon-delete" circle @click="handleDeleteChecklink(index)"></el-button>
            </span>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCheckLinks">确 定</el-button>
      </div>
    </el-dialog>

    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 15, 20, 50, 100]"
      :page-size="pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>
  </div>
</template>

<script>
import {requestByClient, supplierConsumer} from "@/utils/HttpUtils";

export default {
  name: "ChecklinkEdit",
  mounted() {
    this.getList()
  },
  data() {
    return {
      year: localStorage.getItem('editYear'),
      dialogVisible: false,
      visible: false,
      tableData: [],
      pageSize: 20,
      total: 0,
      currentPage: 1,
      ableTarget: [],
      serchForm: {
        word: ''
      },
      editingForm: {
        editingCourse: {},
        checkLinks: []
      }
    }
  },
  watch: {
    '$store.state.settings.editYear': 'getList'
  },
  methods: {
    submitCheckLinks(){
      requestByClient(supplierConsumer, 'POST', 'checkLink/saveOrUpdate', this.editingForm.checkLinks, res => {
        if (res.data.succ) {
          this.$message({
            message: '修改成功',
            type: 'success'
          });
          this.dialogVisible = false
        }
      })
    },
    handleAddChecklink() {
      this.editingForm.checkLinks.push({year: localStorage.getItem('editYear'), name: '', targetScore: 0, courseId: this.editingForm.editingCourse.id})
    },
    handleDeleteChecklink(index) {
      var checkLink = this.editingForm.checkLinks[index]
      if (checkLink.id) {
        this.$confirm('此操作将删除其支撑数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          requestByClient(supplierConsumer, 'POST', 'checkLink/delete', {
            id: checkLink.id
          }, res => {
            if (res.data.succ) {
              this.$message({
                message: '已删除',
                type: 'success'
              });
            } else {
              this.$message.error(res.data.msg);
            }
            this.loading = false
          })
          this.editingForm.checkLinks.splice(index, 1)
        }).catch(() => {
        });
      } else {
        this.editingForm.checkLinks.splice(index, 1)
      }
    },
    handleEditForm(course) {
      requestByClient(supplierConsumer, 'POST', 'checkLink/list', {
        courseId: course.id,
        year: localStorage.getItem('editYear')
      }, res => {
        if (res.data.succ) {
          this.editingForm.editingCourse = course
          this.editingForm.checkLinks = res.data.data
          this.dialogVisible = true
        }
        this.loading = false
      })
    },
    getList() {
      this.dialogVisible = false
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'course/authList', {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        word: this.serchForm.word
      }, res => {
        if (res.data.succ) {
          this.tableData = res.data.data.list
          this.total = res.data.data.total
          this.pageSize = res.data.data.pageSize
          this.currentPage = res.data.data.pageNum
        }
        this.loading = false
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.currentPage = 1
      this.getList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getList()
    },
  }
}
</script>

<style scoped>

</style>

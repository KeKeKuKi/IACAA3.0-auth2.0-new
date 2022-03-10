<template>
  <div style="padding: 25px">
    <el-form :inline="true" :model="serchForm" class="demo-form-inline" style="height: 50px">
      <el-form-item label="">
        <el-input v-model="serchForm.word" placeholder="名称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList()">查询</el-button>
      </el-form-item>
      <span style="float: right;margin-right: 30px">
        <el-form-item>
          <el-button type="warning" icon="el-icon-plus" @click="handleAddCourse()">新增课程</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" icon="el-icon-delete" @click="handleDelete()">删除课程</el-button>
        </el-form-item>
      </span>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 100%"
      height="740"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange">
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>
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
        width="500">
      </el-table-column>
      <el-table-column
        prop="editUserId"
        label="课程管理员"
        width="">
        <template slot-scope="scope">
          <el-select v-model="scope.row.editUserId" @change="changeCourseEditer(scope.row)" placeholder="选择管理员"
                     clearable filterable style="width: 60%;margin-top: 10px">
            <el-option v-for="(item1,index1) in teachers" :key="index1" :label="item1.username" :value="item1.id"/>
          </el-select>
        </template>
      </el-table-column>

      <el-table-column label="课程可编辑">
        <template slot-scope="scope">
          <el-switch
            v-model="editAbles[scope.$index]"
            active-color="#13ce66"
            inactive-color="#ff4949"
            @change="changeEditStatus(scope.row.id)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope1">
          <el-button type="primary" icon="el-icon-edit" @click="handleEditCourse(scope1.row)">编辑</el-button>
          <el-button type="danger" icon="el-icon-delete" @click="deleteOne(scope1.row.id)">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <el-dialog
      :title="addForm.title"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="addForm" status-icon class="demo-ruleForm">
          <el-form-item label="课程名称" prop="name">
            <el-input v-model="addForm.name" type="text" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="简介" prop="pass">
            <el-input v-model="addForm.image" type="text" autocomplete="off"/>
          </el-form-item>
          <el-form-item label="课程管理员" prop="pass">
            <el-select v-model="addForm.editUserId" placeholder="选择管理员" clearable filterable>
              <el-option v-for="(item1,index1) in teachers" :key="index1" :label="item1.username" :value="item1.id"/>
            </el-select>
          </el-form-item>
          <el-form-item label="课程可编辑状态" prop="pass">
            <el-switch
              v-model="addForm.editStatus"
              active-color="#13ce66"
              inactive-color="#ff4949"
            >
            </el-switch>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitAddForm">确 定</el-button>
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
import {requestByClient, supplierConsumer, User} from "@/utils/HttpUtils";

export default {
  name: "Course",
  mounted() {
    this.getList()
    this.getTeachers()
  },
  data() {
    return {
      dialogVisible: false,
      pageSize: 20,
      total: 0,
      currentPage: 1,
      editAbles: [],
      tableData: [],
      serchForm: {
        word: ''
      },
      addForm: {
        id: '',
        title: '',
        name: '',
        image: '',
        editUserId: '',
        editStatus: true
      },
      ids: [],
      teachers: []
    }
  }, methods: {
    submitAddForm() {
      if(this.addForm.title === '添加课程'){
        requestByClient(supplierConsumer, 'POST', 'course/save', {
          name: this.addForm.name,
          image: this.addForm.image,
          editUserId: this.addForm.editUserId,
          editStatus: this.addForm.editStatus ? '1' : '0'
        }, res => {
          if (res.data.succ) {
            this.$message({
              message: '添加成功',
              type: 'success'
            });
            this.dialogVisible = false
            this.getList()
          }
        })
      }else if(this.addForm.title === '编辑课程'){
        requestByClient(supplierConsumer, 'POST', 'course/update', {
          id: this.addForm.id,
          name: this.addForm.name,
          image: this.addForm.image,
          editUserId: this.addForm.editUserId,
          editStatus: this.addForm.editStatus ? '1' : '0'
        }, res => {
          if (res.data.succ) {
            this.$message({
              message: '添加成功',
              type: 'success'
            });
            this.dialogVisible = false
            this.getList()
          }
        })
      }else {
        this.$message({
          message: '出错啦',
          type: 'error'
        });
      }

    },
    handleEditCourse(course){
      this.addForm.title = '编辑课程'
      this.addForm.id = course.id
      this.addForm.image = course.image
      this.addForm.name = course.name
      this.addForm.editUserId = course.editUserId
      this.addForm.editStatus = course.editStatus === 1
      this.dialogVisible = true
    },
    handleAddCourse() {
      this.addForm.title = '添加课程'
      this.addForm.id = ''
      this.addForm.name = ''
      this.addForm.image = ''
      this.addForm.editUserId = ''
      this.addForm.editStatus = true
      this.dialogVisible = true
    },
    changeCourseEditer(course) {
      requestByClient(supplierConsumer, 'POST', 'course/update', course, res => {
        if (res.data.succ) {
          this.$message({
            message: '已保存修改',
            type: 'success'
          });
        }
      })
    },
    getTeachers() {
      requestByClient(User, 'POST', 'api/user/list', {}, res => {
        if (res.data.code === 0) {
          this.teachers = res.data.data
        }
      })
    },
    changeEditStatus(id) {
      requestByClient(supplierConsumer, 'POST', 'course/changeEditStatus', {
        id: id
      }, res => {
        if (res.data.succ) {
          this.$message({
            message: '修改成功',
            type: 'success'
          });
          this.getList()
        }
      })
    },
    getList() {
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'course/adminList', {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        word: this.serchForm.word
      }, res => {
        if (res.data.succ) {
          this.tableData = res.data.data.list
          this.editAbles = this.tableData.map(i => {
            return i.editStatus === 1
          })
          this.total = res.data.data.total
          this.pageSize = res.data.data.pageSize
          this.currentPage = res.data.data.pageNum
        }
        this.loading = false
      })
    },
    getUser() {
      requestByClient(User, 'POST', 'course/voList', {}, res => {
        if (res.data.succ) {
          console.log(res)
        }
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
    deleteOne(id) {
      this.$confirm('是否删除此课程', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        requestByClient(supplierConsumer, 'POST', 'course/del', {
          ids: [id]
        }, res => {
          if (res.data.succ) {
            this.$message({
              message: '删除成功',
              type: 'success'
            });
            this.getList()
          }
          this.loading = false
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleDelete() {
      this.$confirm('是否删除选中的课程', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        requestByClient(supplierConsumer, 'POST', 'course/del', {
            ids: this.ids
          }
          , res => {
            if (res.data.succ) {
              this.$message({
                message: '删除成功',
                type: 'success'
              });
              this.getList()
            }
            this.loading = false
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleSelectionChange(val) {
      const result = val.map(item => item.id)
      this.ids = result;
    },
  }
}
</script>

<style scoped>

</style>

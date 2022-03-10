<template>
  <span style="width: 100%">
    <el-form :inline="true" :model="serchForm" class="demo-form-inline" style="height: 50px">
      <el-form-item label="">
        <el-input v-model="serchForm.word" placeholder="标题/描述" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getReqList">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 100%"
      height="750"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column
        prop="year"
        label="年份"
        width="100"
      />
      <el-table-column
        prop="name"
        label="毕业要求"
        width="400"
      />
      <el-table-column prop="targets" type="expand" label="指标点" width="1000">
        <template slot-scope="scope">
          <el-table :data="scope.row.targets" stripe>
            <el-table-column
              prop="discribe"
              label="指标点描述"
              width="500"
            />
            <el-table-column
              prop="createdDate"
              label="创建时间"
              width="200"
            />
            <el-table-column
              prop="updateDate"
              label="更新时间"
              width="200"
            />
            <el-table-column label="操作">
              <template slot-scope="scope1">
                <el-button type="primary" icon="el-icon-edit" circle @click="handleTargetEditForm(scope1.row,scope.row.name)" />
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="指标点支撑编辑"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="targetEditForm" status-icon class="demo-ruleForm">
          <el-form-item label="毕业要求" prop="name">
            <el-input v-model="targetEditForm.reqName" disabled type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="指标点" prop="pass">
            <el-input v-model="targetEditForm.target" disabled type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="支撑课程:" prop="pass">
            <el-button type="primary" round style="" @click="handleAddTarget">添加</el-button>
            <br>
            <!--eslint-disable-next-line-->
            <span v-for="(item,index) in targetEditForm.courseTargets" type="text" autocomplete="off">
              <el-select v-model="item.course.id" placeholder="选择课程" clearable filterable style="width: 60%;margin-top: 10px">
                <el-option v-for="(item1,index1) in courses" :key="index1" :label="item1.name" :value="item1.id" />
              </el-select>
              <el-input-number v-model="item.mix" :min="0.1" :max="1" :step="0.1" label="权重系数" style="width: 30%;margin-top: 10px" />
              <el-button type="danger" icon="el-icon-delete" circle @click="deleteDiscribe(index)" />
            </span>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitTargetEditForm()">确 定</el-button>
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
  </span>
</template>

<script>
import { requestByClient } from '@/utils/HttpUtils'
import { supplierConsumer } from '@/utils/HttpUtils'
export default {
  name: 'Target',
  data() {
    return {
      dialogVisible: false,
      dialogVisible1: false,
      visible: false,
      tableData: [],
      pageSize: 20,
      total: 0,
      currentPage: 1,
      serchForm: {
        word: '',
        year: localStorage.getItem('editYear')
      },
      editForm: {
        id: '',
        discrible: '',
        name: '',
        targets: []
      },
      addForm: {
        discrible: '',
        name: ''
      },
      courses: [],
      ids: [],
      targetEditForm: {
        id: '',
        reqName: '',
        target: '',
        courseTargets: []
      }

    }
  },
  watch: {
    '$store.state.settings.editYear': 'getReqList'
  },
  mounted() {
    this.getReqList()
    this.getCourseList()
  },
  methods: {
    handleSelectionChange(val) {
      const result = val.map(item => item.id)
      this.ids = result
    },
    getCourseList() {
      this.dialogVisible = false
      this.dialogVisible1 = false
      requestByClient(supplierConsumer, 'POST', 'course/list', {
      }, res => {
        if (res.data.succ) {
          this.courses = res.data.data
        }
      })
    },
    getReqList() {
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/voList', {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        word: this.serchForm.word,
        year: this.$store.state.settings.editYear
      }, res => {
        if (res.data.succ) {
          this.tableData = res.data.data
          // this.total = res.data.data.total
          // this.pageSize = res.data.data.pageSize
          // this.currentPage = res.data.data.pageNum
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
    handleClose(done) {
    },
    handleTargetEditForm(record, req) {
      this.dialogVisible = true
      this.editForm.id = record.id
      this.editForm.discrible = record.discrible
      this.editForm.name = record.name
      requestByClient(supplierConsumer, 'POST', 'courseTarget/voList', {
        targetId: record.id }
      , res => {
        if (res.data.succ) {
          this.targetEditForm.id = record.id
          this.targetEditForm.reqName = req
          this.targetEditForm.target = record.discribe
          this.targetEditForm.courseTargets = res.data.data
        }
        this.loading = false
      })
    },
    handleAddForm() {
      this.dialogVisible1 = true
      this.addForm.discrible = ''
      this.addForm.name = ''
    },
    handleAddTarget(targetId) {
      this.targetEditForm.courseTargets.push({ course: { id: '' }, target: { id: this.targetEditForm.id }, mix: 0.1 })
    },
    deleteDiscribe(index) {
      let id = this.targetEditForm.courseTargets[index].id
      if(id){
        this.$confirm('此操作将删除其支撑数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          requestByClient(supplierConsumer, 'POST', 'courseTarget/deleteOne', {
            id: id
          }, res => {
            if (res.data.succ) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
            } else {
              this.$message.warning(res.data.msg)
            }
            this.loading = false
          })
          this.targetEditForm.courseTargets.splice(index, 1)
        }).catch(() => {
        });
      }else {
        this.targetEditForm.courseTargets.splice(index, 1)
      }

    },
    submitTargetEditForm() {
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'courseTarget/saveOrUpdate', this.targetEditForm.courseTargets, res => {
        if (res.data.succ) {
          this.dialogVisible = false
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        } else {
          this.$message.warning(res.data.msg)
        }
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.el-table__header tr,
.el-table__header th {
  padding: 0;
  height: 40px;
  line-height: 50px;
}
.el-table__body tr,
.el-table__body td {
  padding: 0;
  height: 40px;
  line-height: 30px;
}
.el-pagination{
  text-align: right;
}
.el-form{
  text-align: left;
}
.dialog-footer{
  margin-top: 0px;
}
.demo-form-inline{
  margin-left: 50px;
}
</style>

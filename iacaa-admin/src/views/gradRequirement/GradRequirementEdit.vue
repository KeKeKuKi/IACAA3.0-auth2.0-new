<template>
  <span>
    <el-form :inline="true" :model="serchForm" class="demo-form-inline" style="height: 40px;padding: 10px">
      <el-form-item label="">
        <el-input v-model="serchForm.word" placeholder="标题/描述" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getList()">查询</el-button>
      </el-form-item>
      <span style="float: right;margin-right: 30px">
        <el-form-item>
<!--          <el-button type="success" @click="exportTemplate">下载导入模板</el-button>-->
        </el-form-item>
<!--        <el-form-item>-->
<!--          <el-button type="success" @click="">导入年度配置</el-button>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button type="warning" @click="handleAddForm()">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="danger" @click="handleDelete()">删除</el-button>
        </el-form-item>
      </span>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 98%; margin: 30px"
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
        width="80"
      />
      <el-table-column
        prop="name"
        label="毕业要求"
        width="200"
      />
      <el-table-column
        prop="discrible"
        label="描述"
        width="900"
      />
      <el-table-column
        prop="stuGrade"
        label="学生评价成绩"
        width="120"
      />
      <el-table-column
        prop="sysGrade"
        label="系统计算成绩"
        width="120"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle @click="handleEditForm(scope.row)" />
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="毕业要求编辑"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="editForm" status-icon class="demo-ruleForm">
          <el-form-item label="标题" prop="name">
            <el-input v-model="editForm.name" type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="描述" prop="pass">
            <el-input v-model="editForm.discrible" type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="指标点：" prop="pass">
            <el-button type="primary" round style="" @click="handleAddTarget">添加</el-button>
            <br>
            <span :key="index" v-for="(item,index) in editForm.targets" type="text" autocomplete="off">
              <el-input v-model="item.discribe" type="text" autocomplete="off" style="width: 91%;margin-top: 10px" />
              <el-button type="danger" icon="el-icon-delete" circle @click="deleteDiscribe(index)" />
            </span>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitEditForm('editForm')">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="添加年度毕业要求"
      :visible.sync="dialogVisible1"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="addForm" status-icon class="demo-ruleForm">
          <el-form-item label="标题" prop="name">
            <el-input v-model="addForm.name" type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="描述" prop="pass">
            <el-input v-model="addForm.discrible" type="text" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="submitAddForm()">确 定</el-button>
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
import axios from 'axios'
import {getToken} from "@/utils/auth";
export default {
  name: 'GradRequirementEdit',
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
      ids: []
    }
  },
  mounted() {
    this.getList()
  },
  watch: {
    '$store.state.settings.editYear': 'getList'
  },
  methods: {
    handleSelectionChange(val) {
      const result = val.map(item => item.id)
      this.ids = result
    },
    onSubmit() {
    },
    getList() {
      this.dialogVisible = false
      this.dialogVisible1 = false
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/list', {
        pageNum: this.currentPage,
        pageSize: this.pageSize,
        word: this.serchForm.word,
        year: this.$store.state.settings.editYear
      },res => {
        if (res.data.succ) {
          this.tableData = res.data.data
          // this.total = res.data.data.total
          // this.pageSize = res.data.data.pageSize
          // this.currentPage = res.data.data.pageNum
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
    handleClose() {
    },
    submitEditForm() {
      this.dialogVisible = false
      this.loading = true
      requestByClient(supplierConsumer, 'POST', 'gradRequirement/update', this.editForm,res => {
        if (res.data.succ) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.getList()
        }
        this.loading = false
      })
    },
    submitAddForm() {
      this.dialogVisible1 = false
      this.loading = true
      requestByClient(supplierConsumer, 'POST','gradRequirement/save', {
        discrible: this.addForm.discrible,
        name: this.addForm.name,
        year: this.$store.state.settings.editYear
      },res => {
        if (res.data.succ) {
          this.$message({
            message: '添加成功',
            type: 'success'
          })
          this.getList()
        }
        this.loading = false
      })
    },
    handleEditForm(record) {
      this.dialogVisible = true
      this.editForm.id = record.id
      this.editForm.discrible = record.discrible
      this.editForm.name = record.name
      requestByClient(supplierConsumer, 'POST','target/list', {
        reqId: record.id
        }
      , res => {
        if (res.data.succ) {
          this.editForm.targets = res.data.data
        }
        this.loading = false
      })
    },
    handleAddForm() {
      this.dialogVisible1 = true
      this.addForm.discrible = ''
      this.addForm.name = ''
    },
    handleDelete() {
      this.$confirm('此操作将删除其支撑数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        requestByClient(supplierConsumer, 'POST','gradRequirement/deleteList', {
            ids: this.ids }
          , res => {
            if (res.data.succ) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.getList()
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleAddTarget() {
      this.editForm.targets.push({ discribe: '', year: localStorage.getItem('editYear'),reqId: this.editForm.id })
    },
    deleteDiscribe(index) {
      let target = this.editForm.targets[index]
      if(target.id){
        this.$confirm('此操作将删除其支撑数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let id = target.id
          requestByClient(supplierConsumer, 'POST','target/deleteOne', {id: id},res => {
            if (res.data.succ) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
            }
          })
          this.editForm.targets.splice(index, 1)
        }).catch(() => {
        });
      }else {
        this.editForm.targets.splice(index, 1)
      }
    },
    exportTemplate() {
      let baseURL = supplierConsumer.defaults.baseURL
      axios.post(baseURL + '/stuScore/exportTemplate',{},{
        responseType:  'blob',
        headers:{
          '_token': getToken()
        }
      }).then(res => {
        const blob = new Blob([res.data], {
          type: 'application/vnd.ms-excel'
        })
        const objectUrl = URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = objectUrl
        a.download = '导入模板.xlsx'
        // a.click();
        // 下面这个写法兼容火狐
        a.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }))
        window.URL.revokeObjectURL(objectUrl)
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
.el-main .el-divider--horizontal{
  margin: 0px 0;
}
</style>

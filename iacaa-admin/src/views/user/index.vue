<template>
  <div>
    <el-form>
      <span style="float: right;margin: 20px">
          <el-button type="warning" @click="addUser">添加用户</el-button>
          <el-button type="danger" >删除</el-button>
      </span>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 98%; margin: 30px"
      height="750"
      tooltip-effect="dark"
    >
      <el-table-column
        type="selection"
        width="55"
      />
      <el-table-column
        type="index"
        width="50"
      />
      <el-table-column
        prop="id"
        label="ID"
        width="80"
      />
      <el-table-column
        prop="username"
        label="用户名"
        width="200"
      />
      <el-table-column
        prop="createBy"
        label="创建者"
        width="200"
      />
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="200"
      />
      <el-table-column
        prop="lastModifiedBy"
        label="最终修改者"
        width="200"
      />
      <el-table-column
        prop="lastModifiedTime"
        label="修改时间"
        width="200"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" @click="editUser(scope.row)" >编辑角色</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="添加用户"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="userAddForm" status-icon class="demo-ruleForm">
          <el-form-item label="用户名" prop="name">
            <el-input v-model="userAddForm.username" type="text" autocomplete="off" />
          </el-form-item>
          <el-form-item label="密码" prop="pass">
            <el-input v-model="userAddForm.password" type="password" autocomplete="off" />
          </el-form-item>
          <el-form-item label="确认密码" prop="pass">
            <el-input v-model="userAddForm.password2" type="password" autocomplete="off" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submituserAddForm()">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="编辑用户"
      :visible.sync="dialogVisible2"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="userEditForm" status-icon class="demo-ruleForm">
          <el-form-item label="用户名" prop="name" >
            <el-input v-model="userEditForm.username" type="text" autocomplete="off" disabled style="width: 80%"/>
          </el-form-item>
          <el-form-item label="角 色" prop="name">
            <el-select v-model="userEditForm.roleId" placeholder="选择角色" clearable filterable style="width: 82%">
              <el-option v-for="(item1,index1) in roles" :key="index1" :label="item1.description" :value="item1.id" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="submitUserEdit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '../../directive/waves'
// import Pagination from '../../components/Pagination'
import { UserServer, AuthServer, requestByClient } from '@/utils/HttpUtils'
const defaultData = {
  id: null,
  username: '',
  password: '',
  permission: [],
  role: 0
}

export default {
  name: 'User',
  // components: { Pagination },
  directives: { waves },
  filters: {},
  data() {
    return {
      fileList: [],
      categories: [],
      categoriesLoading: false,
      tags: [{
        value: 0,
        label: '普通用户'
      }, {
        value: 1,
        label: '管理员'
      }],
      groups: [],
      addTag: '',
      data: Object.assign({}, defaultData),
      tableKey: 0,
      list: null,
      listLoading: true,
      listQuery: {
        page: 1,
        number: 100,
        sort: 'username',
        asc: true,
        customParams: {
          role: -1
        }
      },
      dialogType: 'new',
      dialogVisible: false,
      dialogVisible2: false,
      tableData: [],
      pageSize: 20,
      total: 0,
      currentPage: 1,
      userAddForm: {
        username: '',
        password: '',
        password2: ''
      },
      userEditForm: {
        username: '',
        userId: '',
        roleId: ''
      },
      roles: []
    }
  },
  computed: {},
  mounted() {
    this.getList()
  },
  methods: {
    submitUserEdit(){
      requestByClient(AuthServer, 'post', '/api/user/setUserRole', this.userEditForm, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        }
        this.dialogVisible2 = false
      })
    },
    editUser(row){
      this.userEditForm.username = row.username
      this.userEditForm.userId = row.id
      this.userEditForm.roleId = row.roleId
      requestByClient(AuthServer, 'post', '/api/role/list', {

      }, resp => {
        console.log(resp)
        const respJson = resp.data
        const { code } = respJson
        console.log(respJson)
        if (code === 0) {
          this.roles = respJson.data

        }
        this.listLoading = false
      })
      this.dialogVisible2 = true
    },
    submituserAddForm(){
      if(this.userAddForm.password.length < 6){
        this.$message({
          message: '密码长度最少6位数',
          type: 'error'
        })
        return false
      }

      if(this.userAddForm.password !== this.userAddForm.password2){
        this.$message({
          message: '两次密码不一致',
          type: 'error'
        })
        return false
      }

      requestByClient(AuthServer, 'post', '/api/user', {
        username: this.userAddForm.username,
        password: this.userAddForm.password,
        client: 'Iacaa20Server'
      }, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          this.$message({
            message: '添加成功',
            type: 'success'
          })
          this.userAddForm.password = ''
          this.userAddForm.username = ''
          this.userAddForm.password2 = ''
          this.dialogVisible = false
          this.getList()
        }
        this.listLoading = false
      })
    },
    addUser(){
      this.dialogVisible = true
    },
    getList() {
      this.listLoading = true
      requestByClient(UserServer, 'post', '/api/user/page', this.listQuery, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          this.tableData = respJson.data.items
          this.total = respJson.data.total
          // this.pageSize = res.data.data.pageSize
          // this.currentPage = res.data.data.pageNum
        }
        this.listLoading = false
      })
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'time') {
        this.sortByTime(order)
      }
    },
    sortByTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = 'time'
        this.listQuery.asc = true
      } else {
        this.listQuery.sort = 'time'
        this.listQuery.asc = false
      }
      this.handleFilter()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    getSortClass(filed) {
      const asc = this.listQuery.asc
      const sort = this.listQuery.sort
      if (sort === filed) {
        return asc ? 'ascending' : 'descending'
      } else {
        return ''
      }
    },
    addShow() {
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleAdd() {
      this.data = Object.assign({}, defaultData)
      this.addShow()
    },
    editShow(username) {
      requestByClient(UserServer, 'get', '/api/user/findByUsername/' + username, null, resp => {
        const respJson = resp.data
        const { code, data } = respJson

        if (code === 0) {
          this.data.id = data.id
          this.data.username = data.username
          this.data.role = data.role
          if (data.permission) {
            this.data.permission = data.permission.split(',')
          }
          this.dialogType = 'edit'
          this.dialogVisible = true
        }
      })
    },
    handleEdit(scope) {
      this.editShow(scope.row.username)
    },
    handleDelete({ row }) {
      this.$confirm('确认要删除该执行器?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          requestByClient(UserServer, 'post', '/user/newRemove/' + row.id, null, resp => {
            const respJson = resp.data
            const { code, error } = respJson
            if (code === 0) {
              this.$notify({
                title: '成功',
                dangerouslyUseHTMLString: true,
                message: ``,
                type: 'success'
              })
              this.getList()
            } else {
              this.$notify({
                title: '失败',
                dangerouslyUseHTMLString: true,
                message: error,
                type: 'warning'
              })
            }
          })
        })
        .catch(err => {
          // eslint-disable-next-line no-console
          console.log(err)
        })
    },
    handleClose(tag) {
      this.blog.tags.splice(this.blog.tags.indexOf(tag), 1)
      this.tags.push({ 'value': tag, 'label': tag })
    },
    submitMethod() {
      let url
      if (this.dialogType === 'edit') {
        url = '/user/newUpdate'
      } else {
        url = '/user/newAdd'
      }
      this.dialogVisible = false
      this.data.permission = this.data.permission.join(',')
      requestByClient(UserServer, 'post', url, this.data, resp => {
        const respJson = resp.data
        const { code, error } = respJson
        if (code === 0) {
          this.$message({
            message: '成功',
            type: 'success'
          })
          this.getList()
        } else {
          this.$notify({
            title: '失败',
            dangerouslyUseHTMLString: true,
            message: error,
            type: 'warning'
          })
        }
      })
    }
  }
}
</script>

<style scoped>
</style>

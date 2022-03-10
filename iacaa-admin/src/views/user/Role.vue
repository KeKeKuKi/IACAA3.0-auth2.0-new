<template>
  <span>
    <el-form>
      <span style="float: right;margin: 20px">
          <el-button type="warning" @click="addRole">添加角色</el-button>
        <el-button type="danger" @click="deleteRole">删除角色</el-button>
      </span>
    </el-form>
    <el-table
      ref="multipleTable"
      :data="tableData"
      style="width: 98%; margin: 30px"
      height="750"
      tooltip-effect="dark"
      @selection-change="handleSelectionChange">
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
        prop="name"
        label="Name"
        width="200"
      />
      <el-table-column
        prop="description"
        label="描述"
        width="200"
      />
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="primary" @click="editRoles(scope.row)" >编辑权限</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      title="添加角色"
      :visible.sync="dialogVisible"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="roleAddForm" status-icon class="demo-ruleForm">
          <el-form-item label="名称" prop="name" >
            <el-input v-model="roleAddForm.name" type="text" autocomplete="off" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="描述" prop="describe" >
            <el-input v-model="roleAddForm.description" type="text" autocomplete="off" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="权限" prop="permision">
            <el-select v-model="roleAddForm.permissionList" multiple filterable placeholder="请选择" style="width: 80%">
              <el-option
                v-for="item in permissions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否超级管理员" prop="permision">
            <el-switch
              v-model="roleAddForm.admin"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitRoleAdd">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="编辑权限"
      :visible.sync="dialogVisible1"
      :close-on-click-modal="false"
      width="30%"
      center
    >
      <div>
        <el-form ref="ruleForm" :model="roleEditForm" status-icon class="demo-ruleForm">
          <el-form-item label="名称" prop="name" >
            <el-input v-model="roleEditForm.name" type="text" autocomplete="off" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="描述" prop="describe" >
            <el-input v-model="roleEditForm.description" type="text" autocomplete="off" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="权限" prop="permision">
            <el-select v-model="roleEditForm.permissionList" multiple filterable placeholder="请选择" style="width: 80%">
              <el-option
                v-for="item in permissions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否超级管理员" prop="permision">
            <el-switch
              v-model="roleEditForm.admin"
              active-color="#13ce66"
              inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible1 = false">取 消</el-button>
        <el-button type="primary" @click="submitRoleEdit">确 定</el-button>
      </div>
    </el-dialog>
  </span>
</template>

<script>
import {AuthServer, requestByClient} from "@/utils/HttpUtils";

export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      dialogVisible: false,
      dialogVisible1: false,
      roleAddForm: {
        name: '',
        description: '',
        permissionList: [],
        admin: false
      },
      roleEditForm: {
        id: '',
        name: '',
        description: '',
        permissionList: [],
        admin: false
      },
      deleteIds: [],
      permissions: []
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    getList(){
      requestByClient(AuthServer, 'post', '/api/role/page', {
        page : 1,
        number : 10
      }, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.tableData = respJson.data.items
        }
      })
    },
    handleSelectionChange(val) {
      this.deleteIds = val.map(i => {return {id: i.id}});
    },
    addRole() {
      this.dialogVisible = true
      requestByClient(AuthServer, 'post', '/api/permission/list', {}, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.permissions = respJson.data
        }
      })
    },
    deleteRole(){
      requestByClient(AuthServer, 'post', '/api/role/deleteAll', this.deleteIds, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.$message({
            message: '删除成功',
            type: 'success'
          })
        }
      })
      this.getList()
    },
    editRoles(row) {
      this.dialogVisible1 = true
      this.roleEditForm.id = row.id
      this.roleEditForm.name = row.name
      this.roleEditForm.admin = false
      this.roleEditForm.description = row.description
      requestByClient(AuthServer, 'post', '/api/permission/listByRole', {id: row.id}, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.roleEditForm.permissionList = respJson.data.map(i => {return i.id})
        }
      })
      requestByClient(AuthServer, 'post', '/api/permission/list', {}, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.permissions = respJson.data
        }
      })
    },
    submitRoleAdd() {
      console.log(this.roleEditForm)
      requestByClient(AuthServer, 'post', '/api/role', this.roleAddForm, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.$message({
            message: '添加成功',
            type: 'success'
          })
          this.dialogVisible = false
          this.getList()
        }
      })
    },
    submitRoleEdit() {
      console.log(this.roleEditForm)
      requestByClient(AuthServer, 'put', '/api/role', this.roleEditForm, resp => {
        const respJson = resp.data
        if(respJson.code === 0){
          this.$message({
            message: '修改成功',
            type: 'success'
          })
          this.dialogVisible1 = false
        }
      })
    }
  }
}
</script>
<style scoped>

</style>

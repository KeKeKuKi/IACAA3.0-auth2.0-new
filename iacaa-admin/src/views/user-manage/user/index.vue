<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.customParams.userId" placeholder="ID" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-input v-model="listQuery.customParams.username" placeholder="用户名" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button class="filter-item" type="primary" @click="handleAddRole">创建新用户</el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column label="ID" align="center" width="150">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户名" prop="username" sortable="custom" align="center" :class-name="getSortClass('username')">
        <template slot-scope="scope">
          <span>{{ scope.row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" prop="email" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.customizeInfo.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="说明" prop="email" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.customizeInfo.introduction }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" sortable="custom" :class-name="getSortClass('createTime')">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.createBy === 0 ? '认证中心' : scope.row.createBy }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最近修改时间" prop="lastModifiedTime" align="center" sortable="custom" :class-name="getSortClass('lastModifiedTime')">
        <template slot-scope="scope">
          <span>{{ scope.row.lastModifiedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="最近修改人" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.lastModifiedBy === 0 ? '认证中心' : scope.row.lastModifiedBy }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否启用" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.customizeInfo.isEnabled"
            style="display: block"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="启用"
            inactive-text="禁用"
            @change="handleStatus(scope)"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.number" @pagination="getList" />

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑用户':'新用户'">
      <el-form :model="user" label-width="80px" label-position="left">
        <el-form-item label="用户名">
          <el-input v-model="user.username" :disabled="dialogType==='edit'" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item v-show="dialogType!=='edit'" label="密码">
          <el-input v-model="user.password" show-password placeholder="请输入密码" />
        </el-form-item>
        <el-form-item v-show="dialogType!=='edit'" label="重复密码">
          <el-input v-model="user.rPassword" show-password placeholder="请重复输入密码" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="user.customizeInfo.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="user.customizeInfo.avatar" placeholder="请输入头像uri" />
        </el-form-item>
        <el-form-item label="是否启用">
          <el-switch
            v-model="user.customizeInfo.isEnabled"
            style="display: block"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
        <el-form-item v-if="positions.length > 0" label="职位">
          <el-select
            v-model="user.customizeInfo.positionId"
            style="width: 400px;"
            placeholder="请选择职位"
            @change="positionChange"
          >
            <el-option
              v-for="item in positions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="positionUsers.length > 0" label="上级">
          <el-select
            v-model="user.customizeInfo.superiorId"
            style="width: 400px;"
            placeholder="请选择上级"
            @change="positionChange"
          >
            <el-option
              v-for="item in positionUsers"
              :key="item.id"
              :label="item.username"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select
            v-model="user.roleId"
            style="width: 400px;"
            filterable
            remote
            reserve-keyword
            placeholder="请选择角色"
            :remote-method="queryRole"
            :loading="loading"
          >
            <el-option
              v-for="item in roles"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="说明">
          <el-input
            v-model="user.customizeInfo.introduction"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="说明"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmData">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import { requestByClient, User, Auth } from '@/utils/HttpUtils'
import { asyncRoutes } from '@/router'
import path from 'path'

const defaultUser = {
  id: '',
  username: '',
  password: '',
  rPassword: '',
  customizeInfo: {
    isEnabled: false,
    introduction: '',
    email: '',
    avatar: '',
    positionId: null,
    superiorId: null
  },
  createTime: '',
  lastModifiedTime: '',
  createBy: null,
  lastModifiedBy: null,
  client: '',
  roleId: null
}

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
  },
  data() {
    return {
      user: Object.assign({}, defaultUser),
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      loading: false,
      listQuery: {
        page: 1,
        number: 20,
        sort: 'username',
        asc: true,
        customParams: {
          userId: null,
          username: '',
          email: ''
        }
      },
      serviceRoutes: [],
      routes: [],
      permissions: [],
      apis: [],
      dialogType: 'new',
      dialogVisible: false,
      dialogFormVisible: false,
      checkStrictly: false,
      dialogStatus: '',
      dialogPvVisible: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      defaultPropsForPermission: {
        children: 'children',
        label: 'name'
      },
      roles: [],
      positions: [],
      positionUsers: []
    }
  },
  computed: {
    routesData() {
      return this.routes
    },
    permissionsData() {
      return this.permissions
    }
  },
  created() {
    this.getRoutes()
    this.getList()
    this.queryRole('')
  },
  methods: {
    positionChange() {
      if (this.user.customizeInfo.positionId && this.user.customizeInfo.positionId !== '') {
        requestByClient(Auth, 'get', '/api/user/findAllByPositionIdBetween/' + this.user.customizeInfo.positionId, null, resp => {
          const { code, data } = resp.data
          if (code === 0 && data) {
            this.positionUsers = data
          }
        })
      }
    },
    queryRole(query) {
      this.loading = true
      requestByClient(Auth, 'get', '/api/role/findAllByName?name=' + query, null, resp => {
        const respJson = resp.data
        const { code, data } = respJson
        if (code === 0) {
          this.roles = data
        }
        this.loading = false
      }, error => {
        console.error(error)
        this.loading = false
      })
    },
    getList() {
      this.listLoading = true
      requestByClient(User, 'post', '/api/user/page', this.listQuery, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          this.list = respJson.data.items
          this.list.forEach(item => {
            item.customizeInfo = JSON.parse(item.customizeInfo)
          })
          this.total = respJson.data.total
        }
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'username') {
        this.sortByUsername(order)
      } else if (prop === 'createTime') {
        this.sortByCreateTime(order)
      } else if (prop === 'lastModifiedTime') {
        this.sortByLastModifiedTime(order)
      }
    },
    sortByCreateTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = 'createTime'
        this.listQuery.asc = true
      } else {
        this.listQuery.sort = 'createTime'
        this.listQuery.asc = false
      }
      this.handleFilter()
    },
    sortByLastModifiedTime(order) {
      if (order === 'ascending') {
        this.listQuery.sort = 'lastModifiedTime'
        this.listQuery.asc = true
      } else {
        this.listQuery.sort = 'lastModifiedTime'
        this.listQuery.asc = false
      }
      this.handleFilter()
    },
    sortByUsername(order) {
      if (order === 'ascending') {
        this.listQuery.sort = 'username'
        this.listQuery.asc = true
      } else {
        this.listQuery.sort = 'username'
        this.listQuery.asc = false
      }
      this.handleFilter()
    },
    getSortClass: function(filed) {
      const asc = this.listQuery.asc
      const sort = this.listQuery.sort
      if (sort === filed) {
        return asc ? 'ascending' : 'descending'
      } else {
        return ''
      }
    },
    async getRoutes() {
      // 从Mock加载
      this.serviceRoutes = asyncRoutes
      this.routes = this.generateRoutes(asyncRoutes)
    },
    // Reshape the routes structure so that it looks the same as the sidebar
    generateRoutes(routes) {
      const res = []

      for (let route of routes) {
        // skip some route
        if (route.hidden) { continue }

        const onlyOneShowingChild = this.onlyOneShowingChild(route.children, route)

        if (route.children && onlyOneShowingChild && !route.alwaysShow) {
          route = onlyOneShowingChild
        }

        const data = {
          description: route.meta && route.meta.title,
          title: route.meta && route.meta.title

        }

        // recursive child routes
        if (route.children) {
          data.children = this.generateRoutes(route.children, data.path)
        }
        res.push(data)
      }
      return res
    },
    generateArr(routes) {
      let data = []
      routes.forEach(route => {
        data.push(route)
        if (route.children) {
          const temp = this.generateArr(route.children)
          if (temp.length > 0) {
            data = [...data, ...temp]
          }
        }
      })
      return data
    },
    handleStatus(scope) {
      const { row } = scope
      const data = {
        userId: row.id,
        clientId: this.$store.getters.clientId,
        info: JSON.stringify({
          isEnabled: row.customizeInfo.isEnabled
        })
      }
      requestByClient(User, 'put', '/api/userCustomizeInfo', data, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          this.$notify({
            title: '成功',
            dangerouslyUseHTMLString: true,
            message: '修改用户状态成功',
            type: 'success'
          })
        }
      })
    },
    addShow() {
      requestByClient(Auth, 'get', '/api/position/findAll', null, resp => {
        const { code, data } = resp.data
        if (code === 0) {
          this.positions = data
          this.positionChange()
          this.user = Object.assign({}, defaultUser)
          if (this.$refs.tree) {
            this.$refs.tree.setCheckedNodes([])
          }
          this.dialogType = 'new'
          this.dialogVisible = true
        }
      })
    },
    handleAddRole() {
      this.addShow()
    },
    resolveCheckedMenu(checkedMenus, routes, menuPermission, parent) {
      let checkedParent = null
      routes.forEach(item => {
        if (item.children) {
          this.resolveCheckedMenu(checkedMenus, item.children, menuPermission, item)
        } else {
          if (menuPermission && menuPermission.includes(item.description)) {
            checkedMenus.push(item)
            if (!checkedParent) {
              checkedMenus.push(parent)
              checkedParent = parent
            }
          }
        }
      })
    },
    handleEdit(scope) {
      const clientId = this.$store.getters.clientId
      requestByClient(User, 'get', '/api/user/' + scope.row.id + '?clientId=' + clientId, null, resp => {
        const respJson = resp.data
        const { code } = respJson
        if (code === 0) {
          requestByClient(Auth, 'get', '/api/user/findUserRoleId/' + scope.row.id, null, resp => {
            if (resp.data.code === 0) {
              this.user.id = respJson.data.id
              this.user.username = respJson.data.username
              this.user.introduction = respJson.data.introduction
              // this.user.email = respJson.data.email
              this.user.isEnabled = respJson.data.isEnabled
              this.user.customizeInfo = JSON.parse(respJson.data.customizeInfo)
              this.user.roleId = resp.data.data
              requestByClient(Auth, 'get', '/api/position/findAll', null, resp => {
                const { code, data } = resp.data
                if (code === 0) {
                  this.positions = data
                  this.positionChange()
                  this.dialogType = 'edit'
                  this.dialogVisible = true
                }
              })
            }
          })
        }
      })
    },
    handleDelete({ row }) {
      this.$confirm('确认要删除该用户?', '警告', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          requestByClient(User, 'delete', '/api/user/' + row.id, null, resp => {
            const respJson = resp.data
            const { code } = respJson
            if (code === 0) {
              this.$notify({
                title: '成功',
                dangerouslyUseHTMLString: true,
                message: `
            <div>用户名: ${row.username}</div>
          `,
                type: 'success'
              })
              this.getList()
            }
          })
        })
        .catch(err => { console.error(err) })
    },
    generateTree(routes, checkedKeys) {
      const res = []

      for (const route of routes) {
        const description = route.meta && route.meta.title

        // recursive child routes
        if (route.children) {
          route.children = this.generateTree(route.children, checkedKeys)
        }

        if (checkedKeys.includes(description) || (route.children && route.children.length >= 1)) {
          res.push(route)
        }
      }
      return res
    },
    resolveMenuPermission(arr, routes) {
      routes.forEach(item => {
        const roles = item.meta.roles
        if (!item.children && roles && roles.length > 0) {
          let mark
          for (let i = 0; i < roles.length; i++) {
            if (roles[i] !== 'adminExclusive') {
              mark = roles[i]
              break
            }
          }
          if (mark) {
            arr.push({
              mark: mark,
              name: item.meta.title
            })
          }
        } else if (item.children) {
          this.resolveMenuPermission(arr, item.children)
        }
      })
    },
    showFail(error) {
      this.dialogVisible = false
      this.$notify({
        title: '失败',
        dangerouslyUseHTMLString: true,
        message: error,
        type: 'error'
      })
    },
    showSuccess() {
      const { username } = this.user
      this.dialogVisible = false
      this.$notify({
        title: '成功',
        dangerouslyUseHTMLString: true,
        message: `
            <div>用户名: ${username}</div>
          `,
        type: 'success'
      })
    },
    async confirmData() {
      const isEdit = this.dialogType === 'edit'
      this.user.client = 'vea-admin'
      this.user.customizeInfo = JSON.stringify(this.user.customizeInfo)
      if (isEdit) {
        requestByClient(User, 'put', '/api/user  ', this.user, resp => {
          const respJson = resp.data
          const { code } = respJson
          if (code === 0) {
            if (this.user.roleId) {
              requestByClient(Auth, 'post', '/api/user/setUserRole', {
                userId: this.user.id,
                roleId: this.user.roleId
              }, resp => {
                if (resp.data.code === 0) {
                  this.getList()
                  this.showSuccess()
                } else {
                  this.showFail('更新用户成功，更新用户角色失败')
                }
              })
            } else {
              this.getList()
              this.showSuccess()
            }
          } else {
            this.showFail('更新用户失败')
          }
        })
      } else {
        requestByClient(Auth, 'post', '/api/user', this.user, resp => {
          const respJson = resp.data
          const { code } = respJson
          if (code === 0) {
            this.getList()
            this.showSuccess()
          } else {
            this.showFail('新增用户失败')
          }
        })
      }
    },
    // reference: src/view/layout/components/Sidebar/SidebarItem.vue
    onlyOneShowingChild(children = [], parent) {
      let onlyOneChild = null
      const showingChildren = children.filter(item => !item.hidden)

      // When there is only one child route, the child route is displayed by default
      if (showingChildren.length === 1) {
        onlyOneChild = showingChildren[0]
        onlyOneChild.path = path.resolve(parent.path, onlyOneChild.path)
        return onlyOneChild
      }

      // Show parent if there are no child route to display
      if (showingChildren.length === 0) {
        onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return onlyOneChild
      }

      return false
    }
  }
}
</script>

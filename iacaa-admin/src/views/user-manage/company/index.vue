<template>
  <div class="app-container">
    <div class="filter-container" />
    <el-tree
      :data="data"
      :allow-drop="allowDrop"
      :allow-drag="allowDrag"
      node-key="id"
      default-expand-all
      draggable
      :expand-on-click-node="false"
      :render-content="renderContent"
      @node-drag-end="handleDragEnd"
    />

    <el-dialog title="职位" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="名称" :label-width="formLabelWidth">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { requestByClient, Auth } from '@/utils/HttpUtils'

export default {
  name: 'Company',
  data() {
    return {
      data: [],
      dialogFormVisible: false,
      formLabelWidth: '120px',
      form: {
        id: null,
        name: '',
        aboveId: null
      },
      current: null,
      isEdit: false
    }
  },
  mounted() {
    requestByClient(Auth, 'get', '/api/company/findTree', null, resp => {
      const { data } = resp.data
      if (data) {
        this.data = data
      }
    })
  },
  methods: {
    showSuccess(data) {
      if (data) {
        const { name } = data
        this.$notify({
          title: '成功',
          dangerouslyUseHTMLString: true,
          message: `
            <div>职位名称: ${name}</div>
          `,
          type: 'success'
        })
      } else {
        this.$notify({
          title: '成功',
          dangerouslyUseHTMLString: true,
          type: 'success'
        })
      }
    },
    handleDragEnd(draggingNode, dropNode, dropType) {
      if (dropType === 'none') {
        return
      }
      requestByClient(Auth, 'post', '/api/company/updateAll', this.data[0].children, resp => {
        if (resp.data.code === 0) {
          this.showSuccess()
        }
      })
    },
    allowDrop(draggingNode, dropNode) {
      return dropNode.label !== '组织架构'
    },
    allowDrag(draggingNode) {
      return draggingNode.data.label.indexOf('组织架构') === -1
    },
    confirm() {
      if (this.isEdit) {
        requestByClient(Auth, 'put', '/api/company', this.form, resp => {
          if (resp.data.code === 0) {
            this.showSuccess(this.form)
            this.current.label = this.form.name
            this.current = null
          }
        })
      } else {
        this.form.aboveId = parseInt(this.current.id)
        requestByClient(Auth, 'post', '/api/company', this.form, resp => {
          const { code, data } = resp.data
          if (code === 0 && data) {
            this.showSuccess(this.form)
            const newChild = { id: data, label: this.form.name, children: [] }
            if (!this.current.children) {
              this.$set(this.current, 'children', [])
            }
            this.current.children.push(newChild)
            this.form = {}
            this.current = null
          }
        })
      }
      this.dialogFormVisible = false
    },
    append(data) {
      this.dialogFormVisible = true
      this.current = data
      this.isEdit = false
    },

    edit(data) {
      this.form.name = data.label
      this.form.id = parseInt(data.id)
      this.dialogFormVisible = true
      this.current = data
      this.isEdit = true
    },

    remove(node, data) {
      requestByClient(Auth, 'delete', '/api/company/' + data.id, null, resp => {
        const { code } = resp.data
        if (code === 0) {
          this.showSuccess(data)
          const parent = node.parent
          const children = parent.data.children || parent.data
          const index = children.findIndex(d => d.id === data.id)
          children.splice(index, 1)
        }
      })
    },
    renderContent(h, { node, data }) {
      return (
        <span class='custom-tree-node'>
          <span>{node.label}</span>
          <span class='op'>
            <el-button type='text' on-click={ () => this.append(data) }>追加</el-button>
            <el-button type='text' on-click={ () => this.edit(data) }>修改</el-button>
            <el-button type='text' on-click={ () => this.remove(node, data) }>删除</el-button>
          </span>
        </span>)
    }
  }
}
</script>

<style>
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 20px;
    padding-right: 200px;
  }
</style>

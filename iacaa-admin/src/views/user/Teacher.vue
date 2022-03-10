<template>
  <span>
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
  </span>
</template>

<script>
export default {
  name: "Teacher",
  data() {
    return {
      tableData: []
    }
  }
}
</script>

<style scoped>

</style>

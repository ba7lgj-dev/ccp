<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="80px">
      <el-form-item label="学校名称" prop="schoolName">
        <el-input v-model="queryParams.schoolName" placeholder="请输入学校名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="城市" prop="cityId">
        <el-select v-model="queryParams.cityId" placeholder="请选择城市" clearable filterable>
          <el-option v-for="item in cityOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:school:add']">新增学校</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="schoolList">
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="学校名称" prop="schoolName" />
      <el-table-column label="学校简称" prop="schoolShortName" />
      <el-table-column label="城市" prop="cityName">
        <template slot-scope="scope">
          {{ findLabel(cityOptions, scope.row.cityId) }}
        </template>
      </el-table-column>
      <el-table-column label="Logo" prop="logoUrl" width="100">
        <template slot-scope="scope">
          <el-image v-if="scope.row.logoUrl" style="width:60px;height:60px" :src="scope.row.logoUrl" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="地址" prop="address" />
      <el-table-column label="状态" prop="status" width="120">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:school:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ccp:school:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <school-form ref="formRef" :city-options="cityOptions" @success="getList" />
  </div>
</template>

<script>
import { listSchool, delSchool, changeSchoolStatus } from '@/api/ccp/school'
import SchoolForm from './form'

export default {
  name: 'SchoolIndex',
  components: { SchoolForm },
  data() {
    return {
      loading: false,
      total: 0,
      schoolList: [],
      cityOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schoolName: undefined,
        cityId: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.loadCityOptions()
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listSchool(this.queryParams).then(res => {
        const data = res.data || res
        this.schoolList = data.rows || []
        this.total = data.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    loadCityOptions() {
      this.getDicts('ccp_city').then(response => {
        this.cityOptions = response.data || []
      }).catch(() => {
        this.cityOptions = []
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleAdd() {
      this.$refs.formRef.open()
    },
    handleUpdate(row) {
      this.$refs.formRef.open(row)
    },
    handleDelete(row) {
      const ids = row.id
      this.$modal.confirm('是否确认删除学校编号为"' + ids + '"的数据项？').then(() => delSchool(ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '停用'
      changeSchoolStatus({ id: row.id, status: row.status }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    findLabel(options, value) {
      const item = options.find(opt => opt.value === value)
      return item ? item.label : value
    }
  }
}
</script>

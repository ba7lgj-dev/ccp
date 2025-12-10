<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="90px">
      <el-form-item label="所属学校" prop="schoolId">
        <el-select v-model="queryParams.schoolId" placeholder="请选择学校" clearable filterable>
          <el-option v-for="item in schoolOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="校区名称" prop="campusName">
        <el-input v-model="queryParams.campusName" placeholder="请输入校区名称" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:campus:add']">新增校区</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="campusList">
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="所属学校" prop="schoolName">
        <template slot-scope="scope">
          {{ findLabel(schoolOptions, scope.row.schoolId) || scope.row.schoolName }}
        </template>
      </el-table-column>
      <el-table-column label="校区名称" prop="campusName" />
      <el-table-column label="地址" prop="address" />
      <el-table-column label="经纬度" prop="latitude">
        <template slot-scope="scope">
          <span v-if="scope.row.latitude && scope.row.longitude">{{ scope.row.latitude }}, {{ scope.row.longitude }}</span>
        </template>
      </el-table-column>
      <el-table-column label="管理员" prop="managerUserName" />
      <el-table-column label="状态" prop="status" width="120">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:campus:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ccp:campus:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <campus-form ref="formRef" :school-options="schoolOptions" @success="refreshAfterEdit" />
  </div>
</template>

<script>
import { listCampus, delCampus, changeCampusStatus } from '@/api/ccp/campus'
import { listSchool } from '@/api/ccp/school'
import CampusForm from './form'

export default {
  name: 'CampusIndex',
  components: { CampusForm },
  data() {
    return {
      loading: false,
      total: 0,
      campusList: [],
      schoolOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schoolId: undefined,
        campusName: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.loadSchoolOptions()
    this.getList()
  },
  methods: {
    loadSchoolOptions() {
      listSchool({ pageNum: 1, pageSize: 999 }).then(res => {
        const data = res.data || res
        this.schoolOptions = (data.rows || []).map(item => ({ label: item.schoolName, value: item.id }))
      })
    },
    getList() {
      this.loading = true
      listCampus(this.queryParams).then(res => {
        const data = res.data || res
        this.campusList = data.rows || []
        this.total = data.total || 0
      }).finally(() => {
        this.loading = false
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
      this.$modal.confirm('是否确认删除校区编号为"' + ids + '"的数据项？').then(() => delCampus(ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '停用'
      changeCampusStatus({ id: row.id, status: row.status }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    refreshAfterEdit() {
      this.loadSchoolOptions()
      this.getList()
    },
    findLabel(options, value) {
      const item = options.find(opt => opt.value === value)
      return item ? item.label : value
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="90px">
      <el-form-item label="所属校区" prop="campusId">
        <el-select v-model="queryParams.campusId" placeholder="请选择校区" clearable filterable>
          <el-option v-for="item in campusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="校门名称" prop="gateName">
        <el-input v-model="queryParams.gateName" placeholder="请输入校门名称" clearable @keyup.enter.native="handleQuery" />
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
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:gate:add']">新增校门</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="gateList">
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="所属校区" prop="campusName">
        <template slot-scope="scope">
          {{ findLabel(campusOptions, scope.row.campusId) || scope.row.campusName }}
        </template>
      </el-table-column>
      <el-table-column label="校门名称" prop="gateName" />
      <el-table-column label="坐标" prop="latitude">
        <template slot-scope="scope">
          <span v-if="scope.row.latitude && scope.row.longitude">{{ scope.row.latitude }}, {{ scope.row.longitude }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序" prop="sort" width="100" />
      <el-table-column label="状态" prop="status" width="120">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(scope.row)" />
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="180" />
      <el-table-column label="操作" width="180" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:gate:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ccp:gate:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <gate-form ref="formRef" :campus-options="campusOptions" @success="refreshAfterEdit" />
  </div>
</template>

<script>
import { listGate, delGate, changeGateStatus } from '@/api/ccp/gate'
import { listCampus } from '@/api/ccp/campus'
import GateForm from './form'

export default {
  name: 'GateIndex',
  components: { GateForm },
  data() {
    return {
      loading: false,
      total: 0,
      gateList: [],
      campusOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        campusId: undefined,
        gateName: undefined,
        status: undefined
      }
    }
  },
  created() {
    this.loadCampusOptions()
    this.getList()
  },
  methods: {
    loadCampusOptions() {
      listCampus({ pageNum: 1, pageSize: 999 }).then(res => {
        const data = res.data || res
        this.campusOptions = (data.rows || []).map(item => ({ label: item.campusName, value: item.id }))
      })
    },
    getList() {
      this.loading = true
      listGate(this.queryParams).then(res => {
        const data = res.data || res
        this.gateList = data.rows || []
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
      this.$modal.confirm('是否确认删除校门编号为"' + ids + '"的数据项？').then(() => delGate(ids)).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    handleStatusChange(row) {
      const text = row.status === 1 ? '启用' : '停用'
      changeGateStatus({ id: row.id, status: row.status }).then(() => {
        this.$modal.msgSuccess(text + '成功')
      }).catch(() => {
        row.status = row.status === 1 ? 0 : 1
      })
    },
    refreshAfterEdit() {
      this.loadCampusOptions()
      this.getList()
    },
    findLabel(options, value) {
      const item = options.find(opt => opt.value === value)
      return item ? item.label : value
    }
  }
}
</script>

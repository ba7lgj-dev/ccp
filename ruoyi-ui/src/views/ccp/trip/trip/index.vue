<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" inline label-width="80px">
      <el-form-item label="学校" prop="schoolId">
        <el-input v-model="queryParams.schoolId" placeholder="学校ID" clearable />
      </el-form-item>
      <el-form-item label="校区" prop="campusId">
        <el-input v-model="queryParams.campusId" placeholder="校区ID" clearable />
      </el-form-item>
      <el-form-item label="关键词" prop="keyword">
        <el-input v-model="queryParams.keyword" placeholder="起点/终点关键词" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button v-hasPermi="['ccp:trip:export']" type="warning" icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="tripList">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column label="订单ID" prop="id" width="100" />
      <el-table-column label="学校" prop="schoolName" />
      <el-table-column label="校区" prop="campusName" />
      <el-table-column label="发起人" prop="ownerNickName" />
      <el-table-column label="起点" prop="startAddress" />
      <el-table-column label="终点" prop="endAddress" />
      <el-table-column label="出发时间" prop="departureTime" />
      <el-table-column label="人数" prop="currentPeople" width="120" :formatter="peopleFormatter" />
      <el-table-column label="状态" prop="statusLabel" />
      <el-table-column label="创建时间" prop="createTime" />
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleDetail(scope.row)">详情</el-button>
          <el-dropdown v-hasPermi="['ccp:trip:edit']" trigger="click">
            <el-button size="mini" type="text">状态<i class="el-icon-arrow-down el-icon--right"></i></el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="changeStatus(scope.row, 3)">标记完成</el-dropdown-item>
              <el-dropdown-item @click.native="changeStatus(scope.row, 4)">取消</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :visible.sync="detailOpen" title="订单详情" width="500px">
      <el-descriptions :column="1" size="small" v-if="detail">
        <el-descriptions-item label="学校">{{ detail.schoolName }}</el-descriptions-item>
        <el-descriptions-item label="校区">{{ detail.campusName }}</el-descriptions-item>
        <el-descriptions-item label="发起人">{{ detail.ownerNickName }}</el-descriptions-item>
        <el-descriptions-item label="起点">{{ detail.startAddress }}</el-descriptions-item>
        <el-descriptions-item label="终点">{{ detail.endAddress }}</el-descriptions-item>
        <el-descriptions-item label="出发时间">{{ detail.departureTime }}</el-descriptions-item>
        <el-descriptions-item label="人数">{{ detail.currentPeople }}/{{ detail.totalPeople }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import { listTrip, getTrip, changeTripStatus, exportTrip } from '@/api/ccp/trip/trip'

export default {
  name: 'CcpTripAdmin',
  data() {
    return {
      loading: false,
      total: 0,
      tripList: [],
      detailOpen: false,
      detail: null,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schoolId: undefined,
        campusId: undefined,
        keyword: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTrip(this.queryParams).then(response => {
        this.tripList = response.data.rows || []
        this.total = response.data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    peopleFormatter(row) {
      if (row.totalPeople) {
        return `${row.currentPeople || 0}/${row.totalPeople}`
      }
      return row.currentPeople
    },
    handleDetail(row) {
      getTrip(row.id).then(res => {
        this.detail = res.data
        this.detailOpen = true
      })
    },
    changeStatus(row, status) {
      this.$confirm('确认更新该订单状态吗？', '提示', { type: 'warning' }).then(() => {
        return changeTripStatus({ tripId: row.id, newStatus: status })
      }).then(() => {
        this.$message.success('更新成功')
        this.getList()
      })
    },
    handleExport() {
      this.download('/ccp/trip/trip/export', { ...this.queryParams }, `trip_${Date.now()}.xlsx`)
    }
  }
}
</script>

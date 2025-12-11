<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" inline label-width="80px">
      <el-form-item label="学校" prop="schoolId">
        <el-select v-model="queryParams.schoolId" placeholder="请选择学校" clearable filterable @change="handleSchoolChange">
          <el-option
            v-for="item in schoolOptions"
            :key="item.id"
            :label="item.schoolShortName || item.schoolName"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="校区" prop="campusId">
        <el-select v-model="queryParams.campusId" placeholder="请选择校区" clearable filterable :disabled="!queryParams.schoolId">
          <el-option
            v-for="item in campusOptions"
            :key="item.id"
            :label="item.campusName"
            :value="item.id"
          />
        </el-select>
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
      <el-table-column label="学校/校区" min-width="180">
        <template slot-scope="scope">
          {{ formatSchoolCampus(scope.row) }}
        </template>
      </el-table-column>
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
          <el-button size="mini" type="text" icon="el-icon-user" @click="handleViewMembers(scope.row)">查看成员</el-button>
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

    <el-drawer
      :visible.sync="memberDrawerVisible"
      size="70%"
      :title="memberDrawerTitle"
      direction="rtl"
      append-to-body
    >
      <trip-member-list ref="memberList" :trip-id="currentTripId" :visible="memberDrawerVisible" />
    </el-drawer>
  </div>
</template>

<script>
import { listTrip, getTrip, changeTripStatus, exportTrip } from '@/api/ccp/trip/trip'
import { listSchool } from '@/api/ccp/school'
import { listCampus } from '@/api/ccp/campus'
import TripMemberList from '../member/index.vue'

export default {
  name: 'CcpTripAdmin',
  components: { TripMemberList },
  data() {
    return {
      loading: false,
      total: 0,
      tripList: [],
      detailOpen: false,
      detail: null,
      schoolOptions: [],
      campusOptions: [],
      memberDrawerVisible: false,
      memberDrawerTitle: '成员列表',
      currentTripId: null,
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
    this.fetchSchools()
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
    fetchSchools() {
      listSchool({ pageNum: 1, pageSize: 999 }).then(res => {
        this.schoolOptions = res.data.rows || []
      })
    },
    fetchCampuses(schoolId) {
      if (!schoolId) {
        this.campusOptions = []
        return
      }
      listCampus({ schoolId: schoolId, pageNum: 1, pageSize: 999 }).then(res => {
        this.campusOptions = res.data.rows || []
      })
    },
    handleSchoolChange(value) {
      this.queryParams.campusId = undefined
      this.campusOptions = []
      if (value) {
        this.fetchCampuses(value)
      }
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleSchoolChange(this.queryParams.schoolId)
      this.handleQuery()
    },
    peopleFormatter(row) {
      if (row.totalPeople) {
        return `${row.currentPeople || 0}/${row.totalPeople}`
      }
      return row.currentPeople
    },
    formatSchoolCampus(row) {
      if (row.schoolName || row.campusName) {
        return `${row.schoolName || ''}${row.schoolName && row.campusName ? ' / ' : ''}${row.campusName || ''}`
      }
      return '--'
    },
    handleDetail(row) {
      getTrip(row.id).then(res => {
        this.detail = res.data
        this.detailOpen = true
      })
    },
    handleViewMembers(row) {
      this.currentTripId = row.id
      this.memberDrawerTitle = `成员列表 - 订单ID：${row.id}`
      this.memberDrawerVisible = true
      this.$nextTick(() => {
        if (this.$refs.memberList) {
          this.$refs.memberList.refresh(row.id)
        }
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

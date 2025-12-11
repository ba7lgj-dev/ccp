<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" inline label-width="80px">
      <el-form-item label="订单ID" prop="tripId">
        <el-input v-model="queryParams.tripId" placeholder="关联订单ID" clearable />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="用户ID" clearable />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="手机号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="memberList">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column label="成员ID" prop="memberId" width="120" />
      <el-table-column label="订单ID" prop="tripId" width="120" />
      <el-table-column label="用户ID" prop="userId" width="120" />
      <el-table-column label="昵称" prop="nickName" />
      <el-table-column label="手机号" prop="phone" />
      <el-table-column label="角色" prop="roleLabel" />
      <el-table-column label="人数" prop="joinPeopleCount" />
      <el-table-column label="状态" prop="memberStatusLabel" />
      <el-table-column label="加入时间" prop="joinTime" />
      <el-table-column label="操作" width="160">
        <template slot-scope="scope">
          <el-button v-hasPermi="['ccp:tripMember:edit']" size="mini" type="text" @click="handleNoShow(scope.row)">标记爽约</el-button>
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
  </div>
</template>

<script>
import { listTripMember, markNoShow } from '@/api/ccp/trip/member'

export default {
  name: 'CcpTripMemberAdmin',
  data() {
    return {
      loading: false,
      total: 0,
      memberList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tripId: this.$route.query.tripId,
        userId: undefined,
        phone: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTripMember(this.queryParams).then(res => {
        this.memberList = res.data.rows || []
        this.total = res.data.total || 0
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.handleQuery()
    },
    handleNoShow(row) {
      this.$prompt('请输入备注，可选', '标记爽约', { inputPlaceholder: '备注' }).then(({ value }) => {
        return markNoShow({ memberId: row.memberId, remark: value })
      }).then(() => {
        this.$message.success('操作成功')
        this.getList()
      })
    }
  }
}
</script>

<template>
  <div class="member-list">
    <el-table v-loading="loading" :data="memberList" size="mini">
      <el-table-column type="index" label="序号" width="60" />
      <el-table-column label="成员ID" prop="memberId" width="120" />
      <el-table-column label="角色" prop="roleLabel" width="100" />
      <el-table-column label="昵称" prop="nickName" />
      <el-table-column label="真实姓名" prop="realName" />
      <el-table-column label="性别" prop="gender" :formatter="genderFormatter" width="80" />
      <el-table-column label="手机号" prop="phone" width="140" />
      <el-table-column label="人数" prop="joinPeopleCount" width="100" />
      <el-table-column label="状态" prop="memberStatusLabel" width="100" />
      <el-table-column label="好评度" prop="avgRating" width="100" />
      <el-table-column label="爽约次数" prop="totalNoShow" width="100" />
      <el-table-column label="加入时间" prop="joinTime" width="180" />
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
import { listTripMemberByTrip, markNoShow } from '@/api/ccp/trip/member'

export default {
  name: 'TripMemberList',
  props: {
    tripId: {
      type: [Number, String],
      default: null
    },
    visible: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      loading: false,
      total: 0,
      memberList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tripId: null,
        memberStatus: undefined
      }
    }
  },
  watch: {
    tripId: {
      immediate: true,
      handler(val) {
        this.queryParams.tripId = val
        if (val && this.visible) {
          this.getList()
        } else {
          this.memberList = []
        }
      }
    },
    visible(val) {
      if (val && this.queryParams.tripId) {
        this.getList()
      }
    }
  },
  methods: {
    getList() {
      if (!this.queryParams.tripId) {
        return
      }
      this.loading = true
      listTripMemberByTrip(this.queryParams).then(res => {
        this.memberList = res.data.rows || []
        this.total = res.data.total || 0
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    refresh(tripId) {
      this.queryParams.tripId = tripId || this.queryParams.tripId
      this.getList()
    },
    handleNoShow(row) {
      this.$prompt('请输入备注，可选', '标记爽约', { inputPlaceholder: '备注' }).then(({ value }) => {
        return markNoShow({ memberId: row.memberId, remark: value })
      }).then(() => {
        this.$message.success('操作成功')
        this.getList()
      })
    },
    genderFormatter(row) {
      if (row.gender === 1) return '男'
      if (row.gender === 2) return '女'
      return '未知'
    }
  }
}
</script>

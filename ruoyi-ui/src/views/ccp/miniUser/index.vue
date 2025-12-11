<template>
  <div class="app-container mini-user-page">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="90px">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="queryParams.nickName" placeholder="请输入昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="真实姓名" prop="realName">
        <el-input v-model="queryParams.realName" placeholder="请输入真实姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="实名状态" prop="realAuthStatus">
        <el-select v-model="queryParams.realAuthStatus" placeholder="全部" clearable>
          <el-option v-for="item in realAuthStatusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="全部" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="在线状态" prop="onlineStatus">
        <el-select v-model="queryParams.onlineStatus" placeholder="全部" clearable>
          <el-option label="在线" :value="1" />
          <el-option label="离线" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          style="width: 260px"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          v-hasPermi="['ccp:miniUser:export']"
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="miniUserList" border>
      <el-table-column label="头像" width="80">
        <template slot-scope="scope">
          <el-avatar :size="40" :src="scope.row.avatarUrl" icon="el-icon-user" />
        </template>
      </el-table-column>
      <el-table-column label="昵称" prop="nickName" min-width="120" show-overflow-tooltip />
      <el-table-column label="手机号" prop="phone" min-width="120" />
      <el-table-column label="真实姓名" prop="realName" min-width="120" />
      <el-table-column label="性别" prop="gender" width="80">
        <template slot-scope="scope">
          {{ genderText(scope.row.gender) }}
        </template>
      </el-table-column>
      <el-table-column label="实名状态" prop="realAuthStatus" width="120">
        <template slot-scope="scope">
          <el-tag :type="realAuthTagType(scope.row.realAuthStatus)">{{ realAuthText(scope.row.realAuthStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="账号状态" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag type="info" v-else>停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="在线状态" prop="onlineStatus" width="100">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.onlineStatus === 1">在线</el-tag>
          <el-tag type="info" v-else>离线</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最近活跃时间" prop="lastActiveTime" min-width="160" />
      <el-table-column label="创建时间" prop="createTime" min-width="160" />
      <el-table-column label="管理员备注" min-width="160">
        <template slot-scope="scope">
          <el-tooltip class="item" effect="dark" :content="scope.row.adminRemark" placement="top">
            <span class="ellipsis">{{ scope.row.adminRemark || '-' }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="320" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button
            v-if="scope.row.realAuthStatus === 1"
            type="text"
            size="mini"
            icon="el-icon-check"
            v-hasPermi="['ccp:miniUser:auth']"
            @click="handleApprove(scope.row)"
          >审核通过</el-button>
          <el-button
            v-if="scope.row.realAuthStatus === 1"
            type="text"
            size="mini"
            icon="el-icon-close"
            v-hasPermi="['ccp:miniUser:auth']"
            @click="handleReject(scope.row)"
          >审核拒绝</el-button>
          <el-button
            type="text"
            size="mini"
            :icon="scope.row.status === 1 ? 'el-icon-switch-button' : 'el-icon-open'"
            v-hasPermi="['ccp:miniUser:edit']"
            @click="handleChangeStatus(scope.row)"
          >{{ scope.row.status === 1 ? '停用' : '启用' }}</el-button>
          <el-button type="text" size="mini" icon="el-icon-edit" v-hasPermi="['ccp:miniUser:edit']" @click="openRemark(scope.row)">
            编辑备注
          </el-button>
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

    <el-drawer :visible.sync="detailVisible" title="用户详情" size="50%" :with-header="true">
      <div v-if="detailData" class="detail-wrapper" v-loading="detailLoading">
        <section class="detail-section">
          <h4>基础信息</h4>
          <div class="detail-row">
            <el-avatar :size="64" :src="detailData.user.avatarUrl" icon="el-icon-user" />
            <div class="detail-meta">
              <p>昵称：{{ detailData.user.nickName }}</p>
              <p>手机号：{{ detailData.user.phone || '-' }}</p>
              <p>真实姓名：{{ detailData.user.realName || '-' }}</p>
              <p>性别：{{ genderText(detailData.user.gender) }}</p>
              <p>OpenId：{{ detailData.user.openId || '-' }}</p>
              <p>在线状态：{{ detailData.user.onlineStatus === 1 ? '在线' : '离线' }}</p>
              <p>最近活跃：{{ detailData.user.lastActiveTime || '-' }}</p>
              <p>创建时间：{{ detailData.user.createTime || '-' }}</p>
            </div>
          </div>
        </section>

        <section class="detail-section">
          <h4>实名认证</h4>
          <el-descriptions :column="2" border size="small">
            <el-descriptions-item label="实名状态">
              <el-tag :type="realAuthTagType(detailData.user.realAuthStatus)">{{ realAuthText(detailData.user.realAuthStatus) }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="证件姓名">{{ detailData.user.idCardName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="证件号码">{{ maskIdCard(detailData.user.idCardNumber) }}</el-descriptions-item>
            <el-descriptions-item label="审核人">{{ detailData.user.realAuthReviewBy || '-' }}</el-descriptions-item>
            <el-descriptions-item label="审核时间">{{ detailData.user.realAuthReviewTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="失败原因">{{ detailData.user.realAuthFailReason || '-' }}</el-descriptions-item>
            <el-descriptions-item label="人脸比对">{{ detailData.user.faceVerifyResult || '-' }}</el-descriptions-item>
            <el-descriptions-item label="人脸照片">
              <el-image v-if="detailData.user.faceImageUrl" style="width:80px;height:80px" :src="detailData.user.faceImageUrl" fit="cover" />
              <span v-else>-</span>
            </el-descriptions-item>
          </el-descriptions>
        </section>

        <section class="detail-section">
          <h4>安全与信誉</h4>
          <el-descriptions :column="3" size="small" border>
            <el-descriptions-item label="平均评分">{{ reputationValue(detailData.reputation && detailData.reputation.avgRating) }}</el-descriptions-item>
            <el-descriptions-item label="评价数">{{ reputationValue(detailData.reputation && detailData.reputation.totalReviews, '-') }}</el-descriptions-item>
            <el-descriptions-item label="爽约次数">{{ reputationValue(detailData.reputation && detailData.reputation.totalNoShow, '-') }}</el-descriptions-item>
            <el-descriptions-item label="紧急联系人">{{ detailData.emergencyContactCount != null ? detailData.emergencyContactCount : 0 }}</el-descriptions-item>
            <el-descriptions-item label="黑名单状态">
              <span v-if="detailData.blacklist">{{ detailData.blacklist.status === 1 ? '封禁' : '正常' }}</span>
              <span v-else>未命中</span>
            </el-descriptions-item>
            <el-descriptions-item label="黑名单截止" v-if="detailData.blacklist">{{ detailData.blacklist.expireTime || '-' }}</el-descriptions-item>
          </el-descriptions>
          <div class="tag-list" v-if="detailData.tagStats && detailData.tagStats.length">
            <span class="tag-title">用户标签：</span>
            <el-tag v-for="item in detailData.tagStats" :key="item.tag" type="info" effect="plain" class="mr8">{{
              item.tag
            }} ({{ item.totalCount }})</el-tag>
          </div>
        </section>

        <section class="detail-section">
          <h4>管理员备注</h4>
          <el-input
            type="textarea"
            v-model="remarkForm.adminRemark"
            :rows="3"
            placeholder="填写后台备注"
            @change="remarkChanged = true"
          />
          <div style="margin-top: 10px; text-align: right;">
            <el-button type="primary" size="mini" :disabled="!remarkChanged" @click="submitRemark(detailData.user.id)">保存备注</el-button>
          </div>
        </section>
      </div>
    </el-drawer>

    <el-dialog :visible.sync="remarkDialogVisible" title="编辑备注" width="450px">
      <el-form :model="remarkForm" label-width="80px">
        <el-form-item label="备注">
          <el-input type="textarea" v-model="remarkForm.adminRemark" :rows="4" maxlength="200" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="remarkDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRemark(remarkForm.id)">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMiniUser, getMiniUser, updateMiniUser, approveRealAuth, rejectRealAuth } from '@/api/ccp/miniUser'

export default {
  name: 'MiniUser',
  data() {
    return {
      loading: false,
      detailLoading: false,
      total: 0,
      miniUserList: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phone: undefined,
        realName: undefined,
        realAuthStatus: undefined,
        status: undefined,
        onlineStatus: undefined,
        createTimeBegin: undefined,
        createTimeEnd: undefined
      },
      realAuthStatusOptions: [
        { label: '未认证', value: 0 },
        { label: '待审核', value: 1 },
        { label: '已认证', value: 2 },
        { label: '不通过', value: 3 }
      ],
      detailVisible: false,
      detailData: null,
      remarkDialogVisible: false,
      remarkChanged: false,
      remarkForm: {
        id: undefined,
        adminRemark: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.createTimeBegin = this.dateRange[0]
        params.createTimeEnd = this.dateRange[1]
      } else {
        params.createTimeBegin = undefined
        params.createTimeEnd = undefined
      }
      listMiniUser(params)
        .then(res => {
          this.miniUserList = res.rows || []
          this.total = res.total || 0
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phone: undefined,
        realName: undefined,
        realAuthStatus: undefined,
        status: undefined,
        onlineStatus: undefined,
        createTimeBegin: undefined,
        createTimeEnd: undefined
      }
      this.getList()
    },
    handleView(row) {
      this.detailVisible = true
      this.detailLoading = true
      this.remarkChanged = false
      getMiniUser(row.id)
        .then(res => {
          this.detailData = res.data
          this.remarkForm = {
            id: row.id,
            adminRemark: res.data.user.adminRemark
          }
        })
        .finally(() => {
          this.detailLoading = false
        })
    },
    handleApprove(row) {
      this.$confirm('确认通过该用户的实名认证申请吗？', '提示', { type: 'warning' })
        .then(() => approveRealAuth({ id: row.id }))
        .then(() => {
          this.$message.success('操作成功')
          this.getList()
          if (this.detailVisible) {
            this.handleView(row)
          }
        })
    },
    handleReject(row) {
      this.$prompt('请输入拒绝原因', '实名认证拒绝', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        inputPattern: /.+/,
        inputErrorMessage: '请填写拒绝原因'
      })
        .then(({ value }) => rejectRealAuth({ id: row.id, realAuthFailReason: value }))
        .then(() => {
          this.$message.success('操作成功')
          this.getList()
          if (this.detailVisible) {
            this.handleView(row)
          }
        })
    },
    handleChangeStatus(row) {
      const target = row.status === 1 ? 0 : 1
      const text = target === 1 ? '启用' : '停用'
      this.$confirm(`确认${text}该账号吗？`, '提示', { type: 'warning' })
        .then(() => updateMiniUser({ id: row.id, status: target }))
        .then(() => {
          this.$message.success('操作成功')
          this.getList()
        })
    },
    openRemark(row) {
      this.remarkForm = {
        id: row.id,
        adminRemark: row.adminRemark
      }
      this.remarkDialogVisible = true
    },
    submitRemark(id) {
      if (!id) {
        return
      }
      updateMiniUser(this.remarkForm).then(() => {
        this.$message.success('保存成功')
        this.remarkDialogVisible = false
        this.remarkChanged = false
        if (this.detailVisible) {
          this.handleView({ id })
        }
        this.getList()
      })
    },
    handleExport() {
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.createTimeBegin = this.dateRange[0]
        params.createTimeEnd = this.dateRange[1]
      }
      this.download('/ccp/miniUser/export', params, `mini_user_${Date.now()}.xlsx`)
    },
    genderText(value) {
      if (value === 1) return '男'
      if (value === 2) return '女'
      return '保密'
    },
    realAuthText(status) {
      const map = {
        0: '未认证',
        1: '待审核',
        2: '已认证',
        3: '不通过'
      }
      return map[status] || '未认证'
    },
    realAuthTagType(status) {
      if (status === 1) return 'warning'
      if (status === 2) return 'success'
      if (status === 3) return 'danger'
      return 'info'
    },
    maskIdCard(value) {
      if (!value || value.length < 8) return value || '-'
      return `${value.slice(0, 4)}****${value.slice(-4)}`
    },
    reputationValue(value, fallback = '-') {
      return value === undefined || value === null ? fallback : value
    }
  }
}
</script>

<style lang="scss" scoped>
.mini-user-page {
  .ellipsis {
    display: inline-block;
    max-width: 160px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .detail-wrapper {
    padding-right: 10px;
  }

  .detail-section {
    margin-bottom: 16px;

    h4 {
      margin: 0 0 10px;
      font-weight: 600;
    }
  }

  .detail-row {
    display: flex;
    align-items: center;
    gap: 16px;
  }

  .detail-meta p {
    margin: 0 0 4px;
  }

  .tag-list {
    margin-top: 10px;
  }

  .tag-title {
    margin-right: 6px;
  }
}

.mr8 {
  margin-right: 8px;
}
</style>

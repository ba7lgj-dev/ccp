<template>
  <div class="app-container">
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
      <el-form-item label="提交时间" prop="dateRange">
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

    <el-table v-loading="loading" :data="list" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" prop="id" width="100" />
      <el-table-column label="昵称" prop="nickName" min-width="120" show-overflow-tooltip />
      <el-table-column label="头像" width="90" align="center">
        <template slot-scope="scope">
          <el-avatar :size="40" :src="scope.row.avatarUrl" icon="el-icon-user" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" prop="phone" min-width="120" />
      <el-table-column label="真实姓名" prop="realName" min-width="120" />
      <el-table-column label="身份证号" prop="idCardMasked" min-width="160" />
      <el-table-column label="性别匹配" width="110" align="center">
        <template slot-scope="scope">
          <span :style="{ color: isGenderMismatch(scope.row) ? '#f56c6c' : '#67c23a' }">{{ isGenderMismatch(scope.row) ? '不匹配' : '匹配' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实名状态" prop="realAuthStatus" width="130" align="center">
        <template slot-scope="scope">
          <el-tag :type="realAuthTagType(scope.row.realAuthStatus)">{{ realAuthText(scope.row.realAuthStatus) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核人" prop="realAuthReviewByName" min-width="120" />
      <el-table-column label="审核时间" prop="realAuthReviewTime" min-width="160" />
      <el-table-column label="创建时间" prop="createTime" min-width="160" />
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" v-hasPermi="['ccp:userRealAuth:query']" @click="handleView(scope.row)">详情</el-button>
          <el-button
            v-if="scope.row.realAuthStatus === 1"
            type="text"
            size="mini"
            icon="el-icon-s-check"
            v-hasPermi="['ccp:userRealAuth:approve']"
            @click="openAudit(scope.row)">
            审核
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

    <el-drawer :visible.sync="detailVisible" title="实名详情" size="50%" :with-header="true">
      <div v-if="detailData" class="detail-wrapper" v-loading="detailLoading">
        <section class="detail-section">
          <h4>基础信息</h4>
          <div class="detail-row">
            <el-avatar :size="64" :src="detailData.avatarUrl" icon="el-icon-user" />
            <div class="detail-meta">
              <p>昵称：{{ detailData.nickName || '-' }}</p>
              <p>手机号：{{ detailData.phone || '-' }}</p>
              <p>用户ID：{{ detailData.id }}</p>
              <p>创建时间：{{ detailData.createTime || '-' }}</p>
            </div>
          </div>
        </section>
        <section class="detail-section">
          <h4>实名信息</h4>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="真实姓名">{{ detailData.realName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{ detailData.idCardNumber || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别匹配">
          <span :style="{ color: isGenderMismatch(detailData) ? '#f56c6c' : '#67c23a' }">{{ isGenderMismatch(detailData) ? '不匹配' : '匹配' }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="realAuthTagType(detailData.realAuthStatus)">{{ realAuthText(detailData.realAuthStatus) }}</el-tag>
        </el-descriptions-item>
            <el-descriptions-item label="审核人">{{ detailData.realAuthReviewByName || '-' }}</el-descriptions-item>
            <el-descriptions-item label="审核时间">{{ detailData.realAuthReviewTime || '-' }}</el-descriptions-item>
            <el-descriptions-item label="失败原因">{{ detailData.realAuthFailReason || '-' }}</el-descriptions-item>
            <el-descriptions-item label="管理员备注">{{ detailData.adminRemark || '-' }}</el-descriptions-item>
            <el-descriptions-item label="实名照片">
              <el-image
                v-if="detailData.faceImageUrl"
                style="width: 120px; height: 120px"
                :src="detailData.faceImageUrl"
                :preview-src-list="[detailData.faceImageUrl]"
                fit="cover"
              />
              <span v-else>-</span>
            </el-descriptions-item>
          </el-descriptions>
        </section>
      </div>
    </el-drawer>

    <el-dialog title="实名认证审核" :visible.sync="auditDialog.visible" width="600px">
      <div v-if="auditDialog.data">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="昵称">{{ auditDialog.data.nickName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ auditDialog.data.phone || '-' }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ auditDialog.data.realName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ auditDialog.data.idCardNumber || '-' }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ auditDialog.data.createTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="照片">
            <el-image
              v-if="auditDialog.data.faceImageUrl"
              style="width: 120px; height: 120px"
              :src="auditDialog.data.faceImageUrl"
              :preview-src-list="[auditDialog.data.faceImageUrl]"
              fit="cover"
            />
            <span v-else>-</span>
          </el-descriptions-item>
        </el-descriptions>
        <el-form label-width="90px" class="mt16">
          <el-form-item label="审核备注">
            <el-input type="textarea" v-model="auditDialog.remark" :rows="3" placeholder="可选填写审核备注" />
          </el-form-item>
          <el-form-item label="拒绝原因">
            <el-input type="textarea" v-model="auditDialog.failReason" :rows="3" placeholder="拒绝时必填" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditDialog.visible = false">取 消</el-button>
        <el-button type="primary" :loading="auditDialog.loading" @click="submitApprove">通 过</el-button>
        <el-button type="danger" :loading="auditDialog.loading" @click="submitReject">拒 绝</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUserRealAuth, getUserRealAuth, approveUserRealAuth, rejectUserRealAuth } from '@/api/ccp/userRealAuth'

export default {
  name: 'CcpUserRealAuth',
  data() {
    return {
      loading: false,
      total: 0,
      list: [],
      dateRange: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phone: undefined,
        realName: undefined,
        realAuthStatus: undefined,
        beginTime: undefined,
        endTime: undefined
      },
      realAuthStatusOptions: [
        { label: '不限', value: undefined },
        { label: '未认证', value: 0 },
        { label: '待审核', value: 1 },
        { label: '已认证', value: 2 },
        { label: '审核不通过', value: 3 }
      ],
      detailVisible: false,
      detailLoading: false,
      detailData: null,
      auditDialog: {
        visible: false,
        loading: false,
        data: null,
        failReason: '',
        remark: ''
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    realAuthText(status) {
      const map = {
        0: '未认证',
        1: '待审核',
        2: '已认证',
        3: '审核不通过'
      }
      return map[status] || '-'
    },
    realAuthTagType(status) {
      const map = {
        0: 'info',
        1: 'warning',
        2: 'success',
        3: 'danger'
      }
      return map[status] || 'info'
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.queryParams.beginTime = this.dateRange && this.dateRange.length ? this.dateRange[0] : undefined
      this.queryParams.endTime = this.dateRange && this.dateRange.length ? this.dateRange[1] : undefined
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
        beginTime: undefined,
        endTime: undefined
      }
      this.getList()
    },
    getList() {
      this.loading = true
      listUserRealAuth(this.queryParams).then(res => {
        this.list = res.rows || []
        this.total = res.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleView(row) {
      this.detailVisible = true
      this.detailLoading = true
      getUserRealAuth(row.id).then(res => {
        this.detailData = res.data
      }).finally(() => {
        this.detailLoading = false
      })
    },
    openAudit(row) {
      this.auditDialog.visible = true
      this.auditDialog.loading = false
      this.auditDialog.failReason = ''
      this.auditDialog.remark = ''
      this.auditDialog.data = null
      getUserRealAuth(row.id).then(res => {
        this.auditDialog.data = res.data
      })
    },
    submitApprove() {
      if (!this.auditDialog.data) return
      const doApprove = () => {
        this.auditDialog.loading = true
        approveUserRealAuth({ id: this.auditDialog.data.id, adminRemark: this.auditDialog.remark })
          .then(() => {
            this.$modal.msgSuccess('审核通过')
            this.auditDialog.visible = false
            this.getList()
          })
          .finally(() => {
            this.auditDialog.loading = false
          })
      }
      if (this.isGenderMismatch(this.auditDialog.data)) {
        this.$confirm('用户选择的性别与身份证号码推导的性别不一致，建议仔细核查并酌情驳回，是否仍要继续通过？', '提示', { type: 'warning' })
          .then(() => {
            doApprove()
          })
          .catch(() => {})
      } else {
        doApprove()
      }
    },
    submitReject() {
      if (!this.auditDialog.data) return
      if (!this.auditDialog.failReason) {
        this.$modal.msgError('请填写拒绝原因')
        return
      }
      this.auditDialog.loading = true
      rejectUserRealAuth({
        id: this.auditDialog.data.id,
        realAuthFailReason: this.auditDialog.failReason,
        adminRemark: this.auditDialog.remark
      })
        .then(() => {
          this.$modal.msgSuccess('已拒绝')
          this.auditDialog.visible = false
          this.getList()
        })
        .finally(() => {
          this.auditDialog.loading = false
        })
    },
    calcGenderFromIdCard(cardNo) {
      if (!cardNo || cardNo.length < 17) return undefined
      const code = Number(cardNo.charAt(16))
      if (Number.isNaN(code)) return undefined
      return code % 2 === 0 ? '女' : '男'
    },
    genderText(val) {
      if (val === 1 || val === '1') return '男'
      if (val === 2 || val === '2') return '女'
      return undefined
    },
    isGenderMismatch(row) {
      if (!row) return false
      const idGender = this.calcGenderFromIdCard(row.idCardNumber)
      const userGender = this.genderText(row.gender)
      if (!idGender || !userGender) return false
      return idGender !== userGender
    }
    calcGenderFromIdCard(cardNo) {
      if (!cardNo || cardNo.length < 17) return undefined
      const code = Number(cardNo.charAt(16))
      if (Number.isNaN(code)) return undefined
      return code % 2 === 0 ? '女' : '男'
    },
    genderText(val) {
      if (val === 1 || val === '1') return '男'
      if (val === 2 || val === '2') return '女'
      return undefined
    },
    isGenderMismatch(row) {
      if (!row) return false
      const idGender = this.calcGenderFromIdCard(row.idCardNumber)
      const userGender = this.genderText(row.gender)
      if (!idGender || !userGender) return false
      return idGender !== userGender
    }
  }
}
</script>

<style scoped>
.detail-wrapper {
  padding: 0 10px;
}

.detail-section {
  margin-bottom: 16px;
}

.detail-section h4 {
  margin-bottom: 8px;
}

.detail-row {
  display: flex;
  align-items: center;
}

.detail-meta {
  margin-left: 12px;
  line-height: 22px;
}

.mt16 {
  margin-top: 16px;
}
</style>

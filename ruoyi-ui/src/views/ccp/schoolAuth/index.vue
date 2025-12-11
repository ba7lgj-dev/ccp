<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="90px">
      <el-form-item label="学校名称" prop="schoolName">
        <el-input v-model="queryParams.schoolName" placeholder="请输入学校名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="校区名称" prop="campusName">
        <el-input v-model="queryParams.campusName" placeholder="请输入校区名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学生姓名" prop="studentName">
        <el-input v-model="queryParams.studentName" placeholder="请输入学生姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="学号" prop="studentNo">
        <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="全部" clearable>
          <el-option v-for="item in statusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="提交时间" prop="submitTime">
        <el-date-picker
          v-model="dateRange"
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

    <el-table v-loading="loading" :data="authList" border>
      <el-table-column label="学校" min-width="200">
        <template slot-scope="scope">
          <div>{{ scope.row.schoolName || '-' }}</div>
          <div class="desc">{{ scope.row.cityName || '' }}</div>
        </template>
      </el-table-column>
      <el-table-column label="校区" prop="campusName" min-width="140" show-overflow-tooltip />
      <el-table-column label="学生姓名" prop="studentName" min-width="120" />
      <el-table-column label="学号" prop="studentNo" min-width="120" />
      <el-table-column label="昵称" prop="userNickName" min-width="140" show-overflow-tooltip />
      <el-table-column label="手机号" prop="userPhone" min-width="140" />
      <el-table-column label="提交时间" prop="submitTime" min-width="160" />
      <el-table-column label="审核状态" prop="status" width="120">
        <template slot-scope="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核时间" prop="reviewTime" min-width="160" />
      <el-table-column label="操作" width="240" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-view" @click="handleView(scope.row)">详情</el-button>
          <el-button
            v-if="canReview(scope.row)"
            type="text"
            size="mini"
            icon="el-icon-check"
            v-hasPermi="['ccp:schoolAuth:review']"
            @click="handleApprove(scope.row)"
          >审核通过</el-button>
          <el-button
            v-if="canReview(scope.row)"
            type="text"
            size="mini"
            icon="el-icon-close"
            v-hasPermi="['ccp:schoolAuth:review']"
            @click="openRejectDialog(scope.row)"
          >审核拒绝</el-button>
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

    <el-dialog title="认证详情" :visible.sync="detailVisible" width="720px" append-to-body>
      <el-descriptions :column="2" size="small" border>
        <el-descriptions-item label="学校">{{ detail.schoolName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="校区">{{ detail.campusName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ detail.cityName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学生姓名">{{ detail.studentName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ detail.studentNo || '-' }}</el-descriptions-item>
        <el-descriptions-item label="小程序昵称">{{ detail.userNickName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ detail.userPhone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ statusText(detail.status) }}</el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ detail.submitTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ detail.reviewTime || '-' }}</el-descriptions-item>
        <el-descriptions-item label="审核人">{{ detail.reviewBy || '-' }}</el-descriptions-item>
        <el-descriptions-item label="拒绝原因">{{ detail.failReason || '-' }}</el-descriptions-item>
      </el-descriptions>
      <div class="image-row" v-if="detail.studentCardImageUrl">
        <div class="image-label">学生证：</div>
        <el-image :src="imageUrl(detail.studentCardImageUrl)" :preview-src-list="[imageUrl(detail.studentCardImageUrl)]" />
      </div>
      <div class="image-row" v-if="detail.extraImageUrl">
        <div class="image-label">附加图片：</div>
        <el-image :src="imageUrl(detail.extraImageUrl)" :preview-src-list="[imageUrl(detail.extraImageUrl)]" />
      </div>
    </el-dialog>

    <el-dialog title="审核拒绝" :visible.sync="rejectVisible" width="420px" append-to-body>
      <el-form :model="rejectForm" label-width="80px">
        <el-form-item label="原因">
          <el-input
            type="textarea"
            v-model="rejectForm.failReason"
            placeholder="请输入拒绝原因"
            :rows="4"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="rejectVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitReject">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSchoolAuth, getSchoolAuth, approveSchoolAuth, rejectSchoolAuth } from '@/api/ccp/schoolAuth'

export default {
  name: 'CcpSchoolAuth',
  data() {
    return {
      loading: false,
      total: 0,
      authList: [],
      detailVisible: false,
      rejectVisible: false,
      detail: {},
      rejectForm: {
        id: null,
        failReason: ''
      },
      dateRange: [],
      statusOptions: [
        { label: '待审核', value: 1 },
        { label: '已通过', value: 2 },
        { label: '不通过', value: 3 },
        { label: '已过期', value: 4 }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        schoolName: undefined,
        campusName: undefined,
        studentName: undefined,
        studentNo: undefined,
        status: undefined,
        beginSubmitTime: undefined,
        endSubmitTime: undefined
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
        params.beginSubmitTime = this.dateRange[0]
        params.endSubmitTime = this.dateRange[1]
      } else {
        params.beginSubmitTime = undefined
        params.endSubmitTime = undefined
      }
      listSchoolAuth(params).then(res => {
        this.authList = res.rows || []
        this.total = res.total || 0
      }).finally(() => {
        this.loading = false
      })
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.handleQuery()
    },
    statusText(status) {
      const option = this.statusOptions.find(item => item.value === status)
      return option ? option.label : '-'
    },
    statusTagType(status) {
      switch (status) {
        case 1:
          return 'warning'
        case 2:
          return 'success'
        case 3:
          return 'danger'
        case 4:
          return 'info'
        default:
          return 'info'
      }
    },
    canReview(row) {
      return row.status === 1 || row.status === 3
    },
    handleView(row) {
      getSchoolAuth(row.id).then(res => {
        this.detail = res.data || {}
        this.detailVisible = true
      })
    },
    handleApprove(row) {
      this.$confirm('确认将该学生认证设为已通过？确认后将允许该学生在对应学校发起拼车。', '提示', {
        type: 'warning'
      }).then(() => {
        return approveSchoolAuth({ id: row.id })
      }).then(() => {
        this.msgSuccess('审核通过')
        this.getList()
      }).catch(() => {})
    },
    openRejectDialog(row) {
      this.rejectForm = { id: row.id, failReason: '' }
      this.rejectVisible = true
    },
    submitReject() {
      if (!this.rejectForm.failReason) {
        this.msgError('请填写拒绝原因')
        return
      }
      rejectSchoolAuth(this.rejectForm).then(() => {
        this.msgSuccess('已拒绝')
        this.rejectVisible = false
        this.getList()
      })
    },
    imageUrl(url) {
      if (!url) return ''
      if (url.startsWith('http')) return url
      return process.env.VUE_APP_BASE_API + url
    }
  }
}
</script>

<style scoped>
.app-container .desc {
  color: #909399;
  font-size: 12px;
}
.image-row {
  display: flex;
  align-items: center;
  margin-top: 10px;
}
.image-label {
  width: 80px;
  color: #606266;
}
</style>

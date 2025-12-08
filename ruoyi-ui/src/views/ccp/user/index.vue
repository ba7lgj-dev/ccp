<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="queryParams.nickName" placeholder="请输入昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="证件姓名" prop="idCardName">
        <el-input v-model="queryParams.idCardName" placeholder="请输入证件姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="证件号码" prop="idCardNumber">
        <el-input v-model="queryParams.idCardNumber" placeholder="请输入证件号码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="实名状态" prop="realAuthStatus">
        <el-select v-model="queryParams.realAuthStatus" placeholder="请选择实名状态" clearable>
          <el-option v-for="dict in realAuthStatusOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="账号状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择账号状态" clearable>
          <el-option label="正常" :value="1" />
          <el-option label="封禁" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="dateRange">
        <el-date-picker v-model="dateRange" style="width: 240px" type="daterange" range-separator="-"
          start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:user:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ccp:user:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="userList">
      <el-table-column label="ID" prop="id" width="80" />
      <el-table-column label="昵称" prop="nickName" />
      <el-table-column label="头像" prop="avatarUrl" width="100">
        <template slot-scope="scope">
          <el-image style="width:60px;height:60px" :src="scope.row.avatarUrl" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="手机号" prop="phone" />
      <el-table-column label="证件姓名" prop="idCardName" />
      <el-table-column label="证件号码" prop="idCardNumber" />
      <el-table-column label="真实姓名" prop="realName" />
      <el-table-column label="人脸照片" prop="faceImageUrl" width="120">
        <template slot-scope="scope">
          <el-image style="width:60px;height:60px" :src="scope.row.faceImageUrl" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column label="人脸校验结果" prop="faceVerifyResult" />
      <el-table-column label="性别" prop="gender">
        <template slot-scope="scope">
          <dict-tag :options="genderOptions" :value="scope.row.gender" />
        </template>
      </el-table-column>
      <el-table-column label="账号状态" prop="status">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag type="danger" v-else>封禁</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="实名状态" prop="realAuthStatus">
        <template slot-scope="scope">
          <dict-tag :options="realAuthStatusOptions" :value="scope.row.realAuthStatus" />
        </template>
      </el-table-column>
      <el-table-column label="实名审核时间" prop="realAuthReviewTime" width="160" />
      <el-table-column label="创建时间" prop="createTime" width="160" />
      <el-table-column label="操作" width="240" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" plain icon="el-icon-view" v-if="scope.row.realAuthStatus === 1" @click="handleReview(scope.row)" v-hasPermi="['ccp:user:review']">审核</el-button>
          <el-button size="mini" type="warning" plain icon="el-icon-check" v-if="scope.row.status === 0" @click="handleChangeStatus(scope.row, 1)" v-hasPermi="['ccp:user:changeStatus']">启用</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-close" v-if="scope.row.status === 1" @click="handleChangeStatus(scope.row, 0)" v-hasPermi="['ccp:user:changeStatus']">禁用</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:user:edit']">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['ccp:user:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 编辑弹窗 -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="证件姓名" prop="idCardName">
          <el-input v-model="form.idCardName" placeholder="请输入证件姓名" />
        </el-form-item>
        <el-form-item label="证件号码" prop="idCardNumber">
          <el-input v-model="form.idCardNumber" placeholder="请输入证件号码" />
        </el-form-item>
        <el-form-item label="人脸照片链接" prop="faceImageUrl">
          <el-input v-model="form.faceImageUrl" placeholder="请输入人脸照片链接" />
        </el-form-item>
        <el-form-item label="人脸校验结果" prop="faceVerifyResult">
          <el-input v-model="form.faceVerifyResult" placeholder="请输入人脸校验结果" />
        </el-form-item>
        <el-form-item label="管理员备注" prop="adminRemark">
          <el-input type="textarea" v-model="form.adminRemark" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option v-for="dict in genderOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 审核弹窗 -->
    <el-dialog title="用户审核" :visible.sync="reviewOpen" width="500px">
      <el-form ref="reviewForm" :model="review" label-width="100px">
        <el-form-item label="真实姓名">
          <span>{{ review.realName }}</span>
        </el-form-item>
        <el-form-item label="证件姓名">
          <span>{{ review.idCardName }}</span>
        </el-form-item>
        <el-form-item label="证件号码">
          <span>{{ review.idCardNumber }}</span>
        </el-form-item>
        <el-form-item label="手机号">
          <span>{{ review.phone }}</span>
        </el-form-item>
        <el-form-item label="人脸照片">
          <el-image style="width:120px;height:120px" :src="review.faceImageUrl" fit="cover" />
        </el-form-item>
        <el-form-item label="人脸校验结果">
          <span>{{ review.faceVerifyResult }}</span>
        </el-form-item>
        <el-form-item label="审核结果" prop="targetRealAuthStatus">
          <el-select v-model="review.targetRealAuthStatus">
            <el-option v-for="dict in realAuthStatusOptions" :key="dict.value" :label="dict.label" :value="dict.value" v-if="dict.value === 2 || dict.value === 3" />
          </el-select>
        </el-form-item>
        <el-form-item label="拒绝原因" prop="realAuthFailReason" v-if="review.targetRealAuthStatus === 3">
          <el-input type="textarea" v-model="review.realAuthFailReason" placeholder="请输入拒绝原因" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reviewOpen = false">取消</el-button>
        <el-button type="primary" @click="submitReview">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, delUser, changeUserStatus, reviewUser, exportUser } from '@/api/ccp/user'

export default {
  name: 'CcpUser',
  data() {
    return {
      loading: false,
      total: 0,
      userList: [],
      title: '',
      open: false,
      reviewOpen: false,
      dateRange: [],
      genderOptions: [],
      realAuthStatusOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: undefined,
        phone: undefined,
        idCardName: undefined,
        idCardNumber: undefined,
        status: undefined,
        realAuthStatus: undefined
      },
      form: {},
      review: {}
    }
  },
  created() {
    this.getList()
    this.getDicts('ccp_user_gender').then(res => {
      this.genderOptions = res.data
    })
    this.getDicts('ccp_auth_status').then(res => {
      this.realAuthStatusOptions = res.data
    })
  },
  methods: {
    getList() {
      this.loading = true
      const params = Object.assign({}, this.queryParams)
      if (this.dateRange && this.dateRange.length === 2) {
        params.createTimeStart = this.dateRange[0]
        params.createTimeEnd = this.dateRange[1]
      }
      listUser(params).then(response => {
        this.userList = response.rows
        this.total = response.total
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
    handleAdd() {
      this.form = {}
      this.title = '新增用户'
      this.open = true
    },
    handleUpdate(row) {
      getUser(row.id).then(res => {
        this.form = res.data
        this.title = '编辑用户'
        this.open = true
      })
    },
    handleReview(row) {
      this.review = {
        id: row.id,
        realName: row.realName,
        idCardName: row.idCardName,
        idCardNumber: row.idCardNumber,
        phone: row.phone,
        faceImageUrl: row.faceImageUrl,
        faceVerifyResult: row.faceVerifyResult,
        targetRealAuthStatus: 2,
        realAuthFailReason: ''
      }
      this.reviewOpen = true
    },
    handleChangeStatus(row, status) {
      changeUserStatus({ id: row.id, status: status }).then(() => {
        this.$modal.msgSuccess('操作成功')
        this.getList()
      })
    },
    handleDelete(row) {
      this.$modal.confirm('是否确认删除编号为 "' + row.id + '" 的记录？').then(() => {
        return delUser(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      })
    },
    submitForm() {
      if (this.form.id) {
        updateUser(this.form).then(() => {
          this.$modal.msgSuccess('修改成功')
          this.open = false
          this.getList()
        })
      } else {
        addUser(this.form).then(() => {
          this.$modal.msgSuccess('新增成功')
          this.open = false
          this.getList()
        })
      }
    },
    submitReview() {
      if (this.review.targetRealAuthStatus === 3 && !this.review.realAuthFailReason) {
        this.$modal.msgError('请填写拒绝原因')
        return
      }
      reviewUser(this.review).then(() => {
        this.$modal.msgSuccess('审核完成')
        this.reviewOpen = false
        this.getList()
      })
    },
    handleExport() {
      const params = { ...this.queryParams }
      if (this.dateRange && this.dateRange.length === 2) {
        params.createTimeStart = this.dateRange[0]
        params.createTimeEnd = this.dateRange[1]
      }
      this.download('/ccp/user/export', params, `ccp_user_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

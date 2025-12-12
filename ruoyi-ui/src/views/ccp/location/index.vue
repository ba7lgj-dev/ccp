<template>
  <div class="app-container location-page">
    <el-card shadow="never" class="filter-card">
      <div class="filter-title">学校选择</div>
      <el-form ref="queryForm" :model="queryParams" size="small" label-width="90px">
        <el-row :gutter="12">
          <el-col :span="8">
            <el-form-item label="城市" prop="cityId">
              <el-select v-model="queryParams.cityId" placeholder="按城市筛选" clearable filterable @change="handleCityChange">
                <el-option v-for="item in dict.type.ccp_city" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学校" prop="schoolId">
              <el-select v-model="queryParams.schoolId" placeholder="请选择学校" clearable filterable @change="handleSchoolChange">
                <el-option v-for="item in filteredSchoolOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="校区" prop="campusId">
              <el-select v-model="queryParams.campusId" placeholder="请选择校区" clearable filterable @change="handleCampusChange">
                <el-option v-for="item in campusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="filter-title mt16">地点筛选</div>
      <el-form :model="queryParams" size="small" label-width="90px" inline class="light-form">
        <el-form-item label="地点名称" prop="locationName">
          <el-input v-model="queryParams.locationName" placeholder="支持模糊搜索" clearable @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item label="地点类型" prop="locationType">
          <el-select v-model="queryParams.locationType" placeholder="请选择类型" clearable>
            <el-option v-for="item in locationTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable>
            <el-option label="正常" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="small" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" size="small" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-row :gutter="12" class="mt16 mb12">
      <el-col :span="12">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['ccp:location:add']">新增地点</el-button>
        <el-button icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['ccp:location:export']">导出</el-button>
      </el-col>
    </el-row>

    <el-card shadow="never" class="table-card">
      <el-table v-loading="loading" :data="locationList" border header-cell-class-name="table-header">
        <el-table-column label="序号" type="index" width="60" />
        <el-table-column label="地点名称" prop="locationName" min-width="160">
          <template slot-scope="scope">
            <el-tooltip :content="scope.row.locationName" placement="top" v-if="scope.row.locationName && scope.row.locationName.length > 12">
              <span class="text-ellipsis">{{ scope.row.locationName }}</span>
            </el-tooltip>
            <span v-else>{{ scope.row.locationName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="所属学校" prop="schoolName" min-width="140">
          <template slot-scope="scope">{{ scope.row.schoolName || findSchoolName(scope.row.schoolId) }}</template>
        </el-table-column>
        <el-table-column label="所属校区" prop="campusName" min-width="140" />
        <el-table-column label="类型" prop="locationType" width="120">
          <template slot-scope="scope">
            <el-tag :type="typeColor(scope.row.locationType)" disable-transitions>{{ typeLabel(scope.row.locationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="经纬度" min-width="170">
          <template slot-scope="scope">
            <span>{{ scope.row.latitude }}, {{ scope.row.longitude }}</span>
            <el-button type="text" size="mini" icon="el-icon-copy-document" @click="copyCoordinate(scope.row)">复制</el-button>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status" width="120">
          <template slot-scope="scope">
            <el-switch v-model="scope.row.status" :active-value="1" :inactive-value="0" @change="val => handleStatusChange(scope.row, val)" />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="170" />
        <el-table-column label="操作" fixed="right" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['ccp:location:edit']">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" class="danger-text" @click="handleDelete(scope.row)" v-hasPermi="['ccp:location:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="!loading && !queryParams.campusId" class="empty-tips">请选择学校与校区后查看地点列表</div>
      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
    </el-card>

    <el-dialog :title="dialogTitle" :visible.sync="open" width="820px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="所属学校">
              <el-input :value="findSchoolName(currentSchoolId)" readonly placeholder="请选择学校" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属校区" prop="campusId">
              <el-select v-model="form.campusId" placeholder="请选择校区" filterable>
                <el-option v-for="item in campusOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="地点名称" prop="locationName">
              <el-input v-model="form.locationName" placeholder="请输入地点名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="地点类型" prop="locationType">
              <el-select v-model="form.locationType" placeholder="请选择类型">
                <el-option v-for="item in locationTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="12">
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度" />
              <el-button type="text" size="mini">从地图选择</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片" prop="coverImageUrl">
          <image-upload v-model="form.coverImageUrl" :limit="1" />
        </el-form-item>
        <el-form-item label="富文本描述" prop="descriptionText">
          <editor v-model="form.descriptionText" :min-height="200" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="open = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLocation, getLocation, addLocation, updateLocation, delLocation, changeLocationStatus, exportLocation } from '@/api/ccp/location'
import { listSchool } from '@/api/ccp/school'
import { listCampus } from '@/api/ccp/campus'

export default {
  name: 'CcpLocationIndex',
  dicts: ['ccp_city'],
  data() {
    return {
      loading: false,
      total: 0,
      locationList: [],
      schoolOptions: [],
      campusOptions: [],
      campusCache: {},
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        cityId: undefined,
        schoolId: undefined,
        campusId: undefined,
        locationName: undefined,
        locationType: undefined,
        status: undefined
      },
      form: {},
      open: false,
      dialogTitle: '新增地点',
      rules: {
        campusId: [{ required: true, message: '请选择校区', trigger: 'change' }],
        locationName: [
          { required: true, message: '请输入地点名称', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在1到100字符', trigger: 'blur' }
        ],
        locationType: [{ required: true, message: '请选择地点类型', trigger: 'change' }],
        latitude: [{ required: true, validator: this.validateNumber, trigger: 'blur' }],
        longitude: [{ required: true, validator: this.validateNumber, trigger: 'blur' }]
      },
      locationTypeOptions: [
        { label: '地铁站', value: 1 },
        { label: '商超', value: 2 },
        { label: '公交站', value: 3 },
        { label: '公寓', value: 4 },
        { label: '其他', value: 5 }
      ]
    }
  },
  computed: {
    filteredSchoolOptions() {
      if (!this.queryParams.cityId) {
        return this.schoolOptions
      }
      return this.schoolOptions.filter(item => String(item.cityId) === String(this.queryParams.cityId))
    },
    currentSchoolId() {
      return this.queryParams.schoolId || this.form.schoolId
    }
  },
  created() {
    this.loadSchools()
  },
  methods: {
    loadSchools() {
      listSchool({ pageNum: 1, pageSize: 999 }).then(res => {
        const data = res.data || res
        this.schoolOptions = (data.rows || []).map(item => ({
          label: `${item.schoolShortName || item.schoolName}${item.cityName ? ' · ' + item.cityName : ''}`,
          value: item.id,
          cityId: item.cityId,
          raw: item
        }))
      })
    },
    loadCampuses(schoolId) {
      if (!schoolId) {
        this.campusOptions = []
        return
      }
      if (this.campusCache[schoolId]) {
        this.campusOptions = this.campusCache[schoolId]
        return
      }
      listCampus({ schoolId, pageNum: 1, pageSize: 999 }).then(res => {
        const data = res.data || res
        const options = (data.rows || []).map(item => ({ label: item.campusName, value: item.id }))
        this.campusCache = { ...this.campusCache, [schoolId]: options }
        this.campusOptions = options
      })
    },
    getList() {
      if (!this.queryParams.campusId) {
        this.locationList = []
        this.total = 0
        return
      }
      this.loading = true
      listLocation(this.queryParams)
        .then(res => {
          const data = res.data || res
          this.locationList = data.rows || []
          this.total = data.total || 0
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
      this.resetForm('queryForm')
      this.queryParams.pageNum = 1
      this.queryParams.pageSize = 10
      this.campusOptions = []
      this.getList()
    },
    handleCityChange() {
      this.queryParams.schoolId = undefined
      this.queryParams.campusId = undefined
      this.campusOptions = []
    },
    handleSchoolChange(value) {
      this.queryParams.campusId = undefined
      this.loadCampuses(value)
    },
    handleCampusChange() {
      this.handleQuery()
    },
    handleAdd() {
      if (!this.queryParams.schoolId || !this.queryParams.campusId) {
        this.$modal.msgWarning('请先选择学校和校区')
        return
      }
      this.dialogTitle = '新增地点'
      this.open = true
      this.form = {
        id: undefined,
        schoolId: this.queryParams.schoolId,
        campusId: this.queryParams.campusId,
        locationName: undefined,
        locationType: undefined,
        latitude: undefined,
        longitude: undefined,
        coverImageUrl: undefined,
        descriptionText: '',
        status: 1,
        remark: undefined
      }
    },
    handleUpdate(row) {
      getLocation(row.id).then(res => {
        const data = res.data || res
        this.dialogTitle = '编辑地点'
        this.open = true
        this.form = Object.assign({}, data)
        this.queryParams.schoolId = data.schoolId || this.queryParams.schoolId
        this.loadCampuses(this.queryParams.schoolId)
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) return
        const request = this.form.id ? updateLocation : addLocation
        request(this.form).then(() => {
          this.$modal.msgSuccess('操作成功')
          this.open = false
          this.handleQuery()
        })
      })
    },
    handleDelete(row) {
      const ids = row.id
      this.$modal
        .confirm('是否确认删除地点编号为"' + ids + '"的数据项？')
        .then(() => delLocation(ids))
        .then(() => {
          this.getList()
          this.$modal.msgSuccess('删除成功')
        })
        .catch(() => {})
    },
    handleStatusChange(row, val) {
      const text = val === 1 ? '启用' : '停用'
      changeLocationStatus({ id: row.id, status: val })
        .then(() => this.$modal.msgSuccess(text + '成功'))
        .catch(() => {
          row.status = val === 1 ? 0 : 1
        })
    },
    copyCoordinate(row) {
      const text = `${row.latitude}, ${row.longitude}`
      if (navigator.clipboard && navigator.clipboard.writeText) {
        navigator.clipboard.writeText(text).then(() => this.$modal.msgSuccess('坐标已复制'))
      } else {
        const textarea = document.createElement('textarea')
        textarea.value = text
        document.body.appendChild(textarea)
        textarea.select()
        document.execCommand('copy')
        document.body.removeChild(textarea)
        this.$modal.msgSuccess('坐标已复制')
      }
    },
    typeLabel(value) {
      const hit = this.locationTypeOptions.find(item => item.value === value)
      return hit ? hit.label : '--'
    },
    typeColor(value) {
      switch (value) {
        case 1:
          return 'info'
        case 2:
          return 'warning'
        case 3:
          return 'success'
        case 4:
          return ''
        default:
          return 'default'
      }
    },
    findSchoolName(id) {
      const item = this.schoolOptions.find(opt => opt.value === id)
      return item ? item.label : ''
    },
    validateNumber(rule, value, callback) {
      if (value === undefined || value === null || value === '') {
        callback(new Error('请输入数值'))
        return
      }
      const num = Number(value)
      if (Number.isNaN(num)) {
        callback(new Error('请输入合法数值'))
      } else {
        callback()
      }
    },
    resetForm() {
      this.form = {}
      this.$refs.formRef && this.$refs.formRef.resetFields()
    },
    handleExport() {
      if (!this.queryParams.campusId) {
        this.$modal.msgWarning('请先选择校区后再导出')
        return
      }
      exportLocation(this.queryParams).then(response => {
        this.$download.saveAs(response, '地点数据.xlsx')
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.location-page {
  .filter-card {
    border-radius: 12px;
    .filter-title {
      font-weight: 600;
      color: #303133;
      margin-bottom: 8px;
    }
  }

  .light-form {
    padding: 4px 0;
  }

  .table-card {
    border-radius: 12px;
  }

  .mt16 {
    margin-top: 16px;
  }

  .mb12 {
    margin-bottom: 12px;
  }

  .text-ellipsis {
    display: inline-block;
    max-width: 140px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .danger-text {
    color: #f56c6c;
  }

  .empty-tips {
    padding: 12px;
    color: #909399;
  }
}
</style>

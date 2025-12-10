<template>
  <el-dialog :title="title" :visible.sync="visible" width="700px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="110px">
      <el-form-item label="所属学校" prop="schoolId">
        <treeselect v-model="form.schoolId" :options="schoolTreeOptions" :normalizer="normalizer" placeholder="请选择学校" />
      </el-form-item>
      <el-form-item label="校区名称" prop="campusName">
        <el-input v-model="form.campusName" placeholder="请输入校区名称" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入地址" />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input v-model="form.latitude" placeholder="请输入纬度" />
      </el-form-item>
      <el-form-item label="经度" prop="longitude">
        <el-input v-model="form.longitude" placeholder="请输入经度" />
        <el-button type="text" @click="handlePickCoordinate">选择坐标</el-button>
      </el-form-item>
      <el-form-item label="管理员" prop="managerUserId">
        <el-select v-model="form.managerUserId" placeholder="请选择管理员" clearable filterable>
          <el-option v-for="item in userOptions" :key="item.userId" :label="item.nickName || item.userName" :value="item.userId" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">提交</el-button>
      <el-button @click="visible = false">取消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { addCampus, updateCampus, getCampus } from '@/api/ccp/campus'
import { listUser } from '@/api/system/user'

export default {
  name: 'CampusForm',
  components: { Treeselect },
  props: {
    schoolOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      visible: false,
      title: '',
      form: {},
      userOptions: [],
      rules: {
        schoolId: [{ required: true, message: '请选择所属学校', trigger: 'change' }],
        campusName: [
          { required: true, message: '校区名称不能为空', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在1到100字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    schoolTreeOptions() {
      return this.schoolOptions.map(item => ({ id: item.value, label: item.label }))
    }
  },
  methods: {
    normalizer(node) {
      return { id: node.id, label: node.label, children: node.children }
    },
    open(row) {
      this.reset()
      this.visible = true
      this.loadUsers()
      if (row && row.id) {
        this.title = '修改校区'
        getCampus(row.id).then(res => {
          this.form = Object.assign({ status: 1 }, res.data)
        })
      } else {
        this.title = '新增校区'
        this.form = { status: 1 }
      }
    },
    loadUsers() {
      listUser({ pageNum: 1, pageSize: 500 }).then(res => {
        this.userOptions = res.rows || []
      })
    },
    reset() {
      this.form = {
        id: undefined,
        schoolId: undefined,
        campusName: undefined,
        address: undefined,
        latitude: undefined,
        longitude: undefined,
        managerUserId: undefined,
        status: 1,
        remark: undefined
      }
      this.resetForm('form')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) return
        const request = this.form.id ? updateCampus(this.form) : addCampus(this.form)
        request.then(() => {
          this.$modal.msgSuccess('操作成功')
          this.visible = false
          this.$emit('success')
        })
      })
    },
    handlePickCoordinate() {
      this.$modal.msg('请在地图组件中拾取经纬度后填写到表单中')
    },
    handleClose() {
      this.reset()
    }
  }
}
</script>

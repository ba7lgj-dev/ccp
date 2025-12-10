<template>
  <el-dialog :title="title" :visible.sync="visible" width="600px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="城市" prop="cityId">
        <el-select v-model="form.cityId" placeholder="请选择城市" filterable clearable>
          <el-option v-for="item in cityOptions" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue" />
        </el-select>
      </el-form-item>
      <el-form-item label="学校名称" prop="schoolName">
        <el-input v-model="form.schoolName" placeholder="请输入学校名称" />
      </el-form-item>
      <el-form-item label="学校简称" prop="schoolShortName">
        <el-input v-model="form.schoolShortName" placeholder="请输入学校简称" />
      </el-form-item>
      <el-form-item label="Logo" prop="logoUrl">
        <image-upload v-model="form.logoUrl" :limit="1" />
      </el-form-item>
      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" placeholder="请输入学校地址" />
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
import { addSchool, updateSchool, getSchool } from '@/api/ccp/school'

export default {
  name: 'SchoolForm',
   dicts: ['ccp_city'],
  props: {
    cityOptions: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      visible: false,
      title: '',
      form: {},
      rules: {
        cityId: [{ required: true, message: '请选择城市', trigger: 'change' }],
        schoolName: [
          { required: true, message: '学校名称不能为空', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在1到100字符', trigger: 'blur' }
        ],
        schoolShortName: [{ max: 100, message: '长度不能超过100字符', trigger: 'blur' }],
        address: [{ max: 255, message: '长度不能超过255字符', trigger: 'blur' }]
      }
    }
  },
  methods: {
    open(row) {
      this.reset()
      this.visible = true
      if (row && row.id) {
        this.title = '修改学校'
        getSchool(row.id).then(res => {
          this.form = Object.assign({}, res.data)
        })
      } else {
        this.title = '新增学校'
        this.form = { status: 1 }
      }
    },
    reset() {
      this.form = {
        id: undefined,
        cityId: undefined,
        schoolName: undefined,
        schoolShortName: undefined,
        logoUrl: undefined,
        address: undefined,
        status: 1,
        remark: undefined
      }
      this.resetForm('form')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) {
          return
        }
        const request = this.form.id ? updateSchool(this.form) : addSchool(this.form)
        request.then(() => {
          this.$modal.msgSuccess('操作成功')
          this.visible = false
          this.$emit('success')
        })
      })
    },
    handleClose() {
      this.reset()
    }
  }
}
</script>

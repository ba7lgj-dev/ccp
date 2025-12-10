<template>
  <el-dialog :title="title" :visible.sync="visible" width="650px" @close="handleClose">
    <el-form ref="form" :model="form" :rules="rules" label-width="110px">
      <el-form-item label="所属校区" prop="campusId">
        <el-select v-model="form.campusId" placeholder="请选择校区" clearable filterable>
          <el-option v-for="item in campusOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="校门名称" prop="gateName">
        <el-input v-model="form.gateName" placeholder="请输入校门名称" />
      </el-form-item>
      <el-form-item label="纬度" prop="latitude">
        <el-input v-model="form.latitude" placeholder="请输入纬度" />
      </el-form-item>
      <el-form-item label="经度" prop="longitude">
        <el-input v-model="form.longitude" placeholder="请输入经度" />
        <el-button type="text" @click="handlePickCoordinate">选择坐标</el-button>
      </el-form-item>
      <el-form-item label="排序" prop="sort">
        <el-input-number v-model="form.sort" :min="0" />
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
import { addGate, updateGate, getGate } from '@/api/ccp/gate'

export default {
  name: 'GateForm',
  props: {
    campusOptions: {
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
        campusId: [{ required: true, message: '请选择校区', trigger: 'change' }],
        gateName: [
          { required: true, message: '校门名称不能为空', trigger: 'blur' },
          { min: 1, max: 100, message: '长度在1到100字符', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    open(row) {
      this.reset()
      this.visible = true
      if (row && row.id) {
        this.title = '修改校门'
        getGate(row.id).then(res => {
          this.form = Object.assign({ status: 1 }, res.data)
        })
      } else {
        this.title = '新增校门'
        this.form = { status: 1, sort: 0 }
      }
    },
    reset() {
      this.form = {
        id: undefined,
        campusId: undefined,
        gateName: undefined,
        latitude: undefined,
        longitude: undefined,
        sort: 0,
        status: 1,
        remark: undefined
      }
      this.resetForm('form')
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (!valid) return
        const request = this.form.id ? updateGate(this.form) : addGate(this.form)
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

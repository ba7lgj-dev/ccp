const schoolAuthService = require('../../../services/schoolAuth.js')
const schoolService = require('../../../services/school.js')
const campusService = require('../../../services/campus.js')

Page({
  data: {
    activeTab: 'mine',
    myList: [],
    applyForm: {
      schoolId: '',
      schoolName: '',
      campusId: '',
      campusName: '',
      studentNo: '',
      studentName: '',
      studentCardImageUrl: '',
      extraImageUrl: ''
    },
    schoolOptions: [],
    campusOptions: [],
    loading: false,
    submitting: false,
    detailVisible: false,
    detailItem: null
  },
  onShow() {
    const app = getApp()
    if (app && typeof app.checkAuthChain === 'function') {
      app.checkAuthChain({ from: 'schoolAuth', allowAuthPages: true })
    }
    this.loadMyAuths()
    this.loadSchools()
  },
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    if (tab) {
      this.setData({ activeTab: tab })
      if (tab === 'mine') {
        this.loadMyAuths()
      }
    }
  },
  loadMyAuths() {
    this.setData({ loading: true })
    schoolAuthService.listMine().then((list) => {
      this.setData({ myList: list || [] })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  loadSchools() {
    schoolAuthService.listApproved().then((list) => {
      if (Array.isArray(list) && list.length) {
        this.setData({ schoolOptions: list })
        return
      }
      return schoolService.getSchoolList().then((all) => {
        this.setData({ schoolOptions: all || [] })
      })
    }).catch(() => {})
  },
  onSelectSchool(e) {
    const index = e.detail.value
    const school = this.data.schoolOptions[index]
    if (!school) return
    this.setData({
      applyForm: Object.assign({}, this.data.applyForm, {
        schoolId: school.schoolId || school.id,
        schoolName: school.schoolName || school.name,
        campusId: '',
        campusName: ''
      }),
      campusOptions: []
    })
    this.loadCampusList(school.schoolId || school.id)
  },
  loadCampusList(schoolId) {
    if (!schoolId) return
    campusService.getCampusList(schoolId).then((list) => {
      this.setData({ campusOptions: list || [] })
    })
  },
  onSelectCampus(e) {
    const index = e.detail.value
    const campus = this.data.campusOptions[index]
    if (!campus) return
    this.setData({
      applyForm: Object.assign({}, this.data.applyForm, {
        campusId: campus.id,
        campusName: campus.name
      })
    })
  },
  onInput(e) {
    const field = e.currentTarget.dataset.field
    if (!field) return
    this.setData({ applyForm: Object.assign({}, this.data.applyForm, { [field]: e.detail.value }) })
  },
  uploadStudentCard() {
    this.chooseImage('studentCardImageUrl')
  },
  uploadExtra() {
    this.chooseImage('extraImageUrl')
  },
  chooseImage(field) {
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      success: (res) => {
        const path = res.tempFilePaths && res.tempFilePaths[0]
        if (!path) return
        wx.uploadFile({
          url: require('../../../utils/config.js').BASE_URL + '/mp/upload/image',
          filePath: path,
          name: 'file',
          success: (resp) => {
            try {
              const body = JSON.parse(resp.data)
              const url = body.data && (body.data.path || body.data.url)
              this.setData({ applyForm: Object.assign({}, this.data.applyForm, { [field]: url || '' }) })
            } catch (e) {
              wx.showToast({ title: '上传失败', icon: 'none' })
            }
          },
          fail: () => wx.showToast({ title: '上传失败', icon: 'none' })
        })
      }
    })
  },
  validateForm() {
    const form = this.data.applyForm
    if (!form.schoolId) {
      wx.showToast({ title: '请选择学校', icon: 'none' })
      return false
    }
    if (!form.studentNo || !form.studentName) {
      wx.showToast({ title: '请填写姓名和学号', icon: 'none' })
      return false
    }
    if (!form.studentCardImageUrl) {
      wx.showToast({ title: '请上传学生证照片', icon: 'none' })
      return false
    }
    return true
  },
  submitApply() {
    if (!this.validateForm()) return
    this.setData({ submitting: true })
    const form = this.data.applyForm
    schoolAuthService.apply({
      schoolId: form.schoolId,
      campusId: form.campusId,
      studentNo: form.studentNo,
      studentName: form.studentName,
      studentCardImageUrl: form.studentCardImageUrl,
      extraImageUrl: form.extraImageUrl
    }).then(() => {
      wx.showToast({ title: '提交成功，等待审核', icon: 'success' })
      this.setData({ activeTab: 'mine' })
      this.loadMyAuths()
    }).finally(() => {
      this.setData({ submitting: false })
    })
  },
  showDetail(e) {
    const item = e.currentTarget.dataset.item
    if (!item) return
    this.setData({ detailVisible: true, detailItem: item })
  },
  closeDetail() {
    this.setData({ detailVisible: false, detailItem: null })
  },
  reapply(e) {
    const item = e.currentTarget.dataset.item
    if (!item) return
    this.setData({
      activeTab: 'apply',
      applyForm: Object.assign({}, this.data.applyForm, {
        schoolId: item.schoolId,
        schoolName: item.schoolName,
        campusId: item.campusId || '',
        campusName: item.campusName || '',
        studentNo: item.studentNo || '',
        studentName: item.studentName || ''
      })
    })
    this.loadCampusList(item.schoolId)
  }
})

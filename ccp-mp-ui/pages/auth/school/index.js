const schoolAuthService = require('../../../services/schoolAuth.js')
const schoolService = require('../../../services/school.js')
const campusService = require('../../../services/campus.js')
const { BASE_URL } = require('../../../utils/http.js')

Page({
  data: {
    loading: false,
    tabActive: 'list',
    mySchoolAuthList: [],
    applyForm: {
      schoolId: null,
      schoolName: '',
      campusId: null,
      campusName: '',
      studentNo: '',
      studentName: '',
      studentCardImageUrl: '',
      studentCardImageFullUrl: '',
      extraImageUrl: '',
      extraImageFullUrl: ''
    },
    schoolOptions: [],
    campusOptions: [],
    submitLoading: false
  },
  onLoad() {
    this.getMySchoolAuthList()
    this.loadSchoolOptions()
  },
  onShow() {
    this.getMySchoolAuthList(true)
  },
  switchTab(e) {
    const tab = e.currentTarget.dataset.tab
    this.setData({ tabActive: tab })
  },
  getMySchoolAuthList(hideLoading = false) {
    this.setData({ loading: !hideLoading })
    schoolAuthService.listMine({ hideLoading }).then((list) => {
      this.setData({ mySchoolAuthList: list || [] })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  loadSchoolOptions() {
    schoolService.getSchoolList().then((list) => {
      this.setData({ schoolOptions: list || [] })
    })
  },
  onSelectSchool(e) {
    const index = e.detail.value
    const target = this.data.schoolOptions[index]
    if (!target) return
    this.setData({
      applyForm: {
        ...this.data.applyForm,
        schoolId: target.id,
        schoolName: target.schoolName,
        campusId: null,
        campusName: ''
      }
    })
    this.loadCampusOptions(target.id)
  },
  loadCampusOptions(schoolId) {
    if (!schoolId) {
      this.setData({ campusOptions: [] })
      return
    }
    campusService.getCampusList(schoolId).then((list) => {
      this.setData({ campusOptions: list || [] })
    })
  },
  onSelectCampus(e) {
    const index = e.detail.value
    const target = this.data.campusOptions[index]
    if (!target) return
    this.setData({
      applyForm: {
        ...this.data.applyForm,
        campusId: target.id,
        campusName: target.campusName
      }
    })
  },
  onInput(e) {
    const field = e.currentTarget.dataset.field
    this.setData({ applyForm: { ...this.data.applyForm, [field]: e.detail.value } })
  },
  uploadImage(e) {
    const field = e.currentTarget.dataset.field
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      success: (res) => {
        const filePath = res.tempFilePaths[0]
        const token = wx.getStorageSync('token') || ''
        wx.uploadFile({
          url: BASE_URL + '/mp/upload/image',
          filePath,
          name: 'file',
          header: token ? { Authorization: 'Bearer ' + token } : {},
          success: (uploadRes) => {
            try {
              const body = JSON.parse(uploadRes.data || '{}')
              if (body.code === 0 && body.data && body.data.url) {
                const fullUrl = BASE_URL + body.data.url
                const patch = {}
                patch[field] = body.data.url
                patch[field + 'FullUrl'] = fullUrl
                this.setData({ applyForm: { ...this.data.applyForm, ...patch } })
              } else {
                wx.showToast({ title: body.msg || '上传失败', icon: 'none' })
              }
            } catch (err) {
              wx.showToast({ title: '上传失败', icon: 'none' })
            }
          },
          fail: () => {
            wx.showToast({ title: '上传失败', icon: 'none' })
          }
        })
      }
    })
  },
  previewImage(e) {
    const url = e.currentTarget.dataset.url
    if (url) {
      wx.previewImage({ urls: [url] })
    }
  },
  submitSchoolAuth() {
    const form = this.data.applyForm
    if (!form.schoolId) {
      wx.showToast({ title: '请选择学校', icon: 'none' })
      return
    }
    if (!form.studentNo) {
      wx.showToast({ title: '请输入学号', icon: 'none' })
      return
    }
    if (!form.studentName) {
      wx.showToast({ title: '请输入姓名', icon: 'none' })
      return
    }
    if (!form.studentCardImageUrl) {
      wx.showToast({ title: '请上传学生证照片', icon: 'none' })
      return
    }
    this.setData({ submitLoading: true })
    schoolAuthService.apply({
      schoolId: form.schoolId,
      campusId: form.campusId,
      studentNo: form.studentNo,
      studentName: form.studentName,
      studentCardImageUrl: form.studentCardImageUrl,
      extraImageUrl: form.extraImageUrl
    }).then(() => {
      wx.showToast({ title: '提交成功，等待审核', icon: 'none' })
      this.setData({ tabActive: 'list' })
      this.getMySchoolAuthList(true)
    }).finally(() => {
      this.setData({ submitLoading: false })
    })
  }
})

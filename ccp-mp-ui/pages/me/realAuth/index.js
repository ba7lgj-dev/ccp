const realAuthService = require('../../../services/realAuth.js')
const auth = require('../../../utils/auth.js')
const { buildImageUrl } = require('../../../utils/url.js')

Page({
  data: {
    realAuthStatus: 0,
    realAuthFailReason: '',
    realName: '',
    idCardNumber: '',
    idCardPlaceholder: '',
    faceImageUrl: '',
    faceImagePath: '',
    loading: false,
    submitting: false,
    statusText: '',
    statusDesc: '完成实名认证后即可使用拼车功能'
  },
  onShow() {
    this.loadRealAuthInfo()
  },
  loadRealAuthInfo() {
    this.setData({ loading: true })
    realAuthService.getRealAuthInfo().then((data) => {
      if (!data) return
      const status = typeof data.realAuthStatus === 'number' ? data.realAuthStatus : 0
      const maskedCard = data.idCardMasked || ''
      this.setData({
        realAuthStatus: status,
        realAuthFailReason: data.realAuthFailReason || '',
        realName: data.realName || '',
        idCardNumber: status === 1 || status === 2 ? maskedCard : '',
        idCardPlaceholder: maskedCard,
        faceImageUrl: buildImageUrl(data.faceImageUrl || ''),
        statusText: this.buildStatusText(status),
        statusDesc: this.buildStatusDesc(status, data.realAuthFailReason || '')
      })
      const app = getApp()
      const cached = wx.getStorageSync('userInfo') || {}
      const merged = Object.assign({}, cached, {
        realAuthStatus: status,
        realAuthFailReason: data.realAuthFailReason || '',
        realName: data.realName || cached.realName || ''
      })
      wx.setStorageSync('userInfo', merged)
      if (app && app.globalData) {
        app.globalData.userInfo = merged
      }
    }).catch((err) => {
      if (err && err.code === 4001) {
        auth.clearToken()
        wx.showToast({ title: '登录失效，请重新登录', icon: 'none' })
        wx.reLaunch({ url: '/pages/login/index' })
        return
      }
      wx.showToast({ title: (err && err.message) || '加载失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  buildStatusText(status) {
    if (status === 2) return '已通过实名认证'
    if (status === 1) return '审核中'
    if (status === 3) return '审核不通过'
    return '未认证'
  },
  buildStatusDesc(status, reason) {
    if (status === 2) return '您的实名信息已通过审核'
    if (status === 1) return '审核中，请耐心等待，结果将显示在此页'
    if (status === 3) return `审核不通过：${reason || '请修改后重新提交'}`
    return '请填写真实姓名与身份证号后提交审核'
  },
  onRealNameInput(e) {
    this.setData({ realName: e.detail.value })
  },
  onIdCardInput(e) {
    this.setData({ idCardNumber: e.detail.value })
  },
  onChooseImage() {
    if (this.data.realAuthStatus === 1 || this.data.realAuthStatus === 2) {
      return
    }
    wx.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      success: (res) => {
        if (!res.tempFilePaths || !res.tempFilePaths.length) return
        const path = res.tempFilePaths[0]
        wx.showLoading({ title: '上传中', mask: true })
        realAuthService.uploadRealAuthImage(path).then((data) => {
          const storedPath = data && (data.path || data.url)
          this.setData({
            faceImagePath: storedPath || '',
            faceImageUrl: buildImageUrl(storedPath || '')
          })
        }).catch((err) => {
          wx.showToast({ title: (err && err.message) || '上传失败', icon: 'none' })
        }).finally(() => {
          wx.hideLoading()
        })
      }
    })
  },
  validateForm() {
    const { realName, idCardNumber } = this.data
    if (!realName || realName.trim().length < 2) {
      wx.showToast({ title: '请输入真实姓名', icon: 'none' })
      return false
    }
    const idCard = (idCardNumber || '').trim()
    if (idCard.length !== 15 && idCard.length !== 18) {
      wx.showToast({ title: '身份证号需为15或18位', icon: 'none' })
      return false
    }
    return true
  },
  onSubmit() {
    if (this.data.realAuthStatus === 1 || this.data.realAuthStatus === 2) {
      return
    }
    if (!this.validateForm()) {
      return
    }
    this.setData({ submitting: true })
    realAuthService.applyRealAuth({
      realName: this.data.realName.trim(),
      idCardNumber: (this.data.idCardNumber || '').trim(),
      faceImageUrl: this.data.faceImagePath || ''
    }).then(() => {
      wx.showToast({ title: '提交成功，等待审核', icon: 'success' })
      const status = 1
      this.setData({
        realAuthStatus: status,
        realAuthFailReason: '',
        statusText: this.buildStatusText(status),
        statusDesc: this.buildStatusDesc(status, '')
      })
      const cached = wx.getStorageSync('userInfo') || {}
      const merged = Object.assign({}, cached, { realAuthStatus: status, realAuthFailReason: '' })
      wx.setStorageSync('userInfo', merged)
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.userInfo = merged
      }
    }).catch((err) => {
      wx.showToast({ title: (err && err.message) || '提交失败', icon: 'none' })
    }).finally(() => {
      this.setData({ submitting: false })
    })
  }
})

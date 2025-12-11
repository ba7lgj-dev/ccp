const realAuthService = require('../../../services/realAuth.js')
const { BASE_URL } = require('../../../utils/http.js')

Page({
  data: {
    loading: false,
    submitLoading: false,
    realAuthStatus: 0,
    realName: '',
    idCardNumber: '',
    faceImageUrl: '',
    faceImageFullUrl: '',
    failReason: '',
    userInfo: {
      nickName: '',
      avatarUrl: ''
    }
  },
  onLoad() {
    this.loadInfo()
  },
  onShow() {
    this.loadInfo(true)
  },
  loadInfo(hideLoading = false) {
    this.setData({ loading: !hideLoading })
    realAuthService.getInfo({ hideLoading }).then((info) => {
      if (!info) return
      const faceImageFullUrl = info.faceImageUrl ? BASE_URL + info.faceImageUrl : ''
      this.setData({
        loading: false,
        realAuthStatus: info.realAuthStatus || 0,
        realName: info.realName || '',
        idCardNumber: info.idCardNumberMasked || '',
        faceImageUrl: info.faceImageUrl || '',
        faceImageFullUrl,
        failReason: info.realAuthFailReason || '',
        userInfo: {
          nickName: info.nickName || '',
          avatarUrl: info.avatarUrl ? BASE_URL + info.avatarUrl : ''
        }
      })
    }).catch(() => {
      this.setData({ loading: false })
    })
  },
  handleNameInput(e) {
    this.setData({ realName: e.detail.value })
  },
  handleIdInput(e) {
    this.setData({ idCardNumber: e.detail.value })
  },
  chooseImage() {
    if (this.data.realAuthStatus === 1 || this.data.realAuthStatus === 2) {
      return
    }
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
                this.setData({
                  faceImageUrl: body.data.url,
                  faceImageFullUrl: BASE_URL + body.data.url
                })
              } else {
                wx.showToast({ title: body.msg || '上传失败', icon: 'none' })
              }
            } catch (e) {
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
  previewImage() {
    if (this.data.faceImageFullUrl) {
      wx.previewImage({ urls: [this.data.faceImageFullUrl] })
    }
  },
  submitRealAuth() {
    if (this.data.realAuthStatus === 1 || this.data.realAuthStatus === 2) {
      return
    }
    const { realName, idCardNumber, faceImageUrl } = this.data
    if (!realName || !idCardNumber || !faceImageUrl) {
      wx.showToast({ title: '请完整填写信息', icon: 'none' })
      return
    }
    if (idCardNumber.length < 15 || idCardNumber.length > 18) {
      wx.showToast({ title: '身份证格式不正确', icon: 'none' })
      return
    }
    this.setData({ submitLoading: true })
    realAuthService.apply({ realName, idCardNumber, faceImageUrl }).then(() => {
      wx.showToast({ title: '提交成功，等待审核', icon: 'none' })
      this.setData({ realAuthStatus: 1 })
    }).finally(() => {
      this.setData({ submitLoading: false })
    })
  },
  goHome() {
    wx.switchTab({ url: '/pages/index/index' })
  }
})

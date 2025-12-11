const auth = require('./utils/auth.js')
const { buildImageUrl } = require('./utils/url.js')

App({
  onLaunch() {
    const token = wx.getStorageSync('token') || null
    const userInfo = wx.getStorageSync('userInfo') || null
    const normalizedUser = userInfo ? { ...userInfo, avatarUrl: buildImageUrl(userInfo.avatarUrl) } : null
    if (!token) {
      auth.login().then((info) => {
        this.setGlobalUser(info)
        this.redirectByUser(info)
      }).catch(() => {
        wx.showToast({ title: '登录失败，请重试', icon: 'none' })
      })
      return
    }
    this.globalData.token = token
    this.globalData.userInfo = normalizedUser
    this.redirectByUser(normalizedUser || {})
  },
  redirectByUser(userInfo) {
    if (!userInfo || !userInfo.id) {
      wx.redirectTo({ url: '/pages/login/index' })
      return
    }
    const selectedSchool = wx.getStorageSync('selectedSchool')
    const selectedCampus = wx.getStorageSync('selectedCampus')
    if (!selectedSchool) {
      wx.redirectTo({ url: '/pages/school/select/index' })
      return
    }
    if (!selectedCampus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    wx.switchTab({ url: '/pages/index/index' })
  },
  setGlobalUser(userInfo) {
    this.globalData.userInfo = userInfo
    if (userInfo) {
      this.globalData.token = wx.getStorageSync('token') || null
      wx.setStorageSync('userInfo', userInfo)
    }
  },
  globalData: {
    token: null,
    userInfo: null,
    selectedSchool: null,
    selectedCampus: null
  }
})

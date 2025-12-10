const auth = require('./utils/auth.js')

App({
  onLaunch() {
    const token = wx.getStorageSync('token') || null
    const userInfo = wx.getStorageSync('userInfo') || null
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
    this.globalData.userInfo = userInfo || null
    this.redirectByUser(userInfo || {})
  },
  redirectByUser(userInfo) {
    if (!userInfo || !userInfo.id) {
      wx.redirectTo({ url: '/pages/login/index' })
      return
    }
    wx.redirectTo({ url: '/pages/index/index' })
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

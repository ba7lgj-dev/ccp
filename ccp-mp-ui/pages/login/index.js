const auth = require('../../utils/auth.js')

Page({
  data: {
    loading: false
  },
  onLoad() {},
  onLoginTap() {
    wx.showLoading({ title: '登录中', mask: true })
    auth.login().then((userInfo) => {
      const info = wx.getStorageSync('userInfo') || userInfo || {}
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.userInfo = info
      }
      this.redirectByUser(info)
    }).catch(() => {
      wx.showToast({ title: '登录失败', icon: 'none' })
    }).finally(() => {
      wx.hideLoading()
    })
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
  onTapAgreement() {
    wx.showToast({ title: '隐私协议页面待实现', icon: 'none' })
  }
})

const auth = require('../../utils/auth.js')
const { buildImageUrl } = require('../../utils/url.js')

Page({
  data: {
    loading: false
  },
  onLoad() {},
  onLoginTap() {
    if (this.data.loading) return
    this.setData({ loading: true })
    auth.login().then((userInfo) => {
      const info = wx.getStorageSync('userInfo') || userInfo || {}
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.userInfo = { ...info, avatarUrl: buildImageUrl(info.avatarUrl) }
      }
      if (app && typeof app.checkAuthChain === 'function') {
        app.checkAuthChain({ from: 'login', forceRefresh: true, allowAuthPages: true })
      } else {
        this.redirectByUser(info)
      }
    }).catch((err) => {
      wx.showToast({ title: err.message || '登录失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  redirectByUser(userInfo) {
    if (!userInfo || !userInfo.id) {
      wx.redirectTo({ url: '/pages/login/index' })
      return
    }
    if (userInfo.realAuthStatus !== 2) {
      wx.navigateTo({ url: '/pages/me/realAuth/index' })
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

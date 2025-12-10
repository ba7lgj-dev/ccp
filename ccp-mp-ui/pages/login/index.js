const authService = require('../../services/auth.js')

Page({
  data: {
    loading: false
  },
  onLoad() {},
  onGetPhoneNumber(e) {
    if (e.detail.errMsg && e.detail.errMsg.indexOf('ok') !== -1) {
      wx.showLoading({ title: '登录中', mask: true })
      wx.login({
        success: (res) => {
          const jsCode = res.code
          if (!jsCode) {
            wx.hideLoading()
            wx.showToast({ title: '微信登录失败', icon: 'none' })
            return
          }
          authService.login(jsCode).then((userInfo) => {
            authService.bindPhone(e.detail.encryptedData, e.detail.iv).finally(() => {
              const info = wx.getStorageSync('userInfo') || userInfo || {}
              const app = getApp()
              if (app && app.globalData) {
                app.globalData.userInfo = info
              }
              this.redirectByUser(info)
              wx.hideLoading()
            })
          }).catch(() => {
            wx.hideLoading()
            wx.showToast({ title: '登录失败', icon: 'none' })
          })
        },
        fail: () => {
          wx.hideLoading()
          wx.showToast({ title: '微信登录失败', icon: 'none' })
        }
      })
    } else {
      wx.showToast({ title: '需要手机号授权才能继续使用', icon: 'none' })
    }
  },
  redirectByUser(userInfo) {
    if (!userInfo || !userInfo.id) {
      wx.redirectTo({ url: '/pages/login/index' })
      return
    }
    wx.redirectTo({ url: '/pages/index/index' })
  },
  onTapAgreement() {
    wx.showToast({ title: '隐私协议页面待实现', icon: 'none' })
  }
})

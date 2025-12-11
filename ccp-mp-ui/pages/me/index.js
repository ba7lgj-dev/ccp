const auth = require('../../utils/auth.js')

Page({
  data: {
    loggedIn: false,
    userInfo: null,
    selectedSchool: null,
    selectedCampus: null
  },
  onShow() {
    const token = wx.getStorageSync('token') || null
    if (!token) {
      this.setData({
        loggedIn: false,
        userInfo: null,
        selectedSchool: null,
        selectedCampus: null
      })
      return
    }
    const selectedSchool = wx.getStorageSync('selectedSchool') || null
    const selectedCampus = wx.getStorageSync('selectedCampus') || null
    const app = getApp()
    const userInfo = (app && app.globalData && app.globalData.userInfo) || wx.getStorageSync('userInfo') || null
    if (app && app.globalData) {
      app.globalData.selectedSchool = selectedSchool
      app.globalData.selectedCampus = selectedCampus
    }
    this.setData({
      loggedIn: true,
      userInfo,
      selectedSchool,
      selectedCampus
    })
  },
  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  onSelectSchool() {
    wx.navigateTo({ url: '/pages/school/select/index' })
  },
  onSelectCampus() {
    const selectedSchool = wx.getStorageSync('selectedSchool') || null
    if (!selectedSchool) {
      wx.navigateTo({ url: '/pages/school/select/index' })
      return
    }
    wx.navigateTo({ url: '/pages/campus/select/index' })
  },
  onLogout() {
    wx.showModal({
      title: '退出登录',
      content: '退出后需要重新登录才能使用拼车服务',
      success: (res) => {
        if (res.confirm) {
          auth.clearToken()
          wx.removeStorageSync('selectedSchool')
          wx.removeStorageSync('selectedCampus')
          const app = getApp()
          if (app && app.globalData) {
            app.globalData.token = null
            app.globalData.userInfo = null
            app.globalData.selectedSchool = null
            app.globalData.selectedCampus = null
          }
          this.setData({
            loggedIn: false,
            userInfo: null,
            selectedSchool: null,
            selectedCampus: null
          })
          wx.redirectTo({ url: '/pages/login/index' })
        }
      }
    })
  }
})

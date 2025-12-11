const app = getApp()

Page({
  data: {
    userInfo: null,
    selectedSchool: null,
    selectedCampus: null,
    loginRequired: false
  },
  onShow() {
    const token = wx.getStorageSync('token') || null
    if (!token) {
      this.setData({
        loginRequired: true,
        userInfo: null,
        selectedSchool: null,
        selectedCampus: null
      })
      return
    }
    const selectedSchool = wx.getStorageSync('selectedSchool') || null
    const selectedCampus = wx.getStorageSync('selectedCampus') || null
    if (!selectedSchool) {
      wx.redirectTo({ url: '/pages/school/select/index' })
      return
    }
    if (!selectedCampus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    const userInfo = (app && app.globalData && app.globalData.userInfo) || wx.getStorageSync('userInfo') || null
    if (app && app.globalData) {
      app.globalData.selectedSchool = selectedSchool
      app.globalData.selectedCampus = selectedCampus
    }
    this.setData({
      loginRequired: false,
      userInfo,
      selectedSchool,
      selectedCampus
    })
  },
  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  onGoPublish() {
    wx.navigateTo({ url: '/pages/trip/publish/index' })
  },
  onGoHall() {
    wx.switchTab({ url: '/pages/trip/hall/index' })
  }
})

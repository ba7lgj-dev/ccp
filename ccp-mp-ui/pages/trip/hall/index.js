Page({
  data: {
    hasToken: false
  },
  onShow() {
    const token = wx.getStorageSync('token') || null
    this.setData({ hasToken: !!token })
  },
  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  onGoPublish() {
    wx.navigateTo({ url: '/pages/trip/publish/index' })
  }
})

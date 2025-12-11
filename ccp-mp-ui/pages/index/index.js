const app = getApp()
const tripService = require('../../services/trip.js')
const { buildImageUrl } = require('../../utils/url.js')

function normalizeUserInfo(userInfo) {
  if (!userInfo || typeof userInfo !== 'object') {
    return null
  }
  return { ...userInfo, avatarUrl: buildImageUrl(userInfo.avatarUrl) }
}

function requireRealAuth(onCancel) {
  const app = getApp()
  const userInfo = (app && app.globalData && app.globalData.userInfo) || wx.getStorageSync('userInfo') || {}
  const status = typeof userInfo.realAuthStatus === 'number' ? userInfo.realAuthStatus : 0
  if (status === 2) {
    return true
  }
  wx.showModal({
    title: '温馨提示',
    content: '使用拼车功能前需要完成实名认证，是否前往实名认证？',
    success: (res) => {
      if (res.confirm) {
        wx.navigateTo({ url: '/pages/me/realAuth/index' })
      } else if (typeof onCancel === 'function') {
        onCancel()
      }
    }
  })
  return false
}

Page({
  data: {
    userInfo: null,
    selectedSchool: null,
    selectedCampus: null,
    loginRequired: false,
    activeTrip: null
  },
  onShow() {
    const appInstance = getApp()
    if (appInstance && typeof appInstance.checkAuthChain === 'function') {
      appInstance.checkAuthChain({ from: 'index', allowAuthPages: false })
    }
    const token = wx.getStorageSync('token') || null
    if (!token || (appInstance && appInstance.globalData && (appInstance.globalData.auth.realAuthStatus !== 2 || !appInstance.globalData.auth.hasApprovedSchool))) {
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
    const userInfo = normalizeUserInfo((app && app.globalData && app.globalData.userInfo) || wx.getStorageSync('userInfo') || null)
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
    if (!requireRealAuth(() => wx.switchTab({ url: '/pages/me/index' }))) {
      return
    }
    this.loadActiveTrip()
  },
  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  onGoPublish() {
    wx.navigateTo({ url: '/pages/trip/publish/index' })
  },
  onGoHall() {
    wx.switchTab({ url: '/pages/trip/hall/index' })
  },
  loadActiveTrip() {
    tripService.getMyActiveTrip().then((vo) => {
      this.setData({ activeTrip: vo || null })
    }).catch(() => {
      this.setData({ activeTrip: null })
    })
  },
  onTapActiveTrip() {
    if (this.data.activeTrip && this.data.activeTrip.tripId) {
      wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${this.data.activeTrip.tripId}` })
    }
  }
})

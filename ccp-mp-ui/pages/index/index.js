const app = getApp()
const tripService = require('../../services/trip.js')
const userService = require('../../services/user.js')
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
    activeTrip: null,
    myHistoryTrips: [],
    historyFetchAt: 0
  },
  async onShow() {
    const appInstance = getApp()
    if (appInstance && typeof appInstance.checkAuthChain === 'function') {
      await appInstance.checkAuthChain({ from: 'index' })
    }
    const token = wx.getStorageSync('token') || null
    if (!token || (appInstance && appInstance.globalData && (appInstance.globalData.auth.realAuthStatus !== 2 || !appInstance.globalData.auth.hasApprovedSchool))) {
      this.setData({
        loginRequired: true,
        userInfo: null,
        selectedSchool: null,
        selectedCampus: null,
        myHistoryTrips: []
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
    this.loadProfile()
    this.loadActiveTrip()
    this.loadMyHistoryTrips()
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
  async loadProfile() {
    try {
      const profile = await userService.getProfile()
      const normalized = normalizeUserInfo(profile)
      this.setData({ userInfo: normalized })
      if (app && app.globalData) {
        app.globalData.userInfo = normalized
        wx.setStorageSync('userInfo', normalized)
      }
    } catch (err) {
      wx.showToast({ title: '加载用户信息失败', icon: 'none' })
    }
  },
  loadMyHistoryTrips() {
    const appInstance = getApp() || {}
    const cache = (appInstance.globalData && appInstance.globalData.indexCache) || {}
    const now = Date.now()
    const lastFetch = cache.historyFetchAt || this.data.historyFetchAt || 0
    if (now - lastFetch < 5000 && (cache.myHistoryTrips || this.data.myHistoryTrips.length > 0)) {
      this.setData({ myHistoryTrips: cache.myHistoryTrips || this.data.myHistoryTrips, historyFetchAt: lastFetch })
      return
    }
    tripService.getMyHistoryTrips().then((list) => {
      const mapped = (list || []).map((item) => {
        const departureTimeText = this.formatDepartureTime(item.departureTime)
        const statusText = this.formatStatusText(item.status)
        return Object.assign({}, item, { departureTimeText, statusText })
      })
      if (appInstance && appInstance.globalData) {
        appInstance.globalData.indexCache = appInstance.globalData.indexCache || {}
        appInstance.globalData.indexCache.myHistoryTrips = mapped
        appInstance.globalData.indexCache.historyFetchAt = now
      }
      this.setData({ myHistoryTrips: mapped, historyFetchAt: now })
    }).catch(() => {
      wx.showToast({ title: '加载历史订单失败', icon: 'none' })
    })
  },
  formatDepartureTime(timeStr) {
    if (!timeStr) return ''
    const safeStr = (timeStr || '').replace(/-/g, '/')
    const date = new Date(safeStr)
    if (Number.isNaN(date.getTime())) return timeStr
    const y = date.getFullYear()
    const m = `${date.getMonth() + 1}`.padStart(2, '0')
    const d = `${date.getDate()}`.padStart(2, '0')
    const hh = `${date.getHours()}`.padStart(2, '0')
    const mm = `${date.getMinutes()}`.padStart(2, '0')
    return `${y}-${m}-${d} ${hh}:${mm}`
  },
  formatStatusText(status) {
    switch (status) {
      case 4:
        return '已取消'
      case 5:
        return '已过期'
      case 2:
        return '拼单成功'
      case 3:
        return '已完成'
      default:
        return '已完成'
    }
  },
  onTapActiveTrip() {
    if (this.data.activeTrip && this.data.activeTrip.tripId) {
      wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${this.data.activeTrip.tripId}` })
    }
  },
  onTapHistoryTrip(e) {
    const tripId = e.currentTarget.dataset.tripid
    if (tripId) {
      wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${tripId}` })
    }
  }
})

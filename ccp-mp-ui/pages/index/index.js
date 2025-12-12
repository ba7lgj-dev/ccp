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
    historyLastLoadTime: 0,
    historyLoading: false
  },
  async onShow() {
    const appInstance = getApp()
    if (appInstance && typeof appInstance.checkAuthChain === 'function') {
      const passed = await appInstance.checkAuthChain({ from: 'index', allowAuthPages: false })
      if (!passed) {
        this.setData({ loginRequired: true })
        return
      }
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
    const selectedSchool = wx.getStorageSync('selectedSchool') || appInstance.globalData.school || null
    const selectedCampus = wx.getStorageSync('selectedCampus') || appInstance.globalData.campus || null
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
      app.globalData.school = selectedSchool
      app.globalData.campus = selectedCampus
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
    this.loadUserProfile()
    this.loadActiveTrip()
    this.loadMyHistoryTripsWithCache()
  },
  onGoLogin() {
    wx.navigateTo({ url: '/pages/login/index' })
  },
  onGoPublish() {
    wx.navigateTo({ url: '/pages/trip/publish/index' })
  },
  onGoOrders() {
    wx.navigateTo({ url: '/pages/me/routes/index' })
  },
  onSwitchSchool() {
    wx.navigateTo({ url: '/pages/school/select/index' })
  },
  onGoProfile() {
    wx.switchTab({ url: '/pages/me/index' })
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
  },
  loadUserProfile() {
    const cachedUser = normalizeUserInfo((app && app.globalData && app.globalData.userInfo) || wx.getStorageSync('userInfo') || null)
    if (cachedUser) {
      this.setData({ userInfo: cachedUser })
    }
    userService.getProfile().then((profile) => {
      const normalized = normalizeUserInfo(profile)
      if (normalized) {
        this.setData({ userInfo: normalized })
        if (app && typeof app.setGlobalUser === 'function') {
          app.setGlobalUser(normalized)
        }
      }
    }).catch(() => {
      wx.showToast({ title: '获取用户信息失败', icon: 'none' })
    })
  },
  loadMyHistoryTripsWithCache() {
    const now = Date.now()
    const cache = (app && app.globalData && app.globalData.indexCache) || {}
    const last = this.data.historyLastLoadTime || cache.historyLastLoadTime
    const cachedList = (this.data.myHistoryTrips && this.data.myHistoryTrips.length > 0)
      ? this.data.myHistoryTrips
      : cache.myHistoryTrips
    if (last && cachedList && cachedList.length > 0 && (now - last < 5000)) {
      this.setData({ myHistoryTrips: cachedList, historyLastLoadTime: last })
      return
    }
    this.loadMyHistoryTrips()
  },
  loadMyHistoryTrips() {
    this.setData({ historyLoading: true })
    tripService.getMyHistoryTrips().then((list) => {
      const now = Date.now()
      const formatted = Array.isArray(list) ? list.map(item => this.formatHistoryTrip(item)) : []
      this.setData({ myHistoryTrips: formatted, historyLastLoadTime: now })
      if (app && app.globalData) {
        app.globalData.indexCache = app.globalData.indexCache || {}
        app.globalData.indexCache.myHistoryTrips = formatted
        app.globalData.indexCache.historyLastLoadTime = now
      }
    }).catch(() => {
      wx.showToast({ title: '加载历史订单失败', icon: 'none' })
    }).finally(() => {
      this.setData({ historyLoading: false })
    })
  },
  formatHistoryTrip(item) {
    const status = typeof item.status === 'number' ? item.status : null
    return {
      ...item,
      departureTimeText: this.formatDateTime(item.departureTime),
      statusText: this.getStatusText(status),
      isOwner: Boolean(item.isOwner)
    }
  },
  formatDateTime(value) {
    if (!value) return ''
    const date = typeof value === 'string' ? new Date(value.replace(/-/g, '/')) : new Date(value)
    if (Number.isNaN(date.getTime())) {
      return ''
    }
    const pad = (num) => `${num}`.padStart(2, '0')
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`
  },
  getStatusText(status) {
    switch (status) {
      case 2:
        return '拼单成功'
      case 3:
        return '已完成'
      case 4:
        return '已取消'
      case 5:
        return '已过期'
      default:
        return '进行中'
    }
  },
  onTapHistoryTrip(e) {
    const id = e.currentTarget.dataset.id
    if (id) {
      wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${id}` })
    }
  }
})

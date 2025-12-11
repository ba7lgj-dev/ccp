const tripService = require('../../../services/trip.js')
const auth = require('../../../utils/auth.js')

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

const IMMEDIATE_BEFORE_MINUTES = 10 * 60 * 1000
const IMMEDIATE_AFTER_MINUTES = 30 * 60 * 1000

Page({
  data: {
    selectedCampus: null,
    immediateList: [],
    reserveList: [],
    loading: false,
    refresherTriggered: false
  },
  onShow() {
    const token = wx.getStorageSync('token') || ''
    if (!token) {
      auth.reLogin().then(() => {
        this.onShow()
      }).catch(() => {
        wx.showToast({ title: '请先登录后查看', icon: 'none' })
        wx.navigateTo({ url: '/pages/login/index' })
      })
      return
    }
    const campus = wx.getStorageSync('selectedCampus')
    if (!campus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    this.setData({ selectedCampus: campus })
    if (!requireRealAuth(() => wx.switchTab({ url: '/pages/me/index' }))) {
      this.setData({ immediateList: [], reserveList: [] })
      return
    }
    this.loadTripHall()
  },
  onPullDownRefresh() {
    this.loadTripHall()
    wx.stopPullDownRefresh()
  },
  onRefresherRefresh() {
    this.setData({ refresherTriggered: true })
    this.loadTripHall(() => {
      this.setData({ refresherTriggered: false })
    })
  },
  loadTripHall(done) {
    const campus = this.data.selectedCampus
    if (!campus) return
    this.setData({ loading: true })
    tripService.getTripHall(campus.id).then((list) => {
      const now = Date.now()
      const immediate = []
      const reserve = []
      const parsedList = (list || []).map(item => this.formatTripItem(item))
      parsedList.sort((a, b) => a.departureTimestamp - b.departureTimestamp)
      parsedList.forEach(item => {
        const diff = item.departureTimestamp - now
        if (diff >= -IMMEDIATE_BEFORE_MINUTES && diff <= IMMEDIATE_AFTER_MINUTES) {
          immediate.push(item)
        } else if (diff > IMMEDIATE_AFTER_MINUTES) {
          reserve.push(item)
        }
      })
      this.setData({ immediateList: immediate, reserveList: reserve })
    }).catch(() => {
      wx.showToast({ title: '加载大厅失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
      if (typeof done === 'function') {
        done()
      }
    })
  },
  formatTripItem(item) {
    let departureTimestamp = new Date((item.departureTime || '').replace(/-/g, '/')).getTime()
    if (Number.isNaN(departureTimestamp)) {
      departureTimestamp = Date.now()
    }
    const timeText = this.formatFriendlyTime(departureTimestamp)
    return Object.assign({}, item, {
      departureTimestamp,
      departureTimeFormat: timeText,
      requireText: item.requireText || '无特殊要求'
    })
  },
  formatFriendlyTime(timestamp) {
    if (!timestamp) return ''
    const now = new Date()
    const dep = new Date(timestamp)
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime()
    const tomorrow = today + 24 * 60 * 60 * 1000
    const depDay = new Date(dep.getFullYear(), dep.getMonth(), dep.getDate()).getTime()
    const hh = `${dep.getHours()}`.padStart(2, '0')
    const mm = `${dep.getMinutes()}`.padStart(2, '0')
    if (depDay === today) {
      return `今天 ${hh}:${mm}`
    }
    if (depDay === tomorrow) {
      return `明天 ${hh}:${mm}`
    }
    return `${dep.getMonth() + 1}月${dep.getDate()}日 ${hh}:${mm}`
  },
  onChangeCampus() {
    wx.redirectTo({ url: '/pages/campus/select/index' })
  },
  onTapTrip(e) {
    const id = e.currentTarget.dataset.id
    if (id) {
      wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${id}` })
    }
  }
})

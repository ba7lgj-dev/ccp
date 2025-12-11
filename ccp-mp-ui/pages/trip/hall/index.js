const tripService = require('../../../services/trip.js')

const IMMEDIATE_BEFORE_MINUTES = 10 * 60 * 1000
const IMMEDIATE_AFTER_MINUTES = 30 * 60 * 1000

Page({
  data: {
    selectedCampus: null,
    immediateList: [],
    reserveList: [],
    loading: false
  },
  onShow() {
    const campus = wx.getStorageSync('selectedCampus')
    if (!campus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    this.setData({ selectedCampus: campus })
    this.loadTripHall()
  },
  loadTripHall() {
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
      wx.showToast({ title: '详情页开发中', icon: 'none' })
    }
  }
})

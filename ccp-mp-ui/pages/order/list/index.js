const tripService = require('../../../services/trip.js')

Page({
  data: {
    loading: false,
    activeTrip: null,
    historyTrips: []
  },
  onShow() {
    this.loadData()
  },
  loadData() {
    this.setData({ loading: true })
    Promise.all([
      tripService.getMyActiveTrip(),
      tripService.getMyHistoryTrips()
    ]).then(([active, history]) => {
      this.setData({
        activeTrip: active || null,
        historyTrips: Array.isArray(history) ? history : []
      })
    }).catch(() => {
      wx.showToast({ title: '加载订单失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  goDetail(e) {
    const tripId = e.currentTarget.dataset.tripId
    if (!tripId) return
    wx.navigateTo({ url: `/pages/trip/detail/index?tripId=${tripId}` })
  }
})

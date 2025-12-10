const gateService = require('../../../services/gate.js')
const cache = require('../../../utils/cache.js')

Page({
  data: {
    selectedCampus: null,
    gates: [],
    gateNames: [],
    startGateIndex: -1,
    endGateIndex: -1
  },
  onLoad() {
    const campus = wx.getStorageSync('selectedCampus')
    if (!campus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    this.setData({ selectedCampus: campus })
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.selectedCampus = campus
    }
    const cached = cache.getGateCache(campus.id)
    if (cached) {
      this.setGates(cached)
    } else {
      this.loadGateList()
    }
  },
  loadGateList() {
    const campusId = this.data.selectedCampus.id
    gateService.getGateList(campusId).then((list) => {
      const sorted = (list || []).slice().sort((a, b) => {
        const sortA = typeof a.sort === 'number' ? a.sort : 0
        const sortB = typeof b.sort === 'number' ? b.sort : 0
        if (sortA === sortB) {
          return (a.id || 0) - (b.id || 0)
        }
        return sortA - sortB
      })
      this.setGates(sorted)
      cache.setGateCache(campusId, sorted, 10 * 60 * 1000)
    }).catch(() => {
      wx.showToast({ title: '加载校门失败', icon: 'none' })
    })
  },
  setGates(gates) {
    const names = (gates || []).map(item => item.gateName)
    this.setData({ gates, gateNames: names })
  },
  onStartGateChange(e) {
    this.setData({ startGateIndex: Number(e.detail.value) })
  },
  onEndGateChange(e) {
    this.setData({ endGateIndex: Number(e.detail.value) })
  },
  onNextStep() {
    wx.showToast({ title: '后续功能待实现', icon: 'none' })
  }
})

const campusService = require('../../../services/campus.js')
const cache = require('../../../utils/cache.js')

Page({
  data: {
    selectedSchool: null,
    campusList: []
  },
  async onShow() {
    const app = getApp()
    if (app && typeof app.checkAuthChain === 'function') {
      const ok = await app.checkAuthChain({ from: 'campusSelect', allowAuthPages: true })
      if (!ok) return
    }
    const selectedSchool = wx.getStorageSync('selectedSchool') || (getApp().globalData && getApp().globalData.school)
    if (!selectedSchool) {
      wx.showToast({ title: '请先选择学校', icon: 'none' })
      wx.redirectTo({ url: '/pages/school/select/index' })
      return
    }
    this.setData({ selectedSchool })
    const appInstance = getApp()
    if (appInstance && appInstance.globalData) {
      appInstance.globalData.school = selectedSchool
    }
    const cached = cache.getCampusCache(selectedSchool.id)
    if (cached) {
      this.setData({ campusList: cached })
      return
    }
    this.loadCampusList(selectedSchool.id)
  },
  loadCampusList(schoolId) {
    campusService.getCampusList(schoolId).then((list) => {
      const result = (list || []).slice().sort((a, b) => {
        return (a.campusName || '').localeCompare(b.campusName || '')
      })
      if (!result.length) {
        wx.showToast({ title: '该学校暂无可用校区，请联系管理员', icon: 'none' })
      }
      this.setData({ campusList: result })
      cache.setCampusCache(schoolId, result, 10 * 60 * 1000)
    }).catch(() => {
      wx.showToast({ title: '加载校区失败', icon: 'none' })
    })
  },
  onSelectCampus(e) {
    const id = e.currentTarget.dataset.id
    const campus = this.data.campusList.find(item => item.id === id)
    if (!campus) {
      wx.showToast({ title: '未找到校区信息', icon: 'none' })
      return
    }
    wx.setStorageSync('selectedCampus', campus)
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.campus = campus
    }
    wx.redirectTo({ url: '/pages/trip/publish/index' })
  }
})

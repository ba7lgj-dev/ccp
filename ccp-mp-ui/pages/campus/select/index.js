const campusService = require('../../../services/campus.js')
const cache = require('../../../utils/cache.js')

Page({
  data: {
    selectedSchool: null,
    campusList: []
  },
  onLoad() {
    const selectedSchool = wx.getStorageSync('selectedSchool')
    if (!selectedSchool) {
      wx.redirectTo({ url: '/pages/school/select/index' })
      return
    }
    this.setData({ selectedSchool })
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.selectedSchool = selectedSchool
    }
    const cached = cache.getCampusCache(selectedSchool.id)
    if (cached) {
      this.setData({ campusList: cached })
    } else {
      this.loadCampusList()
    }
  },
  loadCampusList() {
    const school = this.data.selectedSchool
    campusService.getCampusList(school.id).then((list) => {
      const result = (list || []).slice().sort((a, b) => {
        return (a.campusName || '').localeCompare(b.campusName || '')
      })
      if (!result.length) {
        wx.showToast({ title: '该学校暂未录入校区，请联系管理员', icon: 'none' })
      }
      this.setData({ campusList: result })
      cache.setCampusCache(school.id, result, 10 * 60 * 1000)
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
      app.globalData.selectedCampus = campus
    }
    wx.redirectTo({ url: '/pages/trip/publish/index' })
  }
})

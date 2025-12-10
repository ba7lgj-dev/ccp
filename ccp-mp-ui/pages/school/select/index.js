const schoolService = require('../../../services/school.js')
const cache = require('../../../utils/cache.js')

Page({
  data: {
    schools: [],
    filteredSchools: [],
    searchKeyword: ''
  },
  onLoad() {
    const cached = cache.getSchoolCache()
    if (cached && cached.length > 0) {
      this.setData({ schools: cached, filteredSchools: cached })
    } else {
      this.loadSchoolList()
    }
  },
  loadSchoolList() {
    schoolService.getSchoolList().then((list) => {
      const sorted = (list || []).slice().sort((a, b) => {
        return (a.schoolName || '').localeCompare(b.schoolName || '')
      })
      this.setData({ schools: sorted, filteredSchools: sorted })
      cache.setSchoolCache(sorted, 10 * 60 * 1000)
    }).catch(() => {
      wx.showToast({ title: '加载学校列表失败，请稍后重试', icon: 'none' })
    })
  },
  onSearchInput(e) {
    const keyword = e.detail.value || ''
    this.setData({ searchKeyword: keyword })
    if (!keyword) {
      this.setData({ filteredSchools: this.data.schools })
      return
    }
    const filtered = this.data.schools.filter((item) => {
      return item.schoolName && item.schoolName.indexOf(keyword) !== -1
    })
    this.setData({ filteredSchools: filtered })
  },
  onSelectSchool(e) {
    const id = e.currentTarget.dataset.id
    const target = this.data.schools.find(item => item.id === id)
    if (!target) {
      wx.showToast({ title: '未找到学校信息', icon: 'none' })
      return
    }
    const previousCampus = wx.getStorageSync('selectedCampus')
    wx.setStorageSync('selectedSchool', target)
    wx.removeStorageSync('selectedCampus')
    if (previousCampus && previousCampus.id) {
      wx.removeStorageSync('gateCache_' + previousCampus.id)
    }
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.selectedSchool = target
      app.globalData.selectedCampus = null
    }
    wx.redirectTo({ url: '/pages/campus/select/index' })
  }
})

const schoolService = require('../../../services/school.js')
const cache = require('../../../utils/cache.js')

Page({
  data: {
    schools: [],
    filteredSchools: [],
    searchKeyword: '',
    empty: false
  },
  async onShow() {
    const app = getApp()
    if (app && typeof app.checkAuthChain === 'function') {
      const ok = await app.checkAuthChain({ from: 'schoolSelect', allowAuthPages: true })
      if (!ok) return
    }
    this.loadApprovedSchools()
  },
  loadApprovedSchools() {
    schoolService.getApprovedSchoolList().then((list) => {
      const approvedList = Array.isArray(list) ? list : []
      const sorted = approvedList.slice().sort((a, b) => (a.schoolName || '').localeCompare(b.schoolName || ''))
      this.setData({
        schools: sorted,
        filteredSchools: sorted,
        empty: sorted.length === 0
      })
      cache.setSchoolCache(sorted, 10 * 60 * 1000)
    }).catch(() => {
      const cached = cache.getSchoolCache()
      if (cached && cached.length) {
        this.setData({ schools: cached, filteredSchools: cached })
        return
      }
      this.setData({ empty: true })
      wx.showToast({ title: '暂无学校认证记录', icon: 'none' })
    })
  },
  onSearchInput(e) {
    const keyword = (e.detail.value || '').trim()
    this.setData({ searchKeyword: keyword })
    if (!keyword) {
      this.setData({ filteredSchools: this.data.schools })
      return
    }
    const filtered = this.data.schools.filter((item) => {
      return (item.schoolName && item.schoolName.indexOf(keyword) !== -1) || (item.shortName && item.shortName.indexOf(keyword) !== -1)
    })
    this.setData({ filteredSchools: filtered })
  },
  onSelectSchool(e) {
    const id = e.currentTarget.dataset.id
    const target = this.data.schools.find(item => item.schoolId === id || item.id === id)
    if (!target) {
      wx.showToast({ title: '未找到学校信息', icon: 'none' })
      return
    }
    const schoolInfo = {
      id: target.schoolId || target.id,
      schoolName: target.schoolName,
      shortName: target.shortName,
      cityName: target.cityName
    }
    const previousCampus = wx.getStorageSync('selectedCampus')
    wx.setStorageSync('selectedSchool', schoolInfo)
    wx.removeStorageSync('selectedCampus')
    if (previousCampus && previousCampus.id) {
      wx.removeStorageSync('gateCache_' + previousCampus.id)
    }
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.school = schoolInfo
      app.globalData.campus = null
    }
    wx.redirectTo({ url: '/pages/campus/select/index' })
  },
  onGoAuth() {
    wx.reLaunch({ url: '/pages/verify/school/index' })
  }
})

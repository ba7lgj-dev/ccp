const auth = require('../../utils/auth.js')
const userService = require('../../services/user.js')
const { buildImageUrl } = require('../../utils/url.js')

function normalizeUserInfo(userInfo) {
  if (!userInfo || typeof userInfo !== 'object') {
    return {}
  }
  return { ...userInfo, avatarUrl: buildImageUrl(userInfo.avatarUrl) }
}

Page({
  data: {
    userInfo: {},
    schoolName: '',
    campusName: '',
    loading: false,
    hasToken: false
  },
  onShow() {
    const token = wx.getStorageSync('token') || ''
    const storedUser = normalizeUserInfo(wx.getStorageSync('userInfo') || {})
    if (!token) {
      this.handleNotLogin()
      return
    }
    const selectedSchool = wx.getStorageSync('selectedSchool') || null
    const selectedCampus = wx.getStorageSync('selectedCampus') || null
    this.setData({
      hasToken: true,
      userInfo: storedUser,
      schoolName: (selectedSchool && selectedSchool.schoolName) || '',
      campusName: (selectedCampus && selectedCampus.campusName) || ''
    })
    this.fetchProfile()
  },
  handleNotLogin() {
    this.setData({
      hasToken: false,
      userInfo: {},
      schoolName: '',
      campusName: ''
    })
    wx.reLaunch({ url: '/pages/login/index' })
  },
  fetchProfile() {
    this.setData({ loading: true })
    userService.getProfile().then((data) => {
      if (!data) {
        return
      }
      const profile = normalizeUserInfo(data)
      wx.setStorageSync('userInfo', profile)
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.userInfo = profile
      }
      const selectedSchool = wx.getStorageSync('selectedSchool') || null
      const selectedCampus = wx.getStorageSync('selectedCampus') || null
      this.setData({
        userInfo: profile,
        schoolName: profile.schoolName || (selectedSchool && selectedSchool.schoolName) || '',
        campusName: profile.campusName || (selectedCampus && selectedCampus.campusName) || ''
      })
    }).catch((err) => {
      if (err && err.code === 4001) {
        auth.clearToken()
        wx.clearStorageSync()
        wx.showToast({ title: '登录已失效，请重新登录', icon: 'none' })
        wx.reLaunch({ url: '/pages/login/index' })
        return
      }
      wx.showToast({ title: '加载个人信息失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
    })
  },
  goEditProfile() {
    wx.navigateTo({ url: '/pages/me/profile/edit/index' })
  },
  handleSchoolTap() {
    const selectedSchool = wx.getStorageSync('selectedSchool') || null
    const selectedCampus = wx.getStorageSync('selectedCampus') || null
    if (!selectedSchool) {
      wx.navigateTo({ url: '/pages/school/select/index' })
      return
    }
    if (!selectedCampus) {
      wx.navigateTo({ url: '/pages/campus/select/index' })
      return
    }
    wx.navigateTo({ url: '/pages/campus/select/index' })
  },
  goEmergencyContact() {
    wx.navigateTo({ url: '/pages/me/emergency-contact/index' })
  },
  goRoutes() {
    wx.navigateTo({ url: '/pages/me/routes/index' })
  },
  goOrders() {
    wx.navigateTo({ url: '/pages/order/list/index' })
  },
  onLogout() {
    wx.showModal({
      title: '退出登录',
      content: '确认要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          auth.clearToken()
          wx.clearStorageSync()
          const app = getApp()
          if (app && app.globalData) {
            app.globalData.token = null
            app.globalData.userInfo = null
            app.globalData.selectedSchool = null
            app.globalData.selectedCampus = null
          }
          this.setData({
            hasToken: false,
            userInfo: {},
            schoolName: '',
            campusName: ''
          })
          wx.reLaunch({ url: '/pages/login/index' })
        }
      }
    })
  }
})

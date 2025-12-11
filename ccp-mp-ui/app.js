const realAuthService = require('./services/realAuth.js')
const schoolAuthService = require('./services/schoolAuth.js')
const { buildImageUrl } = require('./utils/url.js')

App({
  onLaunch() {
    this.checkAuthChain({ from: 'appLaunch' }).catch(() => {})
  },
  async checkAuthChain(options = {}) {
    const token = wx.getStorageSync('token') || null
    const userInfo = wx.getStorageSync('userInfo') || null
    if (!token || !userInfo) {
      wx.redirectTo({ url: '/pages/login/index' })
      return Promise.reject(new Error('not login'))
    }
    const normalizedUser = { ...userInfo, avatarUrl: buildImageUrl(userInfo.avatarUrl) }
    this.globalData.token = token
    this.globalData.userInfo = normalizedUser
    wx.setStorageSync('userInfo', normalizedUser)
    try {
      const [realInfo, schoolAuthList] = await Promise.all([
        realAuthService.getInfo({ hideLoading: true }),
        schoolAuthService.listMine({ hideLoading: true })
      ])
      this.globalData.auth = {
        realAuthStatus: realInfo ? realInfo.realAuthStatus : null,
        schoolAuthList: schoolAuthList || []
      }
      if (!realInfo || realInfo.realAuthStatus !== 2) {
        wx.reLaunch({ url: '/pages/auth/realname/index' })
        return Promise.reject(new Error('real auth required'))
      }
      const approvedSchools = (schoolAuthList || []).filter(item => item.status === 2)
      if (!approvedSchools.length) {
        wx.reLaunch({ url: '/pages/auth/school/index' })
        return Promise.reject(new Error('school auth required'))
      }
      return { realInfo, schoolAuthList }
    } catch (err) {
      return Promise.reject(err)
    }
  },
  setGlobalUser(userInfo) {
    this.globalData.userInfo = userInfo
    if (userInfo) {
      this.globalData.token = wx.getStorageSync('token') || null
      wx.setStorageSync('userInfo', userInfo)
    }
  },
  globalData: {
    token: null,
    userInfo: null,
    selectedSchool: null,
    selectedCampus: null,
    auth: {
      realAuthStatus: null,
      schoolAuthList: []
    }
  }
})

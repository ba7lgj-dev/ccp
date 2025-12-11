const auth = require('./utils/auth.js')
const { buildImageUrl } = require('./utils/url.js')
const http = require('./utils/http.js')

App({
  onLaunch() {
    const token = wx.getStorageSync('token') || null
    const userInfo = wx.getStorageSync('userInfo') || null
    const normalizedUser = userInfo ? { ...userInfo, avatarUrl: buildImageUrl(userInfo.avatarUrl) } : null
    const school = wx.getStorageSync('selectedSchool') || null
    const campus = wx.getStorageSync('selectedCampus') || null
    this.globalData.token = token
    this.globalData.userInfo = normalizedUser
    this.globalData.school = school
    this.globalData.campus = campus
  },
  async checkAuthChain(options = {}) {
    const opts = options || {}
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo') || null
    const currentPages = getCurrentPages()
    const currentRoute = currentPages && currentPages.length ? currentPages[currentPages.length - 1].route : ''
    const isLoginPage = currentRoute === 'pages/login/index'
    const isRealAuthPage = currentRoute === 'pages/verify/realname/index' || currentRoute === 'pages/me/realAuth/index'
    const isSchoolAuthPage = currentRoute === 'pages/verify/school/index'

    if (!token) {
      wx.reLaunch({ url: '/pages/login/index' })
      return false
    }
    this.globalData.token = token
    if (userInfo) {
      this.globalData.userInfo = userInfo
    }
    if (isLoginPage && opts.allowAuthPages) {
      return true
    }

    const now = Date.now()
    const needRefresh = opts.forceRefresh === true || !this.globalData.auth.lastAuthFetchTime || (now - this.globalData.auth.lastAuthFetchTime) > 30000
    if (needRefresh) {
      try {
        const [realAuthInfo, schoolAuthList] = await Promise.all([
          http.get('/mp/user/realAuth/info', {}, { hideLoading: true }),
          http.get('/mp/user/schoolAuth/listMine', {}, { hideLoading: true })
        ])
        const realStatus = realAuthInfo && typeof realAuthInfo.realAuthStatus === 'number' ? realAuthInfo.realAuthStatus : 0
        this.globalData.auth.realAuthStatus = realStatus
        this.globalData.auth.schoolAuthList = Array.isArray(schoolAuthList) ? schoolAuthList : []
        this.globalData.auth.hasApprovedSchool = this.globalData.auth.schoolAuthList.some((item) => item.status === 2)
        this.globalData.auth.lastAuthFetchTime = now
        this.globalData.userInfo = Object.assign({}, this.globalData.userInfo || {}, { realAuthStatus: realStatus, realName: realAuthInfo && realAuthInfo.realName })
        wx.setStorageSync('userInfo', this.globalData.userInfo)
      } catch (err) {
        if (err && err.code === 4001) {
          auth.clearToken()
          wx.reLaunch({ url: '/pages/login/index' })
          return false
        }
      }
    }

    if (this.globalData.auth.realAuthStatus !== 2) {
      if (isRealAuthPage && opts.allowAuthPages) {
        return true
      }
      wx.reLaunch({ url: '/pages/verify/realname/index' })
      this.globalData.authRedirectFrom = opts.from || ''
      return false
    }
    if (!this.globalData.auth.hasApprovedSchool) {
      if (isSchoolAuthPage && opts.allowAuthPages) {
        return true
      }
      wx.reLaunch({ url: '/pages/verify/school/index' })
      return false
    }
    return true
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
    auth: {
      realAuthStatus: 0,
      schoolAuthList: [],
      hasApprovedSchool: false,
      lastAuthFetchTime: 0
    },
    authRedirectFrom: '',
    school: null,
    campus: null,
    currentTrip: null,
    loadingCount: 0,
    indexCache: {}
  }
})

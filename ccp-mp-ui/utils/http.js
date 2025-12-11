const auth = require('./auth.js')

const { BASE_URL } = require('./config.js')

function withBaseUrl(url = '') {
  if (url.indexOf('http://') === 0 || url.indexOf('https://') === 0) {
    return url
  }
  return `${BASE_URL}${url}`
}

function request(options) {
  const opts = options || {}
  const token = wx.getStorageSync('token')
  const hideLoading = opts.hideLoading === true
  const method = opts.method || 'GET'
  const data = opts.data || {}
  const url = withBaseUrl(opts.url)
  const headers = {
    'Content-Type': 'application/json'
  }
  if (token) {
    headers.Authorization = 'Bearer ' + token
  }
  if (!hideLoading) {
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.loadingCount = (app.globalData.loadingCount || 0) + 1
    }
    wx.showLoading({ title: '加载中', mask: true })
  }
  return new Promise((resolve, reject) => {
    wx.request({
      url,
      method,
      data,
      timeout: 8000,
      header: headers,
      success: (res) => {
        if (res.statusCode !== 200) {
          wx.showToast({ title: '网络异常，请稍后重试', icon: 'none' })
          reject(new Error('network error'))
          return
        }
        const body = res.data || {}
        if (body.code === 0) {
          resolve(body.data)
          return
        }
        if (body.code === 4001) {
          auth.clearToken()
          wx.showToast({ title: '登录失效，请重新登录', icon: 'none' })
          wx.reLaunch({ url: '/pages/login/index' })
          const error = new Error('token invalid')
          error.code = body.code
          reject(error)
          return
        }
        wx.showToast({ title: body.msg || '请求失败', icon: 'none' })
        const error = new Error(body.msg || 'request error')
        error.code = body.code
        reject(error)
      },
      fail: (err) => {
        wx.showToast({ title: '网络异常，请稍后重试', icon: 'none' })
        reject(err)
      },
      complete: () => {
        if (!hideLoading) {
          const app = getApp()
          if (app && app.globalData) {
            app.globalData.loadingCount = Math.max(0, (app.globalData.loadingCount || 1) - 1)
          }
          wx.hideLoading()
        }
      }
    })
  })
}

function get(url, data = {}, options = {}) {
  return request({ url, data, method: 'GET', ...options })
}

function post(url, data = {}, options = {}) {
  return request({ url, data, method: 'POST', ...options })
}

module.exports = { request, get, post, BASE_URL }

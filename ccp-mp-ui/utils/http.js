const auth = require('./auth.js')

// const BASE_URL = 'http://localhost:8080/mp'
const BASE_URL = 'http://hmj123.fucku.top/mp'
function request(options) {
  const opts = options || {}
  const token = wx.getStorageSync('token')
  const hideLoading = opts.hideLoading === true
  const method = opts.method || 'GET'
  const data = opts.data || {}
  const url = BASE_URL + opts.url
  const headers = {
    'Content-Type': 'application/json'
  }
  if (token) {
    headers.Authorization = 'Bearer ' + token
  }
  if (!hideLoading) {
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
          auth.reLogin().then(() => {
            const retryOptions = Object.assign({}, opts, { hideLoading: false })
            request(retryOptions).then(resolve).catch(reject)
          }).catch(() => {
            wx.showToast({ title: '登录失效，请重试', icon: 'none' })
            reject(new Error('token invalid'))
          })
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
          wx.hideLoading()
        }
      }
    })
  })
}

module.exports = { request }

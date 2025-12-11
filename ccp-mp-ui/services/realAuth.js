const { request, BASE_URL } = require('../utils/http.js')

function getRealAuthInfo() {
  return request({ url: '/mp/user/realAuth/info', method: 'GET', hideLoading: true })
}

function applyRealAuth(data) {
  return request({ url: '/mp/user/realAuth/apply', method: 'POST', data })
}

function uploadRealAuthImage(filePath) {
  const token = wx.getStorageSync('token') || ''
  return new Promise((resolve, reject) => {
    wx.uploadFile({
      url: BASE_URL + '/mp/upload/realAuthImage',
      filePath,
      name: 'file',
      header: token ? { Authorization: 'Bearer ' + token } : {},
      success: (res) => {
        if (res.statusCode !== 200) {
          reject(new Error('上传失败'))
          return
        }
        let body = {}
        try {
          body = JSON.parse(res.data || '{}')
        } catch (e) {
          body = res.data || {}
        }
        if (body.code === 0) {
          resolve(body.data || {})
        } else {
          const error = new Error(body.msg || '上传失败')
          error.code = body.code
          reject(error)
        }
      },
      fail: (err) => {
        reject(err)
      }
    })
  })
}

module.exports = { getRealAuthInfo, applyRealAuth, uploadRealAuthImage }

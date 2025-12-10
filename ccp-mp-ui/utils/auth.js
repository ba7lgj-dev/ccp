const { request } = require('./http.js')

function setToken(token) {
  wx.setStorageSync('token', token)
}

function getToken() {
  return wx.getStorageSync('token')
}

function clearToken() {
  wx.removeStorageSync('token')
  wx.removeStorageSync('userInfo')
}

function login() {
  return new Promise((resolve, reject) => {
    wx.login({
      success: (res) => {
        const jsCode = res.code
        if (!jsCode) {
          wx.showToast({ title: '微信登录失败', icon: 'none' })
          reject(new Error('no jsCode'))
          return
        }
        const authService = require('../services/auth.js')
        authService.login(jsCode).then((data) => {
          if (!data || !data.token) {
            wx.showToast({ title: '登录失败', icon: 'none' })
            reject(new Error('invalid login response'))
            return
          }
          setToken(data.token)
          const userInfo = data.userInfo || {}
          wx.setStorageSync('userInfo', userInfo)
          const app = getApp()
          if (app && app.globalData) {
            app.globalData.token = data.token
            app.globalData.userInfo = userInfo
          }
          resolve(userInfo)
        }).catch((err) => {
          wx.showToast({ title: '登录失败', icon: 'none' })
          reject(err)
        })
      },
      fail: (err) => {
        wx.showToast({ title: '微信登录失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

function reLogin() {
  clearToken()
  return login()
}

function bindPhone(encryptedData, iv) {
  // Phone binding is currently disabled.
  return Promise.resolve()
}

module.exports = { setToken, getToken, clearToken, login, reLogin, bindPhone }

const { request } = require('../utils/http.js')

function getProfile() {
  return request({ url: '/user/profile', method: 'GET', hideLoading: true })
}

function updateProfile(data) {
  return request({ url: '/user/profile', method: 'PUT', data })
}

function updateAvatar(data) {
  return request({ url: '/user/avatar', method: 'PUT', data, hideLoading: true })
}

module.exports = { getProfile, updateProfile, updateAvatar }

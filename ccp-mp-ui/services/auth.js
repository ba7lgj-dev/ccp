const { request } = require('../utils/http.js')

function login(jsCode) {
  return request({
    url: '/auth/wxLogin',
    method: 'POST',
    data: { jsCode }
  })
}

function bindPhone(encryptedData, iv) {
  return request({
    url: '/auth/wxPhoneBind',
    method: 'POST',
    data: { encryptedData, iv }
  })
}

module.exports = { login, bindPhone }

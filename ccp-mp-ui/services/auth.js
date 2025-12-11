const { request } = require('../utils/http.js')

function login(jsCode) {
  return request({
    url: '/mp/login',
    method: 'POST',
    data: { jsCode }
  })
}

function bindPhone(encryptedData, iv) {
  // Phone binding is disabled; keep a resolved promise for compatibility.
  return Promise.resolve()
}

module.exports = { login, bindPhone }

const { request } = require('../utils/http.js')

function getInfo(options = {}) {
  return request({ url: '/mp/user/realAuth/info', method: 'GET', hideLoading: options.hideLoading === true })
}

function apply(data) {
  return request({ url: '/mp/user/realAuth/apply', method: 'POST', data })
}

module.exports = { getInfo, apply }

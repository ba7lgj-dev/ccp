const { request } = require('../utils/http.js')

function listMine(options = {}) {
  return request({ url: '/mp/user/schoolAuth/listMine', method: 'GET', hideLoading: options.hideLoading === true })
}

function detail(schoolId) {
  return request({ url: '/mp/user/schoolAuth/detail', method: 'GET', data: { schoolId } })
}

function apply(data) {
  return request({ url: '/mp/user/schoolAuth/apply', method: 'POST', data })
}

function listApproved(options = {}) {
  return request({ url: '/mp/user/schoolAuth/listApproved', method: 'GET', hideLoading: options.hideLoading === true })
}

module.exports = { listMine, detail, apply, listApproved }

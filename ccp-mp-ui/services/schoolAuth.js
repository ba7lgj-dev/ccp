const http = require('../utils/http.js')

function listMine() {
  return http.request({ url: '/mp/user/schoolAuth/listMine', method: 'GET' })
}

function listApproved() {
  return http.request({ url: '/mp/user/schoolAuth/listApproved', method: 'GET' })
}

function getDetail(schoolId) {
  return http.request({ url: '/mp/user/schoolAuth/detail', method: 'GET', data: { schoolId } })
}

function apply(data) {
  return http.request({ url: '/mp/user/schoolAuth/apply', method: 'POST', data })
}

module.exports = {
  listMine,
  listApproved,
  getDetail,
  apply
}

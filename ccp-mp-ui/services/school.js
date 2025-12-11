const { request } = require('../utils/http.js')

function getSchoolList() {
  return request({
    url: '/mp/school/list',
    method: 'GET'
  })
}

function getApprovedSchoolList() {
  return request({
    url: '/mp/user/schoolAuth/listApproved',
    method: 'GET'
  })
}

module.exports = { getSchoolList, getApprovedSchoolList }

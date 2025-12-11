const { request } = require('../utils/http.js')

function getSchoolList() {
  return request({
    url: '/mp/school/list',
    method: 'GET'
  })
}

module.exports = { getSchoolList }

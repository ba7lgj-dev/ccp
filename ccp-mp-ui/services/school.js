const { request } = require('../utils/http.js')

function getSchoolList() {
  return request({
    url: '/school/list',
    method: 'GET'
  })
}

module.exports = { getSchoolList }

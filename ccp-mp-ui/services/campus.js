const { request } = require('../utils/http.js')

function getCampusList(schoolId) {
  return request({
    url: `/campus/listBySchool`,
    method: 'GET',
    data: { schoolId }
  })
}

module.exports = { getCampusList }

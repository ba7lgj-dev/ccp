const { request } = require('../utils/http.js')

function getCampusList(schoolId) {
  return request({
    url: `/mp/campus/listBySchool`,
    method: 'GET',
    data: { schoolId }
  })
}

module.exports = { getCampusList }

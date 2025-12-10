const { request } = require('../utils/http.js')

function getGateList(campusId) {
  return request({
    url: `/gate/listByCampus`,
    method: 'GET',
    data: { campusId }
  })
}

module.exports = { getGateList }

const { request } = require('../utils/http.js')

function getLocationList(campusId) {
  return request({
    url: `/mp/location/listByCampus`,
    method: 'GET',
    data: { campusId }
  })
}

module.exports = { getLocationList }

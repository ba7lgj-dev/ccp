const { request } = require('../utils/http.js')

function publishTrip(data) {
  return request({
    url: `/trip/publish`,
    method: 'POST',
    data
  })
}

function getTripHall(campusId) {
  return request({
    url: `/trip/hall`,
    method: 'GET',
    data: { campusId }
  })
}

module.exports = { publishTrip, getTripHall }

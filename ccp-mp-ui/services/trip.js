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

function getTripDetail(tripId) {
  return request({
    url: `/trip/detail`,
    method: 'GET',
    data: { tripId }
  })
}

function joinTrip(tripId, joinPeopleCount) {
  return request({
    url: `/trip/join`,
    method: 'POST',
    data: { id: tripId, joinPeopleCount }
  })
}

function quitTrip(tripId) {
  return request({
    url: `/trip/quit`,
    method: 'POST',
    data: { id: tripId }
  })
}

function kickMember(tripId, targetUserId) {
  return request({
    url: `/trip/kick`,
    method: 'POST',
    data: { id: tripId, targetUserId }
  })
}

function getMyActiveTrip() {
  return request({
    url: `/trip/myActive`,
    method: 'GET'
  })
}

module.exports = { publishTrip, getTripHall, getTripDetail, joinTrip, quitTrip, kickMember, getMyActiveTrip }

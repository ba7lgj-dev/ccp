const { request } = require('../utils/http.js')

function publishTrip(data) {
  return request({
    url: `/mp/trip/publish`,
    method: 'POST',
    data
  })
}

function getTripHall(campusId) {
  return request({
    url: `/mp/trip/hall`,
    method: 'GET',
    data: { campusId }
  })
}

function getTripDetail(tripId) {
  return request({
    url: `/mp/trip/detail`,
    method: 'GET',
    data: { tripId }
  })
}

function joinTrip(tripId, joinPeopleCount) {
  return request({
    url: `/mp/trip/join`,
    method: 'POST',
    data: { id: tripId, joinPeopleCount }
  })
}

function quitTrip(tripId) {
  return request({
    url: `/mp/trip/quit`,
    method: 'POST',
    data: { id: tripId }
  })
}

function kickMember(tripId, targetUserId) {
  return request({
    url: `/mp/trip/kick`,
    method: 'POST',
    data: { id: tripId, targetUserId }
  })
}

function getMyActiveTrip() {
  return request({
    url: `/mp/trip/myActive`,
    method: 'GET'
  })
}

function getMyHistoryTrips() {
  return request({
    url: `/mp/trip/myHistory`,
    method: 'GET',
    hideLoading: true
  })
}

module.exports = { publishTrip, getTripHall, getTripDetail, joinTrip, quitTrip, kickMember, getMyActiveTrip, getMyHistoryTrips }

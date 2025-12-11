import request from '@/utils/request'

export function listTrip(query) {
  return request({
    url: '/ccp/trip/trip/list',
    method: 'get',
    params: query
  })
}

export function getTrip(id) {
  return request({
    url: '/ccp/trip/trip/' + id,
    method: 'get'
  })
}

export function changeTripStatus(data) {
  return request({
    url: '/ccp/trip/trip/changeStatus',
    method: 'post',
    data: data
  })
}

export function exportTrip(query) {
  return request({
    url: '/ccp/trip/trip/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

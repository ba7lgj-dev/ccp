import request from '@/utils/request'

export function listTripMember(query) {
  return request({
    url: '/ccp/trip/member/list',
    method: 'get',
    params: query
  })
}

export function markNoShow(data) {
  return request({
    url: '/ccp/trip/member/markNoShow',
    method: 'post',
    data: data
  })
}

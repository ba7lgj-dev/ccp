import request from '@/utils/request'

// 查询实名认证列表
export function listUserRealAuth(query) {
  return request({
    url: '/ccp/userRealAuth/list',
    method: 'get',
    params: query
  })
}

// 查询实名认证详情
export function getUserRealAuth(userId) {
  return request({
    url: `/ccp/userRealAuth/${userId}`,
    method: 'get'
  })
}

// 审核通过
export function approveUserRealAuth(data) {
  return request({
    url: '/ccp/userRealAuth/approve',
    method: 'post',
    data: data
  })
}

// 审核拒绝
export function rejectUserRealAuth(data) {
  return request({
    url: '/ccp/userRealAuth/reject',
    method: 'post',
    data: data
  })
}

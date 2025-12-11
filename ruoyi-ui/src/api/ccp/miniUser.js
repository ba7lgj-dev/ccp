import request from '@/utils/request'

// 查询小程序用户列表
export function listMiniUser(query) {
  return request({
    url: '/ccp/miniUser/list',
    method: 'get',
    params: query
  })
}

// 查询用户详情
export function getMiniUser(id) {
  return request({
    url: '/ccp/miniUser/' + id,
    method: 'get'
  })
}

// 更新用户信息
export function updateMiniUser(data) {
  return request({
    url: '/ccp/miniUser',
    method: 'put',
    data: data
  })
}

// 审核通过
export function approveRealAuth(data) {
  return request({
    url: '/ccp/miniUser/realAuth/approve',
    method: 'post',
    data: data
  })
}

// 审核拒绝
export function rejectRealAuth(data) {
  return request({
    url: '/ccp/miniUser/realAuth/reject',
    method: 'post',
    data: data
  })
}

// 导出用户
export function exportMiniUser(query) {
  return request({
    url: '/ccp/miniUser/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

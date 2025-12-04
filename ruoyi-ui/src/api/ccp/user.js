import request from '@/utils/request'

// 查询小程序用户列表
export function listUser(query) {
  return request({
    url: '/ccp/user/list',
    method: 'get',
    params: query
  })
}

// 查询用户详情
export function getUser(id) {
  return request({
    url: '/ccp/user/' + id,
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/ccp/user',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/ccp/user',
    method: 'put',
    data: data
  })
}

// 删除用户
export function delUser(id) {
  return request({
    url: '/ccp/user/' + id,
    method: 'delete'
  })
}

// 修改账号状态
export function changeUserStatus(data) {
  return request({
    url: '/ccp/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 审核用户
export function reviewUser(data) {
  return request({
    url: '/ccp/user/review',
    method: 'put',
    data: data
  })
}

// 导出用户
export function exportUser(query) {
  return request({
    url: '/ccp/user/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

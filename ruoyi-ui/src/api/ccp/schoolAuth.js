import request from '@/utils/request'

// 查询学校学生认证列表
export function listSchoolAuth(query) {
  return request({
    url: '/ccp/schoolAuth/list',
    method: 'get',
    params: query
  })
}

// 获取详情
export function getSchoolAuth(id) {
  return request({
    url: '/ccp/schoolAuth/' + id,
    method: 'get'
  })
}

// 审核通过
export function approveSchoolAuth(data) {
  return request({
    url: '/ccp/schoolAuth/approve',
    method: 'post',
    data: data
  })
}

// 审核拒绝
export function rejectSchoolAuth(data) {
  return request({
    url: '/ccp/schoolAuth/reject',
    method: 'post',
    data: data
  })
}

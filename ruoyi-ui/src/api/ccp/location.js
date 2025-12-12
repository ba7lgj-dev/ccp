import request from '@/utils/request'

// 查询地点列表
export function listLocation(query) {
  return request({
    url: '/ccp/location/list',
    method: 'get',
    params: query
  })
}

// 查询地点详情
export function getLocation(id) {
  return request({
    url: '/ccp/location/' + id,
    method: 'get'
  })
}

// 新增地点
export function addLocation(data) {
  return request({
    url: '/ccp/location',
    method: 'post',
    data: data
  })
}

// 修改地点
export function updateLocation(data) {
  return request({
    url: '/ccp/location',
    method: 'put',
    data: data
  })
}

// 删除地点
export function delLocation(ids) {
  return request({
    url: '/ccp/location/' + ids,
    method: 'delete'
  })
}

// 修改状态
export function changeLocationStatus(data) {
  return request({
    url: '/ccp/location/changeStatus',
    method: 'put',
    data: data
  })
}

// 导出地点
export function exportLocation(query) {
  return request({
    url: '/ccp/location/export',
    method: 'post',
    params: query,
    responseType: 'blob'
  })
}

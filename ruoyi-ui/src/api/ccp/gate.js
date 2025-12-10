import request from '@/utils/request'

// 查询校门列表
export function listGate(query) {
  return request({
    url: '/gate/list',
    method: 'get',
    params: query
  })
}

// 查询校门详情
export function getGate(id) {
  return request({
    url: '/gate/' + id,
    method: 'get'
  })
}

// 新增校门
export function addGate(data) {
  return request({
    url: '/gate',
    method: 'post',
    data: data
  })
}

// 修改校门
export function updateGate(data) {
  return request({
    url: '/gate',
    method: 'put',
    data: data
  })
}

// 删除校门
export function delGate(ids) {
  return request({
    url: '/gate/' + ids,
    method: 'delete'
  })
}

// 修改校门状态
export function changeGateStatus(data) {
  return request({
    url: '/gate/status',
    method: 'put',
    data: data
  })
}

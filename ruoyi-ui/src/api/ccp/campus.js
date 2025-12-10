import request from '@/utils/request'

// 查询校区列表
export function listCampus(query) {
  return request({
    url: '/admin/campus/list',
    method: 'get',
    params: query
  })
}

// 查询校区详情
export function getCampus(id) {
  return request({
    url: '/admin/campus/' + id,
    method: 'get'
  })
}

// 新增校区
export function addCampus(data) {
  return request({
    url: '/admin/campus',
    method: 'post',
    data: data
  })
}

// 修改校区
export function updateCampus(data) {
  return request({
    url: '/admin/campus',
    method: 'put',
    data: data
  })
}

// 删除校区
export function delCampus(ids) {
  return request({
    url: '/admin/campus/' + ids,
    method: 'delete'
  })
}

// 修改校区状态
export function changeCampusStatus(data) {
  return request({
    url: '/admin/campus/status',
    method: 'put',
    data: data
  })
}

import request from '@/utils/request'

// 查询学校列表
export function listSchool(query) {
  return request({
    url: '/school/list',
    method: 'get',
    params: query
  })
}

// 查询学校详情
export function getSchool(id) {
  return request({
    url: '/school/' + id,
    method: 'get'
  })
}

// 新增学校
export function addSchool(data) {
  return request({
    url: '/school',
    method: 'post',
    data: data
  })
}

// 修改学校
export function updateSchool(data) {
  return request({
    url: '/school',
    method: 'put',
    data: data
  })
}

// 删除学校
export function delSchool(ids) {
  return request({
    url: '/school/' + ids,
    method: 'delete'
  })
}

// 修改学校状态
export function changeSchoolStatus(data) {
  return request({
    url: '/school/status',
    method: 'put',
    data: data
  })
}

const { request } = require('../utils/http.js')

function getMessageList(params, options = {}) {
  const { tripId, lastId, lastMessageId, pageSize } = typeof params === 'object' ? params : { tripId: params, lastId: arguments[1], pageSize: arguments[2] }
  const data = {}
  if (tripId !== undefined && tripId !== null) data.tripId = tripId
  if (lastId) data.lastId = lastId
  if (lastMessageId) data.lastMessageId = lastMessageId
  if (pageSize) data.pageSize = pageSize
  return request({
    url: '/mp/trip/chat/list',
    method: 'GET',
    data,
    hideLoading: options.hideLoading !== undefined ? options.hideLoading : true
  })
}

function sendMessage(tripId, content) {
  return request({
    url: '/mp/trip/chat/send',
    method: 'POST',
    data: { tripId, content }
  })
}

function markRead(tripId, lastChatId) {
  return request({
    url: '/mp/trip/chat/markRead',
    method: 'POST',
    data: { tripId, lastChatId },
    hideLoading: true
  })
}

function getUnreadSummary() {
  return request({
    url: '/mp/trip/chat/unreadSummary',
    method: 'GET',
    hideLoading: true
  })
}

module.exports = { getMessageList, sendMessage, markRead, getUnreadSummary }

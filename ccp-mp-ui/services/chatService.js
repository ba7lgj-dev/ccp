const { request } = require('../utils/http.js')

function getMessageList(params) {
  const { tripId, lastId, lastMessageId, pageSize } = typeof params === 'object' ? params : { tripId: params, lastId: arguments[1], pageSize: arguments[2] }
  return request({
    url: '/mp/trip/chat/list',
    method: 'GET',
    data: { tripId, lastId, lastMessageId, pageSize }
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

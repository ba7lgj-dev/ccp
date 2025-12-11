const chatService = require('../../../services/chatService.js')
const tripService = require('../../../services/trip.js')
const { buildImageUrl } = require('../../../utils/url.js')

const FIVE_MINUTES = 5 * 60 * 1000

Page({
  data: {
    loading: false,
    tripId: null,
    tripInfo: {
      startAddress: '',
      endAddress: '',
      departureTime: '',
      campusName: ''
    },
    messages: [],
    pageSize: 20,
    lastId: null,
    hasMore: true,
    scrollIntoView: '',
    inputValue: '',
    sending: false,
    firstLoad: true
  },
  onLoad(options) {
    const tripId = options && options.tripId ? Number(options.tripId) : null
    if (!tripId) {
      wx.showToast({ title: '参数错误', icon: 'none' })
      wx.navigateBack({ delta: 1 })
      return
    }
    this.setData({ tripId })
    const app = getApp()
    if (app && typeof app.checkAuthChain === 'function') {
      app.checkAuthChain({ from: 'tripChat' })
    }
    this.loadTripInfo()
    this.loadMessages(true)
  },
  onShow() {
    if (!this.data.firstLoad) {
      this.markAllRead()
    }
  },
  loadTripInfo() {
    tripService.getTripDetail(this.data.tripId).then((detail) => {
      if (!detail) return
      const info = {
        startAddress: detail.startAddress,
        endAddress: detail.endAddress,
        departureTime: detail.departureTimeDisplay || detail.departureTime,
        campusName: detail.campusName
      }
      this.setData({ tripInfo: info })
    }).catch(() => {})
  },
  loadMessages(initial = false) {
    if (this.data.loading) return
    if (!initial && !this.data.hasMore) return
    const prevFirstId = this.data.messages.length ? this.data.messages[0].id : null
    this.setData({ loading: true })
    const lastId = initial ? null : this.data.lastId
    chatService.getMessageList(this.data.tripId, lastId, this.data.pageSize).then((res) => {
      const list = Array.isArray(res && res.items) ? res.items : []
      const merged = initial ? list : list.concat(this.data.messages)
      const decorated = this.decorateMessages(merged)
      const latestId = decorated.length ? decorated[decorated.length - 1].id : null
      const oldestId = decorated.length ? decorated[0].id : null
      const scrollId = initial ? latestId : (prevFirstId || oldestId)
      this.setData({
        messages: decorated,
        lastId: oldestId,
        hasMore: !!(res && res.hasMore),
        scrollIntoView: scrollId ? `msg-${scrollId}` : ''
      }, () => {
        this.markAllRead()
      })
    }).catch(() => {
      wx.showToast({ title: '加载聊天失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false, firstLoad: false })
    })
  },
  decorateMessages(list) {
    const sorted = (list || []).slice().sort((a, b) => a.id - b.id)
    let lastTs = 0
    return sorted.map((item, index) => {
      const ts = this.parseTime(item.sendTime)
      const needDivider = index === 0 || (ts - lastTs) > FIVE_MINUTES
      lastTs = ts
      return Object.assign({}, item, {
        senderAvatarUrl: buildImageUrl(item.senderAvatarUrl),
        showDivider: needDivider,
        dividerText: this.formatDividerText(ts),
        timeText: this.formatClock(ts)
      })
    })
  },
  parseTime(timeStr) {
    if (!timeStr) return 0
    const ts = new Date(timeStr.replace(/-/g, '/')).getTime()
    return Number.isNaN(ts) ? 0 : ts
  },
  formatDividerText(ts) {
    if (!ts) return ''
    const date = new Date(ts)
    const now = new Date()
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime()
    const targetDay = new Date(date.getFullYear(), date.getMonth(), date.getDate()).getTime()
    const hh = `${date.getHours()}`.padStart(2, '0')
    const mm = `${date.getMinutes()}`.padStart(2, '0')
    if (targetDay === today) {
      return `今天 ${hh}:${mm}`
    }
    const yesterday = today - 24 * 60 * 60 * 1000
    if (targetDay === yesterday) {
      return `昨天 ${hh}:${mm}`
    }
    return `${date.getMonth() + 1}月${date.getDate()}日 ${hh}:${mm}`
  },
  formatClock(ts) {
    if (!ts) return ''
    const date = new Date(ts)
    const hh = `${date.getHours()}`.padStart(2, '0')
    const mm = `${date.getMinutes()}`.padStart(2, '0')
    return `${hh}:${mm}`
  },
  onReachTop() {
    this.loadMessages(false)
  },
  onInput(e) {
    this.setData({ inputValue: e.detail.value })
  },
  onSend() {
    if (this.data.sending || !this.data.inputValue) return
    this.setData({ sending: true })
    chatService.sendMessage(this.data.tripId, this.data.inputValue).then((msg) => {
      const normalized = Object.assign({}, msg, { senderAvatarUrl: buildImageUrl(msg.senderAvatarUrl) })
      const merged = this.decorateMessages(this.data.messages.concat([normalized]))
      const latestId = merged.length ? merged[merged.length - 1].id : null
      this.setData({
        messages: merged,
        inputValue: '',
        scrollIntoView: latestId ? `msg-${latestId}` : '',
        lastId: merged.length ? merged[0].id : null
      }, () => {
        this.markAllRead()
      })
    }).catch(() => {
      wx.showToast({ title: '发送失败，请重试', icon: 'none' })
    }).finally(() => {
      this.setData({ sending: false })
    })
  },
  markAllRead() {
    if (!this.data.messages.length) return
    const lastChatId = this.data.messages[this.data.messages.length - 1].id
    chatService.markRead(this.data.tripId, lastChatId).catch(() => {})
  }
})

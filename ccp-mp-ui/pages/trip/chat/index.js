const chatService = require('../../../services/chatService.js')
const tripService = require('../../../services/trip.js')
const { buildImageUrl } = require('../../../utils/url.js')

const FIVE_MINUTES = 5 * 60 * 1000
const POLL_INTERVAL = 3000

Page({
  data: {
    loading: true,
    tripId: null,
    tripStatus: null,
    tripInfo: {
      startAddress: '',
      endAddress: '',
      departureTime: '',
      campusName: ''
    },
    messages: [],
    pageSize: 20,
    lastMessageId: null,
    hasMore: true,
    scrollIntoView: '',
    inputContent: '',
    sending: false,
    pollTimer: null,
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
    this.initChat()
  },
  onShow() {
    if (!this.data.firstLoad) {
      this.markAllRead()
    }
  },
  onHide() {
    this.clearPoller()
  },
  onUnload() {
    this.clearPoller()
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
      this.setData({ tripInfo: info, tripStatus: detail.status })
    }).catch(() => {})
  },
  initChat() {
    this.loadMessages({ init: true })
    this.clearPoller()
    this.data.pollTimer = setInterval(() => {
      this.loadMessages({ incremental: true })
    }, POLL_INTERVAL)
  },
  clearPoller() {
    if (this.data.pollTimer) {
      clearInterval(this.data.pollTimer)
      this.setData({ pollTimer: null })
    }
  },
  loadMessages(options = {}) {
    const { init, incremental, loadMore } = options
    if (init && this.data.loading === false) {
      this.setData({ loading: true })
    }
    if (!init && !incremental && !this.data.hasMore) return
    const params = { tripId: this.data.tripId, pageSize: this.data.pageSize }
    if (incremental && this.data.lastMessageId) {
      params.lastMessageId = this.data.lastMessageId
    } else if (loadMore && this.data.messages.length) {
      params.lastId = this.data.messages[0].id
    }
    chatService.getMessageList(params).then((res) => {
      const list = Array.isArray(res && res.items) ? res.items : []
      const merged = incremental
        ? this.mergeMessages(this.data.messages.concat(list))
        : loadMore
          ? this.mergeMessages(list.concat(this.data.messages))
          : this.mergeMessages(list)
      const latestId = merged.length ? merged[merged.length - 1].id : null
      const scrollId = incremental ? latestId : (loadMore && this.data.messages.length ? this.data.messages[0].id : latestId)
      const hasMoreFlag = res && typeof res.hasMore === 'boolean' ? res.hasMore : this.data.hasMore
      this.setData({
        messages: merged,
        lastMessageId: latestId,
        hasMore: hasMoreFlag,
        scrollIntoView: scrollId ? `msg-${scrollId}` : ''
      }, () => this.markAllRead())
    }).catch(() => {
      if (init) {
        wx.showToast({ title: '加载聊天失败', icon: 'none' })
      }
    }).finally(() => {
      if (init) {
        this.setData({ loading: false, firstLoad: false })
      }
    })
  },
  mergeMessages(list) {
    const map = new Map()
    list.forEach((item) => {
      if (!item || item.id == null) return
      map.set(item.id, item)
    })
    const sorted = Array.from(map.values()).sort((a, b) => a.id - b.id)
    return this.decorateMessages(sorted)
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
    this.loadMessages({ loadMore: true })
  },
  onInput(e) {
    this.setData({ inputContent: e.detail.value })
  },
  onSend() {
    if (this.data.sending || !this.data.inputContent || this.isChatClosed()) return
    this.setData({ sending: true })
    chatService.sendMessage(this.data.tripId, this.data.inputContent).then(() => {
      this.setData({ inputContent: '' })
      this.loadMessages({ incremental: true })
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
  },
  isChatClosed() {
    const status = this.data.tripStatus
    return status === 3 || status === 4 || status === 5
  }
})

const tripService = require('../../../services/trip.js')
const { buildImageUrl } = require('../../../utils/url.js')

Page({
  data: {
    tripId: null,
    loading: false,
    detail: null,
    canJoin: false,
    canQuit: false,
    canKick: false,
    canChat: false
  },
  onLoad(options) {
    const tripId = options && options.tripId ? Number(options.tripId) : null
    this.setData({ tripId })
    if (tripId) {
      this.loadDetail()
    }
  },
  onShow() {
    if (this.data.tripId && !this.data.loading) {
      this.loadDetail()
    }
  },
  onPullDownRefresh() {
    this.loadDetail(true)
  },
  loadDetail(fromPullDown = false) {
    const tripId = this.data.tripId
    if (!tripId) return
    this.setData({ loading: true })
    tripService.getTripDetail(tripId).then((detail) => {
      if (!detail) {
        wx.showToast({ title: '订单不存在', icon: 'none' })
        return
      }
      if (detail.needRedirect) {
        wx.showToast({ title: '订单已结束', icon: 'none' })
        setTimeout(() => {
          const pages = getCurrentPages()
          if (pages.length > 1) {
            wx.navigateBack({ delta: 1 })
          } else {
            wx.switchTab({ url: '/pages/trip/hall/index' })
          }
        }, 400)
        return
      }
      const normalizedDetail = {
        ...detail,
        ownerAvatar: buildImageUrl(detail.ownerAvatar),
        members: (detail.members || []).map(item => ({
          ...item,
          avatarUrl: buildImageUrl(item.avatarUrl)
        }))
      }
      this.setData({
        detail: normalizedDetail,
        canJoin: detail.currentUserInfo && detail.currentUserInfo.canJoin,
        canQuit: detail.currentUserInfo && detail.currentUserInfo.canQuit,
        canKick: detail.currentUserInfo && detail.currentUserInfo.canKick,
        canChat: detail.currentUserInfo && detail.currentUserInfo.joined
      })
    }).catch(() => {
      wx.showToast({ title: '加载失败', icon: 'none' })
    }).finally(() => {
      this.setData({ loading: false })
      if (fromPullDown) {
        wx.stopPullDownRefresh()
      }
    })
  },
  onTapJoin() {
    wx.showModal({
      title: '确认加入',
      content: '确认加入该拼车吗？',
      success: (res) => {
        if (res.confirm) {
          tripService.joinTrip(this.data.tripId, 1).then(() => {
            wx.showToast({ title: '已加入', icon: 'success' })
            this.loadDetail()
          }).catch((err) => {
            if (err && err.code === 4002) {
              wx.showToast({ title: '你当前已在其他拼车中，不能加入新的拼车', icon: 'none' })
              return
            }
            wx.showToast({ title: err.message || '加入失败', icon: 'none' })
          })
        }
      }
    })
  },
  onTapQuit() {
    wx.showModal({
      title: '退出拼车',
      content: '确认退出当前拼车吗？',
      success: (res) => {
        if (res.confirm) {
          tripService.quitTrip(this.data.tripId).then(() => {
            wx.showToast({ title: '已退出', icon: 'success' })
            this.loadDetail()
          }).catch((err) => {
            wx.showToast({ title: err.message || '操作失败', icon: 'none' })
          })
        }
      }
    })
  },
  onTapKick(e) {
    const userId = e.currentTarget.dataset.userId
    if (!userId) return
    wx.showModal({
      title: '移除成员',
      content: '确定将该成员移出拼单吗？',
      success: (res) => {
        if (res.confirm) {
          tripService.kickMember(this.data.tripId, userId).then(() => {
            wx.showToast({ title: '已移除', icon: 'success' })
            this.loadDetail()
          }).catch((err) => {
            wx.showToast({ title: err.message || '操作失败', icon: 'none' })
          })
        }
      }
    })
  },
  onTapChat() {
    if (!this.data.canChat) {
      wx.showToast({ title: '加入拼单后才能进入聊天', icon: 'none' })
      return
    }
    wx.navigateTo({ url: `/pages/trip/chat/index?tripId=${this.data.tripId}` })
  },
  onTapBackHall() {
    wx.switchTab({ url: '/pages/trip/hall/index' })
  }
})

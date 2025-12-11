const auth = require('../../../../utils/auth.js')
const userService = require('../../../../services/user.js')
const { BASE_URL } = require('../../../../utils/http.js')

Page({
  data: {
    avatarUrl: '',
    nickName: '',
    gender: 0,
    realName: '',
    saving: false
  },
  onLoad() {
    const token = wx.getStorageSync('token') || ''
    if (!token) {
      wx.reLaunch({ url: '/pages/login/index' })
      return
    }
    const storedUser = wx.getStorageSync('userInfo') || {}
    if (!storedUser || !storedUser.id) {
      wx.reLaunch({ url: '/pages/login/index' })
      return
    }
    this.setData({
      avatarUrl: storedUser.avatarUrl || '',
      nickName: storedUser.nickName || '',
      gender: storedUser.gender != null ? storedUser.gender : 0,
      realName: storedUser.realName || ''
    })
  },
  onNickInput(e) {
    this.setData({ nickName: e.detail.value })
  },
  onGenderChange(e) {
    const value = Number(e.detail.value)
    this.setData({ gender: isNaN(value) ? 0 : value })
  },
  onRealNameInput(e) {
    this.setData({ realName: e.detail.value })
  },
  onChangeAvatar() {
    wx.showActionSheet({
      itemList: ['使用微信头像', '从相册选择'],
      success: (res) => {
        if (res.tapIndex === 0) {
          this.useWechatAvatar()
        } else if (res.tapIndex === 1) {
          this.chooseLocalAvatar()
        }
      }
    })
  },
  useWechatAvatar() {
    const handler = (res) => {
      const url = res.userInfo && res.userInfo.avatarUrl
      if (!url) {
        wx.showToast({ title: '获取微信头像失败', icon: 'none' })
        return
      }
      this.updateAvatarUrl(url)
    }
    if (wx.getUserProfile) {
      wx.getUserProfile({ desc: '用于完善资料', success: handler, fail: () => wx.showToast({ title: '获取头像失败', icon: 'none' }) })
    } else {
      wx.getUserInfo({ success: handler, fail: () => wx.showToast({ title: '获取头像失败', icon: 'none' }) })
    }
  },
  chooseLocalAvatar() {
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePaths = res.tempFilePaths || []
        if (!tempFilePaths.length) {
          return
        }
        this.uploadAvatarFile(tempFilePaths[0])
      }
    })
  },
  uploadAvatarFile(filePath) {
    const token = wx.getStorageSync('token') || ''
    wx.showLoading({ title: '上传中', mask: true })
    wx.uploadFile({
      url: `${BASE_URL}/upload/avatar`,
      filePath,
      name: 'file',
      header: token ? { Authorization: 'Bearer ' + token } : {},
      success: (res) => {
        try {
          const body = JSON.parse(res.data || '{}')
          if (body.code === 0 && body.data && body.data.url) {
            this.updateAvatarUrl(body.data.url)
          } else if (body.code === 4001) {
            auth.clearToken()
            wx.reLaunch({ url: '/pages/login/index' })
          } else {
            wx.showToast({ title: body.msg || '上传失败', icon: 'none' })
          }
        } catch (e) {
          wx.showToast({ title: '上传失败', icon: 'none' })
        }
      },
      fail: () => {
        wx.showToast({ title: '上传失败', icon: 'none' })
      },
      complete: () => {
        wx.hideLoading()
      }
    })
  },
  updateAvatarUrl(url) {
    userService.updateAvatar({ avatarUrl: url }).then((data) => {
      const profile = data || {}
      const newAvatar = profile.avatarUrl || url
      this.setData({ avatarUrl: newAvatar })
      wx.setStorageSync('userInfo', profile.id ? profile : { ...wx.getStorageSync('userInfo'), avatarUrl: newAvatar })
      const app = getApp()
      if (app && app.globalData) {
        app.globalData.userInfo = profile.id ? profile : { ...app.globalData.userInfo, avatarUrl: newAvatar }
      }
      wx.showToast({ title: '头像已更新', icon: 'none' })
    }).catch((err) => {
      if (err && err.code === 4001) {
        auth.clearToken()
        wx.reLaunch({ url: '/pages/login/index' })
        return
      }
      wx.showToast({ title: '更新失败，请稍后重试', icon: 'none' })
    })
  },
  onSave() {
    const nick = (this.data.nickName || '').trim()
    if (!nick) {
      wx.showToast({ title: '昵称不能为空', icon: 'none' })
      return
    }
    const payload = {
      nickName: nick,
      gender: this.data.gender,
      realName: (this.data.realName || '').trim()
    }
    this.setData({ saving: true })
    userService.updateProfile(payload).then((data) => {
      if (data) {
        wx.setStorageSync('userInfo', data)
        const app = getApp()
        if (app && app.globalData) {
          app.globalData.userInfo = data
        }
        this.setData({
          avatarUrl: data.avatarUrl || this.data.avatarUrl,
          nickName: data.nickName || nick,
          gender: data.gender != null ? data.gender : this.data.gender,
          realName: data.realName || ''
        })
      }
      wx.showToast({ title: '保存成功', icon: 'success' })
      wx.navigateBack()
    }).catch((err) => {
      if (err && err.code === 4001) {
        auth.clearToken()
        wx.reLaunch({ url: '/pages/login/index' })
        return
      }
      wx.showToast({ title: '保存失败，请稍后重试', icon: 'none' })
    }).finally(() => {
      this.setData({ saving: false })
    })
  },
  onCancel() {
    wx.navigateBack()
  }
})

const gateService = require('../../../services/gate.js')
const locationService = require('../../../services/location.js')
const tripService = require('../../../services/trip.js')
const cache = require('../../../utils/cache.js')

const OWNER_MAX = 6
const TOTAL_MAX = 10

Page({
  data: {
    selectedCampus: null,
    gateList: [],
    gateNames: [],
    locationList: [],
    locationNames: [],
    startType: 'gate',
    endType: 'gate',
    form: {
      startGateId: null,
      startGateName: '',
      startLocationId: null,
      startLocationName: '',
      startAddress: '',
      endGateId: null,
      endGateName: '',
      endLocationId: null,
      endLocationName: '',
      endAddress: '',
      departureType: 'immediate',
      departureDate: '',
      departureTime: '',
      departureDateTime: '',
      ownerPeopleCount: 1,
      totalPeople: 2,
      requireText: ''
    },
    ownerPeopleOptions: [],
    totalPeopleOptions: []
  },
  onLoad() {
    const campus = wx.getStorageSync('selectedCampus')
    if (!campus) {
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return
    }
    this.setData({ selectedCampus: campus })
    const app = getApp()
    if (app && app.globalData) {
      app.globalData.selectedCampus = campus
    }
    this.initPeopleOptions()
    this.initGateList(campus.id)
    this.initLocationList(campus.id)
  },
  initPeopleOptions() {
    const ownerPeopleOptions = Array.from({ length: OWNER_MAX }, (_, idx) => `${idx + 1}人`)
    const totalPeopleOptions = Array.from({ length: TOTAL_MAX - 1 }, (_, idx) => `${idx + 2}人`)
    this.setData({ ownerPeopleOptions, totalPeopleOptions })
  },
  initGateList(campusId) {
    const cached = cache.getGateCache(campusId)
    if (cached) {
      this.setGates(cached)
      return
    }
    gateService.getGateList(campusId).then((list) => {
      const sorted = (list || []).slice().sort((a, b) => {
        const sortA = typeof a.sort === 'number' ? a.sort : 0
        const sortB = typeof b.sort === 'number' ? b.sort : 0
        if (sortA === sortB) {
          return (a.id || 0) - (b.id || 0)
        }
        return sortA - sortB
      })
      this.setGates(sorted)
      cache.setGateCache(campusId, sorted, 10 * 60 * 1000)
    }).catch(() => {
      wx.showToast({ title: '加载校门失败', icon: 'none' })
    })
  },
  setGates(gateList) {
    const gateNames = (gateList || []).map(item => item.gateName)
    this.setData({ gateList, gateNames })
  },
  initLocationList(campusId) {
    const cached = cache.getLocationCache ? cache.getLocationCache(campusId) : null
    if (cached) {
      this.setLocations(cached)
      return
    }
    locationService.getLocationList(campusId).then((list) => {
      const sorted = (list || []).slice().sort((a, b) => {
        return (a.locationName || '').localeCompare(b.locationName || '')
      })
      this.setLocations(sorted)
      if (cache.setLocationCache) {
        cache.setLocationCache(campusId, sorted, 10 * 60 * 1000)
      }
    }).catch(() => {
      wx.showToast({ title: '加载地点失败', icon: 'none' })
    })
  },
  setLocations(locationList) {
    const locationNames = (locationList || []).map(item => item.locationName)
    this.setData({ locationList, locationNames })
  },
  onSelectUserRoute() {
    wx.showToast({ title: '功能开发中', icon: 'none' })
  },
  onSelectPresetRoute() {
    wx.showToast({ title: '功能开发中', icon: 'none' })
  },
  onStartTypeChange(e) {
    const type = e.detail.value
    this.setData({
      startType: type,
      'form.startGateId': null,
      'form.startGateName': '',
      'form.startLocationId': null,
      'form.startLocationName': '',
      'form.startAddress': ''
    })
  },
  onEndTypeChange(e) {
    const type = e.detail.value
    this.setData({
      endType: type,
      'form.endGateId': null,
      'form.endGateName': '',
      'form.endLocationId': null,
      'form.endLocationName': '',
      'form.endAddress': ''
    })
  },
  onStartGateChange(e) {
    const index = Number(e.detail.value)
    const gate = this.data.gateList[index]
    this.setData({
      'form.startGateId': gate ? gate.id : null,
      'form.startGateName': gate ? gate.gateName : '',
      'form.startLocationId': null,
      'form.startLocationName': '',
      'form.startAddress': ''
    })
  },
  onEndGateChange(e) {
    const index = Number(e.detail.value)
    const gate = this.data.gateList[index]
    this.setData({
      'form.endGateId': gate ? gate.id : null,
      'form.endGateName': gate ? gate.gateName : '',
      'form.endLocationId': null,
      'form.endLocationName': '',
      'form.endAddress': ''
    })
  },
  onStartLocationChange(e) {
    const index = Number(e.detail.value)
    const location = this.data.locationList[index]
    this.setData({
      'form.startLocationId': location ? location.id : null,
      'form.startLocationName': location ? location.locationName : '',
      'form.startGateId': null,
      'form.startGateName': '',
      'form.startAddress': ''
    })
  },
  onEndLocationChange(e) {
    const index = Number(e.detail.value)
    const location = this.data.locationList[index]
    this.setData({
      'form.endLocationId': location ? location.id : null,
      'form.endLocationName': location ? location.locationName : '',
      'form.endGateId': null,
      'form.endGateName': '',
      'form.endAddress': ''
    })
  },
  onStartAddressInput(e) {
    const value = e.detail.value || ''
    this.setData({
      'form.startAddress': value,
      'form.startGateId': null,
      'form.startLocationId': null,
      'form.startGateName': '',
      'form.startLocationName': ''
    })
  },
  onEndAddressInput(e) {
    const value = e.detail.value || ''
    this.setData({
      'form.endAddress': value,
      'form.endGateId': null,
      'form.endLocationId': null,
      'form.endGateName': '',
      'form.endLocationName': ''
    })
  },
  onDepartureTypeChange(e) {
    const departureType = e.detail.value
    const update = { departureType }
    if (departureType === 'immediate') {
      update.departureDate = ''
      update.departureTime = ''
      update.departureDateTime = ''
    }
    this.setData({ form: Object.assign({}, this.data.form, update) })
  },
  onDateChange(e) {
    const date = e.detail.value
    this.updateDepartureDateTime(date, this.data.form.departureTime)
  },
  onTimeChange(e) {
    const time = e.detail.value
    this.updateDepartureDateTime(this.data.form.departureDate, time)
  },
  updateDepartureDateTime(date, time) {
    const update = {
      departureDate: date || '',
      departureTime: time || ''
    }
    if (date && time) {
      update.departureDateTime = `${date} ${time}:00`
    }
    this.setData({ form: Object.assign({}, this.data.form, update) })
  },
  onOwnerPeopleChange(e) {
    const ownerPeopleCount = Number(e.detail.value) + 1
    let totalPeople = this.data.form.totalPeople
    if (totalPeople < ownerPeopleCount) {
      totalPeople = ownerPeopleCount
    }
    this.setData({
      'form.ownerPeopleCount': ownerPeopleCount,
      'form.totalPeople': totalPeople
    })
  },
  onTotalPeopleChange(e) {
    const totalPeople = Number(e.detail.value) + 2
    let ownerPeopleCount = this.data.form.ownerPeopleCount
    if (totalPeople < ownerPeopleCount) {
      wx.showToast({ title: '总人数需大于或等于我方人数', icon: 'none' })
      return
    }
    this.setData({
      'form.totalPeople': totalPeople,
      'form.ownerPeopleCount': ownerPeopleCount
    })
  },
  onRequireTextInput(e) {
    this.setData({ 'form.requireText': e.detail.value || '' })
  },
  validateForm() {
    const { selectedCampus, startType, endType, form } = this.data
    if (!selectedCampus) {
      wx.showToast({ title: '请先选择校区', icon: 'none' })
      wx.redirectTo({ url: '/pages/campus/select/index' })
      return false
    }
    if (startType === 'gate' && !form.startGateId) {
      wx.showToast({ title: '请选择起点校门', icon: 'none' })
      return false
    }
    if (startType === 'location' && !form.startLocationId) {
      wx.showToast({ title: '请选择起点地点', icon: 'none' })
      return false
    }
    if (startType === 'manual' && !form.startAddress) {
      wx.showToast({ title: '请输入起点地址', icon: 'none' })
      return false
    }
    if (endType === 'gate' && !form.endGateId) {
      wx.showToast({ title: '请选择终点校门', icon: 'none' })
      return false
    }
    if (endType === 'location' && !form.endLocationId) {
      wx.showToast({ title: '请选择终点地点', icon: 'none' })
      return false
    }
    if (endType === 'manual' && !form.endAddress) {
      wx.showToast({ title: '请输入终点地址', icon: 'none' })
      return false
    }
    if (form.ownerPeopleCount < 1 || form.ownerPeopleCount > OWNER_MAX) {
      wx.showToast({ title: '我方人数超出范围', icon: 'none' })
      return false
    }
    if (form.totalPeople < form.ownerPeopleCount || form.totalPeople > TOTAL_MAX) {
      wx.showToast({ title: '总人数需大于等于我方人数且不超过10', icon: 'none' })
      return false
    }
    if (form.departureType === 'reserve') {
      if (!form.departureDateTime) {
        wx.showToast({ title: '请选择预约时间', icon: 'none' })
        return false
      }
      const reserveTs = new Date(form.departureDateTime.replace(/-/g, '/')).getTime()
      if (Number.isNaN(reserveTs) || reserveTs < Date.now()) {
        wx.showToast({ title: '预约时间需晚于当前时间', icon: 'none' })
        return false
      }
    }
    return true
  },
  buildDepartureTime() {
    const { departureType, departureDateTime } = this.data.form
    if (departureType === 'immediate') {
      const now = new Date()
      const yyyy = now.getFullYear()
      const mm = `${now.getMonth() + 1}`.padStart(2, '0')
      const dd = `${now.getDate()}`.padStart(2, '0')
      const hh = `${now.getHours()}`.padStart(2, '0')
      const mi = `${now.getMinutes()}`.padStart(2, '0')
      const ss = `${now.getSeconds()}`.padStart(2, '0')
      return `${yyyy}-${mm}-${dd} ${hh}:${mi}:${ss}`
    }
    return departureDateTime
  },
  onSubmit() {
    if (!this.validateForm()) {
      return
    }
    const departureTime = this.buildDepartureTime()
    const { selectedCampus, form } = this.data
    const payload = {
      campusId: selectedCampus.id,
      startGateId: form.startGateId,
      startLocationId: form.startLocationId,
      startAddress: form.startAddress || form.startGateName || form.startLocationName,
      endGateId: form.endGateId,
      endLocationId: form.endLocationId,
      endAddress: form.endAddress || form.endGateName || form.endLocationName,
      ownerPeopleCount: form.ownerPeopleCount,
      totalPeople: form.totalPeople,
      departureTime,
      requireText: form.requireText || ''
    }
    tripService.publishTrip(payload).then(() => {
      wx.showToast({ title: '发布成功', icon: 'success' })
      setTimeout(() => {
        wx.redirectTo({ url: '/pages/trip/hall/index' })
      }, 400)
    }).catch((err) => {
      if (err && err.code === 4002) {
        wx.showToast({ title: '你当前已有进行中的拼车，请先完成或退出后再发起新的拼车', icon: 'none' })
        return
      }
      const msg = (err && err.message) || '发布失败'
      wx.showToast({ title: msg, icon: 'none' })
    })
  },
  onSwitchCampus() {
    wx.redirectTo({ url: '/pages/campus/select/index' })
  }
})

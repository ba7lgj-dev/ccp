function setSchoolCache(list, ttlMillis) {
  const expire = Date.now() + ttlMillis
  wx.setStorageSync('schoolCache', { data: list, expire })
}

function getSchoolCache() {
  const cache = wx.getStorageSync('schoolCache')
  if (!cache || !cache.data) {
    return null
  }
  if (Date.now() > cache.expire) {
    return null
  }
  return cache.data
}

function setCampusCache(schoolId, list, ttlMillis) {
  const expire = Date.now() + ttlMillis
  wx.setStorageSync('campusCache_' + schoolId, { data: list, expire })
}

function getCampusCache(schoolId) {
  const cache = wx.getStorageSync('campusCache_' + schoolId)
  if (!cache || !cache.data) {
    return null
  }
  if (Date.now() > cache.expire) {
    return null
  }
  return cache.data
}

function setGateCache(campusId, list, ttlMillis) {
  const expire = Date.now() + ttlMillis
  wx.setStorageSync('gateCache_' + campusId, { data: list, expire })
}

function getGateCache(campusId) {
  const cache = wx.getStorageSync('gateCache_' + campusId)
  if (!cache || !cache.data) {
    return null
  }
  if (Date.now() > cache.expire) {
    return null
  }
  return cache.data
}

function setLocationCache(campusId, list, ttlMillis) {
  const expire = Date.now() + ttlMillis
  wx.setStorageSync('locationCache_' + campusId, { data: list, expire })
}

function getLocationCache(campusId) {
  const cache = wx.getStorageSync('locationCache_' + campusId)
  if (!cache || !cache.data) {
    return null
  }
  if (Date.now() > cache.expire) {
    return null
  }
  return cache.data
}

module.exports = { setSchoolCache, getSchoolCache, setCampusCache, getCampusCache, setGateCache, getGateCache, setLocationCache, getLocationCache }

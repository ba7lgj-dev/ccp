const { BASE_URL } = require('./config.js')

function buildImageUrl(path) {
  if (!path) {
    return ''
  }
  if (path.indexOf('http://') === 0 || path.indexOf('https://') === 0) {
    return path
  }
  let realPath = path
  if (realPath.charAt(0) !== '/') {
    realPath = '/' + realPath
  }
  return BASE_URL + realPath
}

module.exports = { buildImageUrl }

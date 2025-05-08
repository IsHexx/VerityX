/**
 * 日期时间工具函数
 */

/**
 * 格式化日期为 YYYY-MM-DD 格式
 * @param {Date|string} date 日期对象或日期字符串
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date) {
  if (!date) return ''
  
  const d = new Date(date)
  
  // 获取年、月、日
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  
  return `${year}-${month}-${day}`
}

/**
 * 格式化日期时间为 YYYY-MM-DD HH:mm:ss 格式
 * @param {Date|string} date 日期对象或日期字符串
 * @returns {string} 格式化后的日期时间字符串
 */
export function formatDateTime(date) {
  if (!date) return ''
  
  const d = new Date(date)
  
  // 获取年、月、日
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  
  // 获取时、分、秒
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

/**
 * 格式化为时间戳
 * @param {Date|string} date 日期对象或日期字符串
 * @returns {number} 时间戳
 */
export function formatTimestamp(date) {
  if (!date) return 0
  
  const d = new Date(date)
  return d.getTime()
}

/**
 * 获取给定日期是星期几
 * @param {Date|string} date 日期对象或日期字符串
 * @returns {string} 星期几
 */
export function getDayOfWeek(date) {
  if (!date) return ''
  
  const d = new Date(date)
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  
  return weekdays[d.getDay()]
}

/**
 * 获取两个日期之间的天数差
 * @param {Date|string} startDate 开始日期
 * @param {Date|string} endDate 结束日期
 * @returns {number} 天数差
 */
export function getDaysBetween(startDate, endDate) {
  if (!startDate || !endDate) return 0
  
  const start = new Date(startDate)
  const end = new Date(endDate)
  
  // 清除时间部分，只保留日期部分
  start.setHours(0, 0, 0, 0)
  end.setHours(0, 0, 0, 0)
  
  const diffTime = Math.abs(end - start)
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  
  return diffDays
}

export default {
  formatDate,
  formatDateTime,
  formatTimestamp,
  getDayOfWeek,
  getDaysBetween
} 
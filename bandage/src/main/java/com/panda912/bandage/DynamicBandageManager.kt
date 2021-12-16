package com.panda912.bandage

import com.panda912.bandage.data.DynamicBandageData
import java.util.regex.Pattern

/**
 * Created by panda on 2021/12/15 17:30
 */
object DynamicBandageManager {

  private val dynamicBandageDataList = arrayListOf<DynamicBandageData>()

  fun addData(list: List<DynamicBandageData>?) {
    synchronized(this) {
      dynamicBandageDataList.clear()
      if (!list.isNullOrEmpty()) {
        dynamicBandageDataList.addAll(list.filter { isValid(it) })
      }
    }
  }

  @Synchronized
  fun isEmpty(): Boolean {
    return dynamicBandageDataList.isEmpty()
  }

  @Synchronized
  fun getDynamicBandageDataByThrowable(th: Throwable): DynamicBandageData? {
    val className = th.javaClass.name
    for (item in dynamicBandageDataList) {
      if (isValid(item) && className == item.className) {
        val message = th.message
        if (item.messageRegular.isNullOrEmpty()) {
          if (match(item, th)) {
            return item
          }
        } else if (message.isNullOrEmpty()) {
          continue
        } else if (
          (message == item.messageRegular || Pattern.matches(item.messageRegular, message)) &&
          match(item, th)
        ) {
          return item
        }
      }
    }
    return null
  }

  private fun match(data: DynamicBandageData, th: Throwable): Boolean {
    val stackTrace = th.stackTrace
    if (stackTrace.isNullOrEmpty() || data.stacks.isNullOrEmpty()) {
      return false
    }

    val stacks = data.stacks.toMutableList()
    var i = 0
    var count = 0
    while (i < stackTrace.size && i < 20) {
      val str = stackTrace[i].className + "." + stackTrace[i].methodName
      val it = stacks.iterator()
      while (it.hasNext()) {
        if (str == it.next().trim()) {
          count++
          stacks.remove(str)
          break
        }
      }
      i++
    }

    return count >= data.stacks.size
  }

  private fun isValid(data: DynamicBandageData): Boolean {
    if (data.process == "all") {
      return true
    }
    val config = Bandage.config!!
    val processName =
      if (data.process == "main") config.packageName else config.packageName + ":" + data.process
    return processName == config.currentProcessName
  }

}
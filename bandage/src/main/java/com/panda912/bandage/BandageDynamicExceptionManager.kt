package com.panda912.bandage

import android.os.SystemClock
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.data.remove
import com.panda912.bandage.logger.BandageLogger
import kotlin.math.min

/**
 * Created by panda on 2021/12/15 17:30
 */
object BandageDynamicExceptionManager {
  private const val TAG = "BandageDynamicExceptionManager"

  private val dynamicBandageDataList = arrayListOf<DynamicBandageData>()

  fun addData(list: List<DynamicBandageData>?) {
    synchronized(this) {
      dynamicBandageDataList.clear()
      if (!list.isNullOrEmpty()) {
        dynamicBandageDataList.addAll(list.filter { isProcessMatch(it) })
      }
    }
  }

  @Synchronized
  fun getDynamicBandageData(th: Throwable): DynamicBandageData? {
    if (dynamicBandageDataList.isEmpty()) {
      return null
    }

    for (data in dynamicBandageDataList) {
      if (isProcessMatch(data) &&                   // process √
        data.exceptionMatch?.isMatch(th) == true && // class name && message √
        isStackMatch(data, th)                      // stack √
      ) {
        return data
      }
    }
    return null
  }

  private fun isStackMatch(data: DynamicBandageData, th: Throwable): Boolean {
    if (data.stacks.isNullOrEmpty()) {
      return false
    }

    val startRecordTime = SystemClock.elapsedRealtime()

    val causes = getThrowableCauses(th)

    val dataStacks = data.stacks.toMutableList()
    var dataCauses: ArrayList<DynamicBandageData.ExceptionMatch>? = null
    if (data.causes?.isNotEmpty() == true) {
      dataCauses = ArrayList(data.causes)
    }

    for (i in 0 until min(causes.size, 10)) {
      val cause = causes[i]
      if (i > 0 && dataCauses != null) {
        remove(dataCauses, cause)
      }
      val stackTrace = cause.stackTrace
      for (j in 0 until min(stackTrace.size, 20)) {
        if (dataStacks.isEmpty()) {
          break
        }
        val element = stackTrace[j]
        dataStacks.remove("${element.className}.${element.methodName}")
      }
    }

    BandageLogger.i(TAG, "stacks size: ${dataStacks.size}, data.stacks size: ${data.stacks.size}")
    BandageLogger.i(
      TAG,
      "causes size: ${dataCauses?.size ?: 0}, data.causes size: ${data.causes?.size ?: 0}"
    )
    BandageLogger.i(TAG, "match stack cost: ${SystemClock.elapsedRealtime() - startRecordTime}")

    if (dataStacks.isNotEmpty()) {
      return false
    }
    if (dataCauses.isNullOrEmpty()) {
      return true
    }
    return false
  }

  private fun getThrowableCauses(th: Throwable): List<Throwable> {
    val list = ArrayList<Throwable>()
    list.add(th)

    var cur = th
    for (i in 0..9) {
      val next = cur.cause ?: break
      if (next == cur) break
      list.add(next)
      cur = next
    }
    return list
  }

  private fun isProcessMatch(data: DynamicBandageData): Boolean {
    if (data.process == "all") {
      return true
    }
    val processName = if (data.process == "main") {
      Bandage.config!!.packageName
    } else {
      Bandage.config!!.packageName + ":" + data.process
    }
    return processName == Bandage.config!!.currentProcessName
  }

}
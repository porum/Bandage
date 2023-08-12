package com.panda912.bandage.internal.dynamic

import android.os.SystemClock
import com.panda912.bandage.Bandage
import com.panda912.bandage.internal.data.DynamicBandageData
import com.panda912.bandage.internal.data.removeMatchedCause
import com.panda912.bandage.internal.BandageLogger
import kotlin.math.min

/**
 * Created by panda on 2022/2/8 17:10
 */
internal object BandageDataMatcher {
  private const val TAG = "BandageDataMatcher"

  fun isStackMatch(data: DynamicBandageData, th: Throwable): Boolean {
    if (data.stacks.isEmpty()) {
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
        removeMatchedCause(dataCauses, cause)
      }
      val stackTrace = cause.stackTrace
      for (j in 0 until min(stackTrace.size, 40)) {
        if (dataStacks.isEmpty()) {
          break
        }
        val element = stackTrace[j]
        dataStacks.remove("${element.className}.${element.methodName}")
      }
    }

    BandageLogger.i(TAG, "stacks size: ${dataStacks.size}, data.stacks size: ${data.stacks.size}")
    BandageLogger.i(TAG, "causes size: ${dataCauses?.size}, data.causes size: ${data.causes?.size}")
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

  fun isProcessMatch(data: DynamicBandageData): Boolean {
    if (data.process == "all") {
      return true
    }
    val processName = if (data.process == "main") {
      Bandage.config.packageName
    } else {
      Bandage.config.packageName + ":" + data.process
    }
    return processName == Bandage.config.currentProcessName
  }

}
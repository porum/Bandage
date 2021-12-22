package com.panda912.bandage.checkers

import android.util.Log
import com.panda912.bandage.data.CrashData

/**
 * Created by panda on 2021/12/13 18:02
 */
class SerialCrashChecker : ICrashChecker {

  override fun isHopeful(
    crashDataList: List<CrashData>,
    times: Int,
    thread: Thread,
    throwable: Throwable
  ): Boolean {
    if (crashDataList.size < 3) {
      return true
    }
    val last = crashDataList[crashDataList.size - 1]
    val thirdFromLast = crashDataList[crashDataList.size - 3]
    val duration = last.timestamp - thirdFromLast.timestamp
    if (duration < 1000) {
      return false
    }
    val secondFromLast = crashDataList[crashDataList.size - 2]
    if (isSameCrash(last.throwable, secondFromLast.throwable) &&
      isSameCrash(secondFromLast.throwable, thirdFromLast.throwable) &&
      duration < 3000
    ) {
      return false
    }
    return true
  }

  private fun isSameCrash(th1: Throwable, th2: Throwable): Boolean {
    return Log.getStackTraceString(th1) == Log.getStackTraceString(th2)
  }
}
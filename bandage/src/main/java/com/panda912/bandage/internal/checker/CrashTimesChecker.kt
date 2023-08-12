package com.panda912.bandage.internal.checker

import com.panda912.bandage.ICrashChecker
import com.panda912.bandage.internal.data.CrashData

/**
 * Created by panda on 2021/12/13 18:02
 */
class CrashTimesChecker : ICrashChecker {

  override fun isHopeful(
    crashDataList: List<CrashData>,
    times: Int,
    thread: Thread,
    throwable: Throwable
  ): Boolean {
    return times <= 6
  }
}
package com.panda912.bandage.checkers

import com.panda912.bandage.data.CrashData

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
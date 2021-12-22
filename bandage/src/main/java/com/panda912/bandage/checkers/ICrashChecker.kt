package com.panda912.bandage.checkers

import com.panda912.bandage.data.CrashData

/**
 * Created by panda on 2021/12/13 17:59
 */
fun interface ICrashChecker {

  /**
   * check whether need throw crash and exit app.
   * @return true intercept crash and not throw, otherwise return false.
   */
  fun isHopeful(
    crashDataList: List<CrashData>,
    times: Int,
    thread: Thread,
    throwable: Throwable
  ): Boolean
}
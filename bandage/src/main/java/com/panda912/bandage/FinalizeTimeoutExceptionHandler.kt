package com.panda912.bandage

import com.panda912.bandage.logger.BandageLogger
import java.util.concurrent.TimeoutException

/**
 * Created by panda on 2021/12/22 18:00
 */
class FinalizeTimeoutExceptionHandler(
  private val handler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

  override fun uncaughtException(t: Thread, e: Throwable) {
    if (t.name == "FinalizerWatchdogDaemon" && e is TimeoutException) {
      BandageLogger.i(TAG, "catch FinalizeTimeoutException")
      BandageHelper.uploadCrash(e)
    } else {
      handler?.uncaughtException(t, e)
    }
  }
}
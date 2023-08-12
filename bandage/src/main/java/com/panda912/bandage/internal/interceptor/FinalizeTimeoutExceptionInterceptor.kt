package com.panda912.bandage.internal.interceptor

import com.panda912.bandage.BandageHelper
import com.panda912.bandage.IExceptionInterceptor
import com.panda912.bandage.TAG
import com.panda912.bandage.internal.BandageLogger
import java.util.concurrent.TimeoutException

/**
 * Created by panda on 2022/1/14 15:20
 */
internal class FinalizeTimeoutExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "FinalizeTimeoutExceptionInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (thread.name == "FinalizerWatchdogDaemon" && throwable is TimeoutException) {
      BandageLogger.i(TAG, "catch FinalizeTimeoutException")
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
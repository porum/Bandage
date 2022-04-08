package com.panda912.bandage.interceptors

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.utils.RomUtil

/**
 * Created by panda on 2022/4/8 10:15
 */
class StopActivityNPEInterceptor : IExceptionInterceptor {

  override fun getName() = "StopActivityNPEInterceptor"

  override fun shouldEnableOpt(): Boolean {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt == 23 || sdkInt == 24 || sdkInt == 25) {
      val model = Build.MODEL
      if (model.startsWith("GIONEE") || model.startsWith("Funtouch") || model.startsWith("coloros_V3.0")) {
        return true
      }
      return RomUtil.isGionee()
    }
    return false
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is NullPointerException) {
      return false
    }

    val stacktrace = throwable.stackTrace
    if (stacktrace.isNullOrEmpty()) {
      return false
    }

    val element = stacktrace[0]
    if (element != null && element.className == "android.app.ActivityThread" && element.methodName == "handleStopActivity") {
      BandageHelper.uploadCrash(throwable)
      return true
    }

    return false
  }

}
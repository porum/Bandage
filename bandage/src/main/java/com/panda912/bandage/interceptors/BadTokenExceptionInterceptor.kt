package com.panda912.bandage.interceptors

import android.view.WindowManager
import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/13 17:57
 */
class BadTokenExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "BadTokenExceptionInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isBadTokenException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      BandageHelper.finishFatalActivity(throwable)
      return true
    }
    return false
  }

  private fun isBadTokenException(th: Throwable): Boolean {
    val stackTrace = th.stackTrace
    if (th !is WindowManager.BadTokenException ||
      th.message?.contains("is not valid; is your activity running?") == false ||
      stackTrace.isNullOrEmpty()
    ) {
      return false
    }
    var lastIndex = stackTrace.size - 1
    while (lastIndex >= 0 && stackTrace.size - lastIndex <= 20) {
      val element = stackTrace[lastIndex]
      if (element.className == "android.app.ActivityThread" && element.fileName == "ActivityThread.java" && element.methodName == "handleResumeActivity") {
        return true
      }
      lastIndex--
    }
    return false
  }
}
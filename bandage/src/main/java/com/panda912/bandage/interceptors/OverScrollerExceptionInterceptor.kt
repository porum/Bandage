package com.panda912.bandage.interceptors

import android.os.Build
import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/22 14:23
 */
class OverScrollerExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "OverScrollerExceptionInterceptor"

  override fun shouldEnableOpt() = Build.VERSION.SDK_INT == 30

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isOverScrollerException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }

  private fun isOverScrollerException(th: Throwable): Boolean {
    val stackTrace: Array<StackTraceElement>
    if (th is ArrayIndexOutOfBoundsException) {
      stackTrace = th.stackTrace ?: return false
      if (stackTrace.isNotEmpty()) {
        return stackTrace.any {
          it.className == "android.widget.OverScroller\$SplineOverScroller" &&
              it.fileName == "OverScroller.java" &&
              it.methodName == "update"
        }
      }
    }
    return false
  }
}
package com.panda912.bandage.interceptors

import android.os.Build
import android.os.DeadSystemException
import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/13 17:57
 */
class DeadSystemExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "DeadSystemExceptionInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isDeadSystemException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }

  private fun isDeadSystemException(th: Throwable): Boolean {
    if (Build.VERSION.SDK_INT >= 24 && th is RuntimeException && th.cause is DeadSystemException) {
      val stackTrace = th.stackTrace
      if (stackTrace.isNotEmpty()) {
        val element = stackTrace[0]
        return element.className == "android.app.servertransaction.PendingTransactionActions\$StopInfo" &&
            element.fileName == "PendingTransactionActions.java" &&
            element.methodName == "run"
      }
    }
    return false
  }
}
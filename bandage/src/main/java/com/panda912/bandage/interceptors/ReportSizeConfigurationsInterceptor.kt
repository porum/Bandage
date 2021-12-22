package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/22 15:56
 */
class ReportSizeConfigurationsInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isReportSizeConfigurationsException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      BandageHelper.finishFatalActivity(throwable)
      return true
    }
    return false
  }

  private fun isReportSizeConfigurationsException(th: Throwable): Boolean {
    if (th !is IllegalArgumentException) return false
    val message = th.message ?: return false
    if (!message.contains("reportSizeConfigurations: ActivityRecord not found for")) return false
    val stackTrace = th.stackTrace ?: return false
    var lastIndex = stackTrace.size - 1
    while (lastIndex >= 0 && stackTrace.size - lastIndex <= 20) {
      val element = stackTrace[lastIndex]
      if (element.className == "android.app.ActivityThread" &&
        element.fileName == "ActivityThread.java" &&
        element.methodName == "reportSizeConfigurations"
      ) {
        return true
      }
      lastIndex--
    }
    return false
  }
}
package com.panda912.bandage.internal.interceptor

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.IExceptionInterceptor

/**
 * Created by panda on 2022/4/8 10:27
 */
internal class TopResumedActivityInterceptor : IExceptionInterceptor {

  companion object {
    private const val ERR_MSG = "Activity top position already set to onTop=false"
    private const val STACK_CLASS = "android.app.ActivityThread"
    private const val STACK_METHOD = "handleTopResumedActivityChanged"
  }

  override fun getName() = "TopResumedActivityInterceptor"

  override fun shouldEnableOpt(): Boolean {
    return Build.VERSION.SDK_INT == 29
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is IllegalStateException) {
      return false
    }

    val stacktrace = throwable.stackTrace
    if (stacktrace.isNullOrEmpty()) {
      return false
    }

    for (element in stacktrace) {
      if (element != null && element.className == STACK_CLASS && element.methodName == STACK_METHOD && throwable.message == ERR_MSG) {
        BandageHelper.uploadCrash(throwable)
        return true
      }
    }

    return false
  }
}
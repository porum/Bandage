package com.panda912.bandage.sample.bandage.interceptors

import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interceptors.IExceptionInterceptor

/**
 * Created by panda on 2021/12/22 14:42
 */
class SpannableStringBuilderExceptionInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isSpannableStringBuilderException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      BandageHelper.finishFatalActivity(throwable)
      return true
    }
    return false
  }

  private fun isSpannableStringBuilderException(th: Throwable): Boolean {
    if (th is NullPointerException && th.message == "Attempt to invoke interface method 'int java.lang.CharSequence.length()' on a null object reference") {
      val stackTrace = th.stackTrace ?: return false
      if (stackTrace.size >= 5) {
        val element = stackTrace[0]
        if (element.className == "android.text.SpannableStringBuilder" && element.methodName == "replace") {
          return true
        }
      }
    }
    return false
  }
}
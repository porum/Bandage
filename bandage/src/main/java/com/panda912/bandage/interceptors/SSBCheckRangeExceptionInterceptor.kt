package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * https://stackoverflow.com/questions/2214336/java-lang-indexoutofboundsexception-getchars-7-0-has-end-before-start
 *
 * Created by panda on 2022/4/25 14:35
 */
class SSBCheckRangeExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "SSBCheckRangeExceptionInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is IndexOutOfBoundsException) {
      val message = throwable.message ?: return false
      if (message.startsWith("getChars") && message.endsWith(" has end before start")) {
        BandageHelper.uploadCrash(throwable)
        return true
      }
    }
    return false
  }

}
package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/16 9:06
 */
class HWReadExceptionNPEInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable.message != "Attempt to invoke interface method 'void com.huawei.contentsensor.a\$b.d(android.content.ComponentName)' on a null object reference" || throwable !is NullPointerException) {
      return false
    }
    BandageHelper.uploadCrash(throwable)
    BandageHelper.finishFatalActivity(throwable)
    return true
  }
}
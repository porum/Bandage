package com.panda912.bandage.sample.bandage.interceptors

import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interceptors.IExceptionInterceptor

/**
 * Created by panda on 2021/12/16 9:06
 */
class HWReadExceptionNPEInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is NullPointerException && throwable.message == "Attempt to invoke interface method 'void com.huawei.contentsensor.a\$b.d(android.content.ComponentName)' on a null object reference") {
      BandageHelper.uploadCrash(throwable)
      BandageHelper.finishFatalActivity(throwable)
      return true
    }
    return false
  }
}
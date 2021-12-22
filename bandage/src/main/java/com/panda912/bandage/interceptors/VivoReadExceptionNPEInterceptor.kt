package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/16 9:27
 */
class VivoReadExceptionNPEInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is NullPointerException && throwable.message == "Attempt to invoke virtual method 'void com.vivo.upslide.qs.external.ll111.l11l11ll1()' on a null object reference") {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
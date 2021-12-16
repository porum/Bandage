package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/16 9:11
 */
class LooperExceptionInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (thread.name != "view-assit-thread" || throwable.message != "Only one Looper may be created per thread") {
      return false
    }
    BandageHelper.uploadCrash(throwable)
    return true
  }
}
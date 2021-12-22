package com.panda912.bandage.interceptors

import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/22 14:52
 */
class GMSExceptionInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (thread.name == "GoogleApiHandler" &&
      throwable is IllegalStateException &&
      throwable.message?.contains("Results have already been set") == true
    ) {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
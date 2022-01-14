package com.panda912.bandage.sample.bandage.interceptors

import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interceptors.IExceptionInterceptor

/**
 * https://stackoverflow.com/questions/47726111/gms-illegalstateexception-results-have-already-been-set
 *
 * Created by panda on 2021/12/22 14:52
 */
class GMSExceptionInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (thread.name == "GoogleApiHandler" &&
      throwable is IllegalStateException &&
      throwable.message?.contains("Results have already been set") == true &&
      !throwable.stackTrace.isNullOrEmpty() &&
      throwable.stackTrace[0].toString().contains("com.google.android.gms")
    ) {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
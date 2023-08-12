package com.panda912.bandage.internal.interceptor

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.IExceptionInterceptor
import com.panda912.bandage.utils.RomUtil

/**
 * Created by panda on 2021/12/16 9:11
 */
internal class QikuLooperExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "QikuLooperExceptionInterceptor"

  override fun shouldEnableOpt(): Boolean {
    if (Build.VERSION.SDK_INT == 25) {
      try {
        return RomUtil.isQIKU()
      } catch (ignored: Throwable) {
      }
    }
    return false
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (thread.name == "view-assit-thread" && throwable.message == "Only one Looper may be created per thread") {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
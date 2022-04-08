package com.panda912.bandage.interceptors

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.utils.RomUtil

/**
 * Created by panda on 2022/4/8 9:30
 */
class OppoMessageNPEInterceptor : IExceptionInterceptor {

  override fun getName() = "OppoMessageNPEInterceptor"

  override fun shouldEnableOpt(): Boolean {
    val sdkInt = Build.VERSION.SDK_INT
    if (sdkInt == 21 || sdkInt == 22) {
      try {
        return RomUtil.isOPPO()
      } catch (ignored: Throwable) {
      }
    }
    return false
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is NullPointerException) {
      return false
    }

    for (element in throwable.stackTrace) {
      if (element != null && element.className == "android.os.Message" && element.methodName == "toString") {
        BandageHelper.uploadCrash(throwable)
        return true
      }
    }

    return false
  }
}
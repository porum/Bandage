package com.panda912.bandage.internal.interceptor

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.IExceptionInterceptor
import com.panda912.bandage.utils.RomUtil

/**
 * Created by panda on 2021/12/16 9:27
 */
internal class VivoReadExceptionNPEInterceptor : IExceptionInterceptor {

  override fun getName() = "VivoReadExceptionNPEInterceptor"

  override fun shouldEnableOpt(): Boolean {
    if (Build.VERSION.SDK_INT == 27) {
      try {
        return RomUtil.isVIVO()
      } catch (ignored: Throwable) {
      }
    }
    return false
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is NullPointerException && throwable.message == "Attempt to invoke virtual method 'void com.vivo.upslide.qs.external.ll111.l11l11ll1()' on a null object reference") {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
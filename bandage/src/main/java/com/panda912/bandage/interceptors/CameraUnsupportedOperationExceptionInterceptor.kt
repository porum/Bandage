package com.panda912.bandage.interceptors

import android.os.Build
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.logger.BandageLogger

/**
 * Created by panda on 2022/3/3 14:32
 */
class CameraUnsupportedOperationExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "CameraUnsupportedOperationExceptionInterceptor"

  override fun shouldEnableOpt(): Boolean {
    return Build.VERSION.SDK_INT == 27
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is UnsupportedOperationException && throwable.message == "Unknown error -22") {
      BandageLogger.i(getName(), "caught camera_unsupported_operation_exception")
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
package com.panda912.bandage.sample.bandage.interceptors

import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interceptors.IExceptionInterceptor
import com.panda912.bandage.logger.BandageLogger

/**
 * Created by panda on 2022/3/3 14:32
 */

private const val TAG = "CameraUnsupportedOperationExceptionInterceptor"

class CameraUnsupportedOperationExceptionInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable is UnsupportedOperationException && throwable.message == "Unknown error -22") {
      BandageLogger.i(TAG, "caught camera_unsupported_operation_exception")
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }
}
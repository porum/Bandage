package com.panda912.bandage.interceptors

import android.os.Build
import android.view.WindowManager
import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2021/12/16 9:15
 */
class ToastBadTokenExceptionInterceptor : IExceptionInterceptor {

  override fun getName() = "ToastBadTokenExceptionInterceptor"

  override fun shouldEnableOpt(): Boolean {
    val sdkInt = Build.VERSION.SDK_INT
    return sdkInt == 24 || sdkInt == 25
  }

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (isToastBadTokenException(throwable)) {
      BandageHelper.uploadCrash(throwable)
      return true
    }
    return false
  }

  private fun isToastBadTokenException(throwable: Throwable): Boolean {
    val stackTrace = throwable.stackTrace
    if (throwable !is WindowManager.BadTokenException ||
      throwable.message?.contains("is not valid; is your activity running?") == false ||
      stackTrace.isNullOrEmpty()
    ) {
      return false
    }

    var lastIndex = stackTrace.size - 1
    while (lastIndex >= 0 && stackTrace.size - lastIndex <= 20) {
      val element = stackTrace[lastIndex]
      if (element.className == "android.widget.Toast\$TN" && element.fileName == "Toast.java" && element.methodName == "handleShow") {
        return true
      }
      lastIndex--
    }
    return false
  }

}
package com.panda912.bandage.internal.interceptor

import android.view.WindowManager
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.IExceptionInterceptor

/**
 * Created by panda on 2022/4/8 9:48
 */
internal class JsDialogBadTokenInterceptor : IExceptionInterceptor {

  override fun getName() = "JsDialogBadTokenInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is WindowManager.BadTokenException) {
      return false
    }

    for (element in throwable.stackTrace) {
      if (element != null && element.className == "android.webkit.JsDialogHelper" && element.methodName == "showDialog") {
        BandageHelper.uploadCrash(throwable)
        return true
      }
    }

    return false
  }
}
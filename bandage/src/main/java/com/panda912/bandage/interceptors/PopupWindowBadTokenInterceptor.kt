package com.panda912.bandage.interceptors

import android.view.WindowManager
import com.panda912.bandage.BandageHelper

/**
 * Created by panda on 2022/4/8 10:12
 */
class PopupWindowBadTokenInterceptor : IExceptionInterceptor {

  override fun getName() = "PopupWindowBadTokenInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is WindowManager.BadTokenException) {
      return false
    }

    for (element in throwable.stackTrace) {
      if (element != null && element.className == "android.widget.PopupWindow" && element.methodName == "invokePopup") {
        BandageHelper.uploadCrash(throwable)
        return true
      }
    }

    return false
  }
}
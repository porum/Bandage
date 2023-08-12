package com.panda912.bandage.internal.interceptor

import com.panda912.bandage.IExceptionInterceptor

/**
 * Created by panda on 2022/4/8 9:43
 */
internal class BoostMetadataNPEInterceptor : IExceptionInterceptor {

  override fun getName() = "BoostMetadataNPEInterceptor"

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable !is NullPointerException) {
      return false
    }

    for (element in throwable.stackTrace) {
      if (element != null && element.className == "android.app.ActivityThread" && element.methodName == "handleBindApplication") {
        return throwable.toString().contains("Attempt to read from field 'android.os.Bundle android.content.pm.PackageItemInfo.metaData' on a null object reference")
      }
    }

    return false
  }
}
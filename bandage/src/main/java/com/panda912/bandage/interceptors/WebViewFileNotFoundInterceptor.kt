package com.panda912.bandage.interceptors

import android.app.ActivityThread
import android.content.pm.ApplicationInfo
import android.os.Process
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.logger.BandageLogger
import java.io.File

/**
 * Created by panda on 2021/12/22 14:07
 */
class WebViewFileNotFoundInterceptor : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable.cause?.message?.contains("webview_data.lock: open failed: EACCES (Permission denied)") == true) {
      try {
        val applicationInfo: ApplicationInfo = ActivityThread.currentApplication().applicationInfo
        BandageLogger.i(
          "WebViewFileNotFoundInterceptor",
          applicationInfo.dataDir + " canWrite " + File(applicationInfo.dataDir).canWrite()
        )
      } catch (ignored: Throwable) {
      }
      BandageHelper.uploadCrash(throwable)
      Process.killProcess(Process.myPid())
      return true
    }
    return false
  }
}
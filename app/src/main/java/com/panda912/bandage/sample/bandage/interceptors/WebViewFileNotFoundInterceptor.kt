package com.panda912.bandage.sample.bandage.interceptors

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Process
import com.panda912.bandage.BandageHelper
import com.panda912.bandage.interceptors.IExceptionInterceptor
import com.panda912.bandage.logger.BandageLogger
import java.io.File

/**
 * Created by panda on 2021/12/22 14:07
 */

private const val TAG = "WebViewFileNotFoundInterceptor"

class WebViewFileNotFoundInterceptor(private val context: Context) : IExceptionInterceptor {

  override fun intercept(thread: Thread, throwable: Throwable): Boolean {
    if (throwable.cause?.message?.contains("webview_data.lock: open failed: EACCES (Permission denied)") == true) {
      val appInfo: ApplicationInfo = context.applicationInfo
      BandageLogger.i(TAG, appInfo.dataDir + " canWrite " + File(appInfo.dataDir).canWrite())
      BandageHelper.uploadCrash(throwable)
      Process.killProcess(Process.myPid())
      return true
    }
    return false
  }
}
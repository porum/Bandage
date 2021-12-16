package com.panda912.bandage.fixer

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.os.Build
import com.panda912.bandage.logger.BandageLogger
import java.lang.reflect.Proxy

/**
 * Created by panda on 2021/12/16 14:32
 */
object FixReportSizeConfigurations {

  @SuppressLint("PrivateApi", "DiscouragedPrivateApi")
  fun hook() {
    if (Build.VERSION.SDK_INT in 26..28) {
      try {
        val SingletonCls = Class.forName("android.util.Singleton")
        val IActivityManagerSingleton =
          ActivityManager::class.java.getDeclaredField("IActivityManagerSingleton").apply {
            isAccessible = true
          }.get(null)
        if (IActivityManagerSingleton == null) {
          BandageLogger.w("", "IActivityManagerSingleton is null")
          return
        }
        val mInstanceField = SingletonCls.getDeclaredField("mInstance").apply {
          isAccessible = true
        }
        val iActivityManager = mInstanceField.get(IActivityManagerSingleton)
        if (iActivityManager == null) {
          BandageLogger.w("", "iActivityManager is null")
          return
        }

        val IActivityManagerCls = Class.forName("android.app.IActivityManager")
        mInstanceField.set(
          IActivityManagerSingleton,
          Proxy.newProxyInstance(
            IActivityManagerCls.classLoader,
            arrayOf(IActivityManagerCls),
            IActivityManagerProxy(iActivityManager)
          )
        )
        BandageLogger.w("", "hook IActivityManagerProxy success")
      } catch (th: Throwable) {
        BandageLogger.w("", "hook IActivityManagerProxy failed", th)
      }
    }
  }
}
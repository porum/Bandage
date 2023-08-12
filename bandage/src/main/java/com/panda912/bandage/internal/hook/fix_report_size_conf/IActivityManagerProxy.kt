package com.panda912.bandage.internal.hook.fix_report_size_conf

import com.panda912.bandage.TAG
import com.panda912.bandage.internal.BandageLogger
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Created by panda on 2021/12/16 14:49
 */
internal class IActivityManagerProxy(private val iActivityManager: Any) : InvocationHandler {

  override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
    if (method?.name == "reportSizeConfigurations") {
      BandageLogger.i(TAG, "proxy: android.app.IActivityManager\$Stub\$Proxy.reportSizeConfigurations")
      return try {
        method.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
      } catch (th: Throwable) {
        null
      }
    }
    return method?.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
  }
}
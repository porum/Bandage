package com.panda912.bandage.fixer

import com.panda912.bandage.logger.BandageLogger
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

/**
 * Created by panda on 2021/12/16 14:49
 */
class IActivityManagerProxy(private val iActivityManager: Any) : InvocationHandler {

  override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
    if (method?.name == "reportSizeConfigurations") {
      BandageLogger.i(
        "",
        "proxy: android.app.IActivityManager\$Stub\$Proxy.reportSizeConfigurations"
      )
      return try {
        method.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
      } catch (th: Throwable) {
        null
      }
    }
    return method?.invoke(iActivityManager, *(args ?: arrayOfNulls<Any>(0)))
  }
}
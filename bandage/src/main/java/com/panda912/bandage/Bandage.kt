package com.panda912.bandage

import android.app.Application
import com.panda912.bandage.internal.BandageInternal
import com.panda912.bandage.internal.utils.ActivityManager

/**
 * Created by panda on 2021/12/10 14:49
 */
object Bandage {

  @JvmStatic
  fun install(context: Application) {
    install(context, ILogger.DEFAULT)
  }

  @JvmStatic
  fun install(context: Application, logger: ILogger = ILogger.DEFAULT) {
    context.registerActivityLifecycleCallbacks(ActivityManager.getInstance())
    BandageInternal().install(logger)
  }
}
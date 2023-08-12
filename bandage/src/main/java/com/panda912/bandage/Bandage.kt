package com.panda912.bandage

import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.hook.ViewRootImplHandlerHooker
import com.panda912.bandage.hook.activity_thread_hook.ActivityThreadHandlerHooker
import com.panda912.bandage.hook.fix_report_size_conf.FixReportSizeConfigurations
import com.panda912.bandage.logger.BandageLogger
import com.panda912.bandage.utils.ActivityManager

internal const val TAG = "Bandage"

/**
 * Created by panda on 2021/12/14 14:03
 */
object Bandage {

  internal lateinit var config: IBandageConfig

  @JvmStatic
  fun install(config: IBandageConfig) {
    check(this::config.isInitialized.not()) {
      "Bandage already installed"
    }
    if (!config.isEnable) return

    this.config = config
    BandageLogger.logger = config.logger
    config.application.registerActivityLifecycleCallbacks(ActivityManager.getInstance())

    Thread.setDefaultUncaughtExceptionHandler(
      BandageExceptionHandler(
        config,
        Thread.getDefaultUncaughtExceptionHandler()
      )
    )

    if (config.enableActivityThreadHook) {
      ActivityThreadHandlerHooker.hook()
    }
    if (config.enableViewRootImplHandlerHook) {
      ViewRootImplHandlerHooker.hook(config.application)
    }
    if (config.enableFixReportSizeConfigurations) {
      FixReportSizeConfigurations.hook()
    }
  }

  @JvmStatic
  fun addDynamicBandageData(list: List<DynamicBandageData>) {
    if (config.enableDynamicBandageInterceptor) {
      BandageDynamicExceptionManager.addData(list)
    }
  }

}
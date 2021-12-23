package com.panda912.bandage

import com.panda912.bandage.hook.activity_thread_hook.ActivityThreadHandlerHooker
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.hook.fix_report_size_conf.FixReportSizeConfigurations
import com.panda912.bandage.logger.BandageLogger

internal const val TAG = "Bandage"

/**
 * Created by panda on 2021/12/14 14:03
 */
object Bandage {

  @JvmField
  var config: IBandageConfig? = null

  fun install(config: IBandageConfig) {
    check(this.config == null) {
      "Bandage already installed"
    }
    if (!config.isEnable) {
      return
    }

    BandageLogger.i(TAG, "init")

    this.config = config
    BandageLogger.logger = config.logger

    Thread.setDefaultUncaughtExceptionHandler(
      FinalizeTimeoutExceptionHandler(
        BandageExceptionHandler(config, Thread.getDefaultUncaughtExceptionHandler())
      )
    )

    ActivityThreadHandlerHooker.hook()
    FixReportSizeConfigurations.hook()
  }

  fun addDynamicBandageData(list: List<DynamicBandageData>) {
    if (config?.enableDynamicBandageInterceptor == true) {
      BandageDynamicExceptionManager.addData(list)
    }
  }

}
package com.panda912.bandage.sample

import android.os.Build
import com.panda912.bandage.IBandageConfig
import com.panda912.bandage.interceptors.*
import com.panda912.bandage.logger.ILogger

/**
 * Created by panda on 2021/12/16 10:50
 */
class BandageConfig : IBandageConfig {
  override val bandageEnable = true
  override val activityThreadHandlerHookerEnable = true
  override val fixReportSizeConfigurations = true
  override val handleCrashByBandage = true
  override val packageName = "com.panda912.bandage.sample"
  override val currentProcessName = "com.panda912.bandage.sample"
  override val enableDynamicBandageInterceptor = true
  override val fixFinalizeTimeoutException = true
  override val enableSubThreadCrash = true
  override val disableCatchBadTokenInSubProcess = false
  override val behavior = BandageBehavior()
  override fun interceptors(): List<IExceptionInterceptor> {
    val list = arrayListOf<IExceptionInterceptor>()
    if (Build.VERSION.SDK_INT == 23 || Build.VERSION.SDK_INT == 25) {
      list.add(ToastBadTokenExceptionInterceptor())
    }
    list.add(LooperExceptionInterceptor())
    list.add(HWReadExceptionNPEInterceptor())
    list.add(VivoReadExceptionNPEInterceptor())
    return list
  }

  override val logger = ILogger.DEFAULT
}
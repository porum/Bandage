package com.panda912.bandage.sample.bandage

import android.app.Application
import com.panda912.bandage.IBandageConfig
import com.panda912.bandage.ICrashChecker
import com.panda912.bandage.IExceptionInterceptor
import com.panda912.bandage.ILogger

/**
 * Created by panda on 2021/12/16 10:50
 */
class BandageConfig(override val application: Application) : IBandageConfig {
  override val logger = ILogger.DEFAULT
  override val isEnable = true
  override val packageName = "com.panda912.bandage.sample"
  override val currentProcessName = "com.panda912.bandage.sample"
  override val enableSubThreadCrash = true
  override val enableCatchBadTokenInSubProcess = true
  override val enableDynamicBandageInterceptor = true
  override val enableActivityThreadHook = true
  override val enableViewRootImplHandlerHook = true
  override val enableFixReportSizeConfigurations = true
  override val behavior = BandageBehavior()
  override val interceptors: List<IExceptionInterceptor>? = null
  override val checkers: List<ICrashChecker>? = null
}
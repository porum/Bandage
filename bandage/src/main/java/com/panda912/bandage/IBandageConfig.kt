package com.panda912.bandage

import com.panda912.bandage.interceptors.IExceptionInterceptor

/**
 * Created by panda on 2021/12/13 18:07
 */
interface IBandageConfig {
  val bandageEnable: Boolean
  val activityThreadHandlerHookerEnable: Boolean
  val fixReportSizeConfigurations: Boolean
  val handleCrashByBandage: Boolean
  val packageName: String
  val currentProcessName: String
  val enableDynamicBandageInterceptor: Boolean
  val fixFinalizeTimeoutException: Boolean
  val enableSubThreadCrash: Boolean
  val disableCatchBadTokenInSubProcess: Boolean
  val behavior: IBandageBehavior
  fun interceptors(): List<IExceptionInterceptor>?
}
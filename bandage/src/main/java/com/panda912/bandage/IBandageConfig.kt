package com.panda912.bandage

import com.panda912.bandage.interceptors.IExceptionInterceptor
import com.panda912.bandage.logger.ILogger

/**
 * Created by panda on 2021/12/13 18:07
 */
interface IBandageConfig {
  val isEnable: Boolean
  val activityThreadHandlerHookerEnable: Boolean
  val handleCrashByBandage: Boolean
  val packageName: String
  val currentProcessName: String
  val enableDynamicBandageInterceptor: Boolean
  val enableSubThreadCrash: Boolean
  val enableCatchBadTokenInSubProcess: Boolean
  val behavior: IBandageBehavior
  fun interceptors(): List<IExceptionInterceptor>?
  val logger: ILogger?
}
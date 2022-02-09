package com.panda912.bandage

import android.app.Application
import com.panda912.bandage.interceptors.IExceptionInterceptor
import com.panda912.bandage.logger.ILogger

/**
 * Created by panda on 2021/12/13 18:07
 */
interface IBandageConfig {
  val application: Application
  val logger: ILogger?
  val isEnable: Boolean
  val packageName: String
  val currentProcessName: String
  val enableSubThreadCrash: Boolean
  val enableCatchBadTokenInSubProcess: Boolean
  val enableDynamicBandageInterceptor: Boolean
  val enableActivityThreadHook: Boolean
  val enableFixReportSizeConfigurations: Boolean
  val behavior: IBandageBehavior
  val interceptors: List<IExceptionInterceptor>?
}
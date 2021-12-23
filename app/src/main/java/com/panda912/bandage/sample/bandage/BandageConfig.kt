package com.panda912.bandage.sample.bandage

import android.app.Application
import android.os.Build
import com.panda912.bandage.IBandageConfig
import com.panda912.bandage.interceptors.*
import com.panda912.bandage.logger.ILogger
import com.panda912.bandage.sample.bandage.interceptors.*

/**
 * Created by panda on 2021/12/16 10:50
 */
class BandageConfig(private val application: Application) : IBandageConfig {
  override val logger = ILogger.DEFAULT
  override val isEnable = true
  override val packageName = "com.panda912.bandage.sample"
  override val currentProcessName = "com.panda912.bandage.sample"
  override val enableSubThreadCrash = true
  override val enableCatchBadTokenInSubProcess = true
  override val enableDynamicBandageInterceptor = true
  override val behavior = BandageBehavior()
  override val interceptors = arrayListOf<IExceptionInterceptor>().apply {
    add(SpannableStringBuilderExceptionInterceptor())
    add(WebViewFileNotFoundInterceptor(application))
    add(ReportSizeConfigurationsInterceptor())
    add(GMSExceptionInterceptor())
    if (Build.VERSION.SDK_INT == 23 || Build.VERSION.SDK_INT == 25) {
      add(ToastBadTokenExceptionInterceptor())
    }
    if (Build.VERSION.SDK_INT == 25 &&
      Build.MANUFACTURER.contains(Regex("QIKU|360", RegexOption.IGNORE_CASE))
    ) {
      add(LooperExceptionInterceptor())
    }
    if (Build.VERSION.SDK_INT == 27 &&
      Build.MANUFACTURER.contains("HUAWEI", true)
    ) {
      add(HWReadExceptionNPEInterceptor())
    }
    if (Build.VERSION.SDK_INT == 27 &&
      Build.MANUFACTURER.contains(Regex("vivo|bbk", RegexOption.IGNORE_CASE))
    ) {
      add(VivoReadExceptionNPEInterceptor())
    }
    if (Build.VERSION.SDK_INT == 30) {
      add(OverScrollerExceptionInterceptor())
    }
  }
}
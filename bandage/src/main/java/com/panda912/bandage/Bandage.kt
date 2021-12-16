package com.panda912.bandage

import com.panda912.bandage.activity_thread_hook.ActivityThreadHandlerHooker
import com.panda912.bandage.checkers.CrashTimesChecker
import com.panda912.bandage.checkers.ICrashChecker
import com.panda912.bandage.checkers.SerialCrashChecker
import com.panda912.bandage.data.CrashData
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.fixer.FixReportSizeConfigurations
import com.panda912.bandage.interceptors.BadTokenExceptionInterceptor
import com.panda912.bandage.interceptors.DeadSystemExceptionInterceptor
import com.panda912.bandage.interceptors.DynamicBandageInterceptor
import com.panda912.bandage.interceptors.IExceptionInterceptor
import com.panda912.bandage.logger.BandageLogger
import com.panda912.bandage.logger.ILogger

/**
 * Created by panda on 2021/12/14 14:03
 */
object Bandage {

  @JvmField
  var config: IBandageConfig? = null

  private var isInstalled = false
  private var crashTimes = 0
  private val crashList = arrayListOf<CrashData>()
  private val checkers = arrayListOf<ICrashChecker>()
  private val interceptors = arrayListOf<IExceptionInterceptor>()

  fun config(cfg: IBandageConfig): Bandage {
    config = cfg
    return this
  }

  fun install() {
    config?.let {
      if (!isInstalled) {
        isInstalled = true
        if (it.bandageEnable) {
          if (it.activityThreadHandlerHookerEnable) {
            ActivityThreadHandlerHooker.install()
          }
          if (it.fixReportSizeConfigurations) {
            FixReportSizeConfigurations.hook()
          }
          addCheckersAndInterceptors()
          BandageInternal.install(it, ILogger.DEFAULT, object : ExceptionHandler() {
            override fun onEnterSafeMode() {
              BandageLogger.i("", "enter bandage mode")
            }

            override fun onUncaughtExceptionHappened(thread: Thread, throwable: Throwable) {
              if (!interceptCrash(thread, throwable)) {
                handleCrashAndExit(thread, throwable)
              }
            }

            override fun onBandageExceptionHappened(thread: Thread, throwable: Throwable) {
              if (!interceptCrash(thread, throwable)) {
                crashTimes++
                if (notThrowInSafeMode(crashTimes, thread, throwable)) {
                  BandageHelper.uploadCrash(throwable)
                } else {
                  handleCrashAndExit(thread, throwable)
                }
              }
            }
          })
        }
      }
    } ?: run {
      throw RuntimeException("config cannot be null.")
    }
  }

  private fun addCheckersAndInterceptors() {
    checkers.add(CrashTimesChecker())
    checkers.add(SerialCrashChecker())

    val cfg = config!!
    val configInterceptors = cfg.interceptors()
    if (!configInterceptors.isNullOrEmpty()) {
      interceptors.addAll(configInterceptors)
    }
    if (!cfg.disableCatchBadTokenInSubProcess || cfg.packageName == cfg.currentProcessName) {
      interceptors.add(BadTokenExceptionInterceptor())
    }
    interceptors.add(DeadSystemExceptionInterceptor())
    if (cfg.enableDynamicBandageInterceptor) {
      interceptors.add(DynamicBandageInterceptor())
    }
  }

  private fun handleCrashAndExit(thread: Thread, throwable: Throwable) {
    BandageLogger.w("", "handle crash and exit", throwable)
    BandageInternal.handleCrashByDefaultHandler(thread, throwable)
  }

  private fun notThrowInSafeMode(times: Int, thread: Thread, th: Throwable): Boolean {
    crashList.add(CrashData(th, System.currentTimeMillis()))
    return !checkers.any { !it.check(crashList, times, thread, th) }
  }

  private fun interceptCrash(thread: Thread, throwable: Throwable): Boolean {
    return interceptors.any { it.intercept(thread, throwable) }
  }

  fun addDynamicBandageData(list: List<DynamicBandageData>) {
    if (config?.enableDynamicBandageInterceptor == true) {
      DynamicBandageManager.addData(list)
    }
  }

}
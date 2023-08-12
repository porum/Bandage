package com.panda912.bandage

import android.os.Looper
import com.panda912.bandage.checkers.CrashTimesChecker
import com.panda912.bandage.checkers.ICrashChecker
import com.panda912.bandage.checkers.SerialCrashChecker
import com.panda912.bandage.data.CrashData
import com.panda912.bandage.interceptors.*
import com.panda912.bandage.logger.BandageLogger
import com.panda912.bandage.utils.isChoreographerException
import com.panda912.bandage.utils.isOutOfMemoryError

/**
 * Created by panda on 2021/12/22 18:09
 */
class BandageExceptionHandler(
  private val config: IBandageConfig,
  private val handler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

  private var crashTimes = 0
  private val crashList = arrayListOf<CrashData>()
  private val checkers = arrayListOf<ICrashChecker>()
  private val interceptors = arrayListOf<IExceptionInterceptor>()

  init {
    addCheckers()
    addInterceptors()
  }

  private fun addCheckers() {
    checkers.add(CrashTimesChecker())
    checkers.add(SerialCrashChecker())
    val configCheckers = config.checkers ?: return
    if (configCheckers.isNotEmpty()) {
      checkers.addAll(configCheckers)
    }
  }

  private fun addInterceptors() {
    interceptors.add(FinalizeTimeoutExceptionInterceptor())
    interceptors.add(DeadSystemExceptionInterceptor())
    interceptors.add(ReportSizeConfigurationsInterceptor())
    interceptors.add(BoostMetadataNPEInterceptor())
    interceptors.add(TopResumedActivityInterceptor())
    interceptors.add(StopActivityNPEInterceptor())
    interceptors.add(PopupWindowBadTokenInterceptor())
    interceptors.add(JsDialogBadTokenInterceptor())
    interceptors.add(WebViewFileNotFoundInterceptor(config.application))
    interceptors.add(CameraUnsupportedOperationExceptionInterceptor())
    interceptors.add(OverScrollerExceptionInterceptor())
    interceptors.add(SpannableStringBuilderExceptionInterceptor())
    interceptors.add(SSBCheckRangeExceptionInterceptor())
    interceptors.add(GMSExceptionInterceptor())
    interceptors.add(HWReadExceptionNPEInterceptor())
    interceptors.add(OppoMessageNPEInterceptor())
    interceptors.add(VivoReadExceptionNPEInterceptor())
    interceptors.add(QikuLooperExceptionInterceptor())
    if (config.packageName == config.currentProcessName || config.enableCatchBadTokenInSubProcess) {
      interceptors.add(BadTokenExceptionInterceptor())
    }
    if (config.enableDynamicBandageInterceptor) {
      interceptors.add(DynamicBandageInterceptor())
    }
    val configInterceptors = config.interceptors ?: return
    if (configInterceptors.isNotEmpty()) {
      interceptors.addAll(configInterceptors)
    }
  }

  override fun uncaughtException(t: Thread, e: Throwable) {
    val isMainThread = t == Looper.getMainLooper().thread
    if (((isMainThread && !e.isChoreographerException()) || (!isMainThread && config.enableSubThreadCrash)) && !e.isOutOfMemoryError()) {
      uncaughtExceptionHappened(t, e)
      safeMode(t)
    } else {
      handleCrashByDefaultHandler(t, e)
    }
  }

  private fun safeMode(thread: Thread?) {
    BandageLogger.i(TAG, "enter bandage mode")

    val threadName: String = if (thread == null) "null" else thread.name
    BandageLogger.i(TAG, "bandage exception in thread[$threadName]")
    if (Looper.myLooper() == null) {
      BandageLogger.w(TAG, "There is no loop in thread[$threadName]")
      return
    }

    while (true) {
      try {
        Looper.loop()
      } catch (th: Throwable) {
        if (th.isOutOfMemoryError() || (th.isChoreographerException() && thread == Looper.getMainLooper().thread)) {
          handleCrashByDefaultHandler(Thread.currentThread(), th)
        } else {
          bandageExceptionHappened(Thread.currentThread(), th)
        }
      }
    }
  }

  private fun uncaughtExceptionHappened(thread: Thread, th: Throwable) {
    try {
      if (!isIntercept(thread, th)) {
        handleCrashByDefaultHandler(thread, th)
      }
    } catch (th: Throwable) {
      BandageLogger.w(TAG, "uncaughtExceptionHappened", th)
    }
  }

  private fun bandageExceptionHappened(thread: Thread, th: Throwable) {
    try {
      if (!isIntercept(thread, th)) {
        crashTimes++
        if (isHopeless(crashTimes, thread, th)) {
          handleCrashByDefaultHandler(thread, th)
        } else {
          BandageLogger.w(TAG, "crash in safe mode and has been consumed.", th)
        }
      }
    } catch (ex: Throwable) {
      uncaughtExceptionHappened(Thread.currentThread(), ex)
    }
  }

  private fun isHopeless(times: Int, thread: Thread, th: Throwable): Boolean {
    crashList.add(CrashData(th, System.currentTimeMillis()))
    return checkers.any { !it.isHopeful(crashList, times, thread, th) }
  }

  private fun isIntercept(thread: Thread, throwable: Throwable): Boolean {
    return interceptors.any { it.shouldEnableOpt() && it.intercept(thread, throwable) }
  }

  private fun handleCrashByDefaultHandler(thread: Thread, throwable: Throwable) {
    BandageLogger.w(TAG, "handle crash by default handler", throwable)
    handler?.uncaughtException(thread, throwable)
  }
}
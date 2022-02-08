package com.panda912.bandage

import android.os.Looper
import com.panda912.bandage.checkers.CrashTimesChecker
import com.panda912.bandage.checkers.SerialCrashChecker
import com.panda912.bandage.data.CrashData
import com.panda912.bandage.interceptors.*
import com.panda912.bandage.logger.BandageLogger

/**
 * Created by panda on 2021/12/22 18:09
 */
class BandageExceptionHandler(
  private val config: IBandageConfig,
  private val handler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler {

  private var crashTimes = 0
  private val crashList = arrayListOf<CrashData>()
  private val checkers = arrayListOf(CrashTimesChecker(), SerialCrashChecker())
  private val interceptors = arrayListOf<IExceptionInterceptor>()

  init {
    addInterceptors()
  }

  private fun addInterceptors() {
    interceptors.add(FinalizeTimeoutExceptionInterceptor())
    interceptors.add(DeadSystemExceptionInterceptor())
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
    if (t == Looper.getMainLooper().thread || config.enableSubThreadCrash) {
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
        bandageExceptionHappened(Thread.currentThread(), th)
      }
    }
  }

  private fun uncaughtExceptionHappened(thread: Thread, th: Throwable) {
    try {
      if (!isIntercept(thread, th)) {
        handleCrashByDefaultHandler(thread, th)
      }
    } catch (th: Throwable) {
      BandageHelper.uploadCrash(th)
    }
  }

  private fun bandageExceptionHappened(thread: Thread, th: Throwable) {
    try {
      if (!isIntercept(thread, th)) {
        crashTimes++
        if (isHopeless(crashTimes, thread, th)) {
          handleCrashByDefaultHandler(thread, th)
        } else {
          BandageHelper.uploadCrash(th)
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
    return interceptors.any { it.intercept(thread, throwable) }
  }

  private fun handleCrashByDefaultHandler(thread: Thread, throwable: Throwable) {
    BandageLogger.i(TAG, "handle crash by default handler")
    handler?.uncaughtException(thread, throwable)
  }
}
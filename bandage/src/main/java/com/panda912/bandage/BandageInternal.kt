package com.panda912.bandage

import android.os.Looper
import com.panda912.bandage.logger.BandageLogger
import java.util.concurrent.TimeoutException

/**
 * Created by panda on 2021/12/13 17:55
 */
object BandageInternal {
  private const val TAG = "BandageInternal"
  private var isInstalled = false
  private var isSafeMode = false
  private var exceptionHandler: ExceptionHandler? = null
  private var defaultUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null

  fun install(config: IBandageConfig, handler: ExceptionHandler) {
    if (!isInstalled) {
      BandageLogger.i(TAG, "bandage init")
      isInstalled = true
      exceptionHandler = handler
      defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
      Thread.setDefaultUncaughtExceptionHandler { t, e ->
        if (config.fixFinalizeTimeoutException && t.name == "FinalizerWatchdogDaemon" && e is TimeoutException) {
          BandageLogger.i("", "catch FinalizeTimeoutException")
          BandageHelper.uploadCrash(e)
        } else if (t == Looper.getMainLooper().thread || config.enableSubThreadCrash) {
          handler.uncaughtExceptionHappened(t, e)
          safeMode(t)
        } else {
          handleCrashByDefaultHandler(t, e)
        }
      }
    }
  }

  fun handleCrashByDefaultHandler(thread: Thread, throwable: Throwable) {
    BandageLogger.i(TAG, "handle crash by default handler")
    defaultUncaughtExceptionHandler?.uncaughtException(thread, throwable)
  }

  private fun safeMode(thread: Thread?) {
    isSafeMode = true
    exceptionHandler?.enterSafeMode()

    val threadName: String = if (thread == null) "null" else thread.name
    BandageLogger.w(TAG, "bandage exception in thread[$threadName]")
    if (Looper.myLooper() == null) {
      BandageLogger.w(TAG, "There is no loop in thread[$threadName]")
      return
    }

    while (true) {
      try {
        Looper.loop()
      } catch (th: Throwable) {
        exceptionHandler?.bandageExceptionHappened(Thread.currentThread(), th)
      }
    }
  }
}
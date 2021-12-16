package com.panda912.bandage

/**
 * Created by panda on 2021/12/13 18:11
 */
abstract class ExceptionHandler {
  abstract fun onEnterSafeMode()
  abstract fun onUncaughtExceptionHappened(thread: Thread, throwable: Throwable)
  abstract fun onBandageExceptionHappened(thread: Thread, throwable: Throwable)

  fun enterSafeMode() {
    try {
      onEnterSafeMode()
    } catch (th: Throwable) {
      uncaughtExceptionHappened(Thread.currentThread(), th)
    }
  }

  fun uncaughtExceptionHappened(thread: Thread, throwable: Throwable) {
    try {
      onUncaughtExceptionHappened(thread, throwable)
    } catch (th: Throwable) {
      println("catch uncaughtExceptionHappened happened exception: $th")
    }
  }

  fun bandageExceptionHappened(thread: Thread, throwable: Throwable) {
    try {
      onBandageExceptionHappened(thread, throwable)
    } catch (th: Throwable) {
      uncaughtExceptionHappened(Thread.currentThread(), th)
    }
  }

}
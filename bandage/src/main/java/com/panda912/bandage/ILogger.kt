package com.panda912.bandage

import android.util.Log

/**
 * Created by panda on 2021/12/10 12:20.
 */

object BandageLogger {

  interface ILogger {
    fun i(tag: String, message: String)
    fun w(tag: String, message: String, throwable: Throwable?)

    companion object DEFAULT : ILogger {
      override fun i(tag: String, message: String) {
        Log.i(tag, message)
      }

      override fun w(tag: String, message: String, throwable: Throwable?) {
        Log.w(tag, message, throwable)
      }
    }
  }

  @Volatile
  var logger: ILogger? = null

  @JvmStatic
  fun i(tag: String, message: String) {
    val logger = logger ?: return
    logger.i(tag, message)
  }

  @JvmStatic
  fun w(tag: String, message: String, throwable: Throwable? = null) {
    val logger = logger ?: return
    logger.w(tag, message, throwable)
  }

}

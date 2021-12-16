package com.panda912.bandage.logger

/**
 * Created by panda on 2021/12/10 12:20.
 */
object BandageLogger {

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

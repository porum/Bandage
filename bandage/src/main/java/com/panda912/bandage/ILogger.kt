package com.panda912.bandage

import android.util.Log

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
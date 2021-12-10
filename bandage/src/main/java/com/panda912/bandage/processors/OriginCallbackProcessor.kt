package com.panda912.bandage.processors

import android.os.Handler
import com.panda912.bandage.BandageLogger
import com.panda912.bandage.Processor

/**
 * Created by panda on 2021/12/6 17:05
 */
class OriginCallbackProcessor(private val callback: Handler.Callback?) : Processor {
  companion object {
    private const val TAG = "OriginCallbackProcessor"
  }

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()
    if (callback != null) {
      try {
        if (callback.handleMessage(message)) {
          return true
        }
      } catch (th: Throwable) {
        BandageLogger.w(TAG, "origin callback handle message error", th)
      }
    }

    return chain.proceed(message)
  }
}
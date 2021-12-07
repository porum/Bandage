package com.panda912.bandage.processors

import android.os.Handler
import android.util.Log
import com.panda912.bandage.Bandage.TAG
import com.panda912.bandage.Processor

/**
 * Created by panda on 2021/12/6 17:05
 */
class OriginCallbackProcessor(private val callback: Handler.Callback?) : Processor {

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()
    if (callback != null) {
      try {
        if (callback.handleMessage(message)) {
          return true
        }
      } catch (th: Throwable) {
        Log.w(TAG, "origin callback handle message error", th)
      }
    }

    return chain.proceed(message)
  }
}
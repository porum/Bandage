package com.panda912.bandage.processors

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.panda912.bandage.Bandage.TAG
import com.panda912.bandage.Processor

/**
 * Created by panda on 2021/12/6 17:04
 */
class FixMsg2StrCrashProcessor : Processor {

  private val handler = Handler(Looper.getMainLooper())

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()

    val isOppo = "oppo".equals(Build.MANUFACTURER, true) && Build.VERSION.SDK_INT == 22
    if (isOppo && message.target == null) {
      message.target = handler
      val reason = "message[" + message.what + "].target is null"
      Log.e(TAG, "fixMessageToStringCrash :$reason")
    }

    return chain.proceed(message)
  }
}
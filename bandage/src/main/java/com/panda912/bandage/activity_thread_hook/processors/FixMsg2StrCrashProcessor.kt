package com.panda912.bandage.activity_thread_hook.processors

import android.os.Build
import android.os.Handler
import android.os.Looper
import com.panda912.bandage.logger.BandageLogger
import com.panda912.bandage.activity_thread_hook.Processor

/**
 * Created by panda on 2021/12/6 17:04
 */
class FixMsg2StrCrashProcessor : Processor {
  companion object {
    private const val TAG = "FixMsg2StrCrashProcessor"
  }

  private val handler = Handler(Looper.getMainLooper())

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()

    val isOppo = "oppo".equals(Build.MANUFACTURER, true) && Build.VERSION.SDK_INT == 22
    if (isOppo && message.target == null) {
      message.target = handler
      val reason = "message[" + message.what + "].target is null"
      BandageLogger.w(TAG,"fixMessageToStringCrash :$reason")
    }

    return chain.proceed(message)
  }
}
package com.panda912.bandage.activity_thread_hook.processors

import android.os.Build
import android.os.Bundle
import com.android.internal.os.SomeArgs
import com.panda912.bandage.logger.BandageLogger
import com.panda912.bandage.activity_thread_hook.Processor

/**
 * Created by panda on 2021/12/7 10:25
 */
class UiMsgInterceptor : Processor {
  companion object {
    private const val TAG = "UiMsgInterceptor"
  }

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()

    if (!"huawei".equals(Build.MANUFACTURER, true)) {
      return chain.proceed(message)
    }

    if (message.obj is SomeArgs) {
      val someArgs = message.obj as SomeArgs
      if (someArgs.arg2 is Bundle && (someArgs.arg2 as Bundle).containsKey("IGrabNodeReceiver")) {
        BandageLogger.i(TAG, "huawei is grabing node.")
        return true
      }
      return chain.proceed(message)
    } else if (message.obj.javaClass.name != "android.app.ActivityThread\$RequestContentNode") {
      return chain.proceed(message)
    } else {
      message.obj = null
      message.what = -1
      BandageLogger.i(TAG, "huawei is grabing node.")
      return true
    }

  }
}
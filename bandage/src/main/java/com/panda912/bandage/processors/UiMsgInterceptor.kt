package com.panda912.bandage.processors

import android.os.Build
import android.os.Bundle
import com.android.internal.os.SomeArgs
import com.panda912.bandage.Bandage.log
import com.panda912.bandage.Processor

/**
 * Created by panda on 2021/12/7 10:25
 */
class UiMsgInterceptor : Processor {

  override fun process(chain: Processor.Chain): Boolean {
    val message = chain.input()

    if (!"huawei".equals(Build.MANUFACTURER, true)) {
      return chain.proceed(message)
    }

    if (message.obj is SomeArgs) {
      val someArgs = message.obj as SomeArgs
      if (someArgs.arg2 is Bundle && (someArgs.arg2 as Bundle).containsKey("IGrabNodeReceiver")) {
        log(message = "huawei is grabing node.")
        return true
      }
      return chain.proceed(message)
    } else if (message.obj.javaClass.name != "android.app.ActivityThread\$RequestContentNode") {
      return chain.proceed(message)
    } else {
      message.obj = null
      message.what = -1
      log(message = "huawei is grabing node.")
      return true
    }

  }
}
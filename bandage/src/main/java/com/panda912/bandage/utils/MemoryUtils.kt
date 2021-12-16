package com.panda912.bandage.utils

import android.os.Build
import android.view.WindowManagerGlobal

/**
 * Created by panda on 2021/12/16 10:10
 */
object MemoryUtils {

  fun cleanGpuMemory(level: Int) {
    try {
      if (Build.VERSION.SDK_INT in 21..29) {
        WindowManagerGlobal.getInstance().trimMemory(80)
      }
    } catch (ignored: Throwable) {
      // ignore
    }
  }
}
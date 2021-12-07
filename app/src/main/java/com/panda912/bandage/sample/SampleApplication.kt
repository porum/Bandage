package com.panda912.bandage.sample

import android.app.Application
import com.panda912.bandage.Bandage
import com.panda912.bandage.utils.ActivityManager

/**
 * Created by panda on 2021/12/7 14:38
 */
class SampleApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    HiddenApiUtil.exemptAll()
    registerActivityLifecycleCallbacks(ActivityManager.getInstance())
    Bandage.hook()
  }
}
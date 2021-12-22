package com.panda912.bandage.sample

import android.app.Application
import com.panda912.bandage.Bandage
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.sample.utils.HiddenApiUtil
import com.panda912.bandage.utils.ActivityManager

/**
 * Created by panda on 2021/12/7 14:38
 */
class SampleApplication : Application() {

  override fun onCreate() {
    super.onCreate()
    HiddenApiUtil.exemptAll()
    registerActivityLifecycleCallbacks(ActivityManager.getInstance())
    Bandage.install(BandageConfig())
    createDynamicBandageData()
  }

  private fun createDynamicBandageData() {
    // click button 'crash 10 / 0'
    val stack1 =
      "        at com.panda912.bandage.sample.MainActivity.onCreate\$lambda-2(MainActivity.kt:25)\n" +
          "        at com.panda912.bandage.sample.MainActivity.\$r8\$lambda\$P7CD1Jj7lHDl5uOwPa991g3Yrqs(Unknown Source:0)\n" +
          "        at com.panda912.bandage.sample.MainActivity\$\$ExternalSyntheticLambda1.onClick(Unknown Source:0)\n" +
          "        at android.view.View.performClick(View.java:7187)\n" +
          "        at android.view.View.performClickInternal(View.java:7160)\n" +
          "        at android.view.View.access\$3500(View.java:824)\n" +
          "        at android.view.View\$PerformClick.run(View.java:27664)\n" +
          "        at android.os.Handler.handleCallback(Handler.java:914)\n" +
          "        at android.os.Handler.dispatchMessage(Handler.java:100)\n" +
          "        at android.os.Looper.loop(Looper.java:225)\n" +
          "        at android.app.ActivityThread.main(ActivityThread.java:7566)\n" +
          "        at java.lang.reflect.Method.invoke(Native Method)\n" +
          "        at com.android.internal.os.RuntimeInit\$MethodAndArgsCaller.run(RuntimeInit.java:539)\n" +
          "        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:994)"
    val data1 = DynamicBandageData(
      process = "all",
      className = "java.lang.ArithmeticException",
      stacks = stack1.lines().map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
      closeCurActivity = false,
      loadPatch = false,
    )

    // click button 'crash in sub thread'
    val stack2 =
      "at com.panda912.bandage.sample.MainActivity\$onCreate\$4\$1.invoke(MainActivity.kt:30)\n" +
          "        at com.panda912.bandage.sample.MainActivity\$onCreate\$4\$1.invoke(MainActivity.kt:29)\n" +
          "        at kotlin.concurrent.ThreadsKt\$thread\$thread\$1.run(Thread.kt:30)"
    val data2 = DynamicBandageData(
      process = "all",
      className = "java.lang.RuntimeException",
      stacks = stack2.lines().map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
      closeCurActivity = false,
      loadPatch = false,
    )

    Bandage.addDynamicBandageData(listOf(data1, data2))
  }
}
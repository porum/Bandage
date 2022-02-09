package com.panda912.bandage.sample

import android.app.Application
import android.content.Context
import com.panda912.bandage.Bandage
import com.panda912.bandage.data.DynamicBandageData
import com.panda912.bandage.sample.bandage.BandageConfig
import com.panda912.bandage.utils.ActivityManager
import me.weishu.reflection.Reflection

/**
 * Created by panda on 2021/12/7 14:38
 */
class SampleApplication : Application() {

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    Reflection.unseal(base)
  }

  override fun onCreate() {
    super.onCreate()
    registerActivityLifecycleCallbacks(ActivityManager.getInstance())
    Bandage.install(BandageConfig(this))
    createDynamicBandageData()
  }

  private fun createDynamicBandageData() {
    // click button 'crash 10 / 0'
    val stack1 =
      "        at com.panda912.bandage.sample.MainActivity.onCreate\$lambda-1(MainActivity.kt:20)\n" +
          "        at com.panda912.bandage.sample.MainActivity.\$r8\$lambda\$rFwK1IpRD1jDgZ5A7zMkoEr2cEs(Unknown Source:0)\n" +
          "        at com.panda912.bandage.sample.MainActivity\$\$ExternalSyntheticLambda3.onClick(Unknown Source:0)\n" +
          "        at android.view.View.performClick(View.java:7575)\n" +
          "        at android.view.View.performClickInternal(View.java:7548)\n" +
          "        at android.view.View.access\$3600(View.java:837)\n" +
          "        at android.view.View\$PerformClick.run(View.java:28902)\n" +
          "        at android.os.Handler.handleCallback(Handler.java:938)\n" +
          "        at android.os.Handler.dispatchMessage(Handler.java:99)\n" +
          "        at android.os.Looper.loop(Looper.java:236)"
    val data1 = DynamicBandageData(
      process = "all",
      exceptionMatch = DynamicBandageData.ExceptionMatch("java.lang.ArithmeticException", ""),
      stacks = stack1.lines().map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
      causes = null,
      closeCurActivity = false,
      loadPatch = false,
    )

    // click button 'crash in sub thread'
    val stack2 =
      "        at com.panda912.bandage.sample.MainActivity\$onCreate\$3\$1.invoke(MainActivity.kt:26)\n" +
          "        at com.panda912.bandage.sample.MainActivity\$onCreate\$3\$1.invoke(MainActivity.kt:25)\n" +
          "        at kotlin.concurrent.ThreadsKt\$thread\$thread\$1.run(Thread.kt:30)"
    val data2 = DynamicBandageData(
      process = "all",
      exceptionMatch = DynamicBandageData.ExceptionMatch(
        "java.lang.RuntimeException",
        "crash in sub thread."
      ),
      stacks = stack2.lines().map { it.trim().replace(Regex("(at |\\([^\\)]*\\))"), "") },
      causes = null,
      closeCurActivity = false,
      loadPatch = false,
    )

    Bandage.addDynamicBandageData(listOf(data1, data2))
  }
}